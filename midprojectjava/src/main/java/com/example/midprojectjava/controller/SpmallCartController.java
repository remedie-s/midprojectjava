package com.example.midprojectjava.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.midprojectjava.dto.CartListDto;
import com.example.midprojectjava.dto.CartSummaryDto;
import com.example.midprojectjava.dto.CartToOrderForm;
import com.example.midprojectjava.dto.SpmallProductCartForm;
import com.example.midprojectjava.entity.SpmallCart;
import com.example.midprojectjava.entity.SpmallOrder;
import com.example.midprojectjava.entity.SpmallProduct;
import com.example.midprojectjava.entity.SpmallUser;
import com.example.midprojectjava.service.SpmallCartService;
import com.example.midprojectjava.service.SpmallOrderService;
import com.example.midprojectjava.service.SpmallProductService;
import com.example.midprojectjava.service.SpmallUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
public class SpmallCartController {

    private final SpmallCartService spmallCartService;
    private final SpmallUserService spmallUserService;
    private final SpmallOrderService spmallOrderService;
    private final SpmallProductService spmallProductService;

    @GetMapping("/list")
    public ResponseEntity<List<CartListDto>> getCartList(@AuthenticationPrincipal SpmallUser spmallUser) {
        List<SpmallCart> cartList = this.spmallCartService.findBySpmallUserId(spmallUser.getId());
        
        if (cartList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List <CartListDto> cartListDtos = new ArrayList<>();
        for (SpmallCart spmallCart : cartList) {
        	CartListDto cartListDto=new CartListDto();
        	cartListDto.setId(spmallCart.getId());
        	cartListDto.setProductId(spmallCart.getSpmallProduct().getId());
        	cartListDto.setImageUrl(spmallCart.getSpmallProduct().getImageUrl());
        	cartListDto.setPrice(spmallCart.getSpmallProduct().getPrice());
        	cartListDto.setProductName(spmallCart.getSpmallProduct().getProductName());
        	cartListDto.setQuantity(spmallCart.getQuantity());
        	cartListDtos.add(cartListDto);
		}
        return ResponseEntity.ok(cartListDtos);
    }
    @GetMapping("/summary")
    public ResponseEntity<CartSummaryDto> getCartListSummary(@AuthenticationPrincipal SpmallUser spmallUser) {
        List<SpmallCart> cartList = this.spmallCartService.findBySpmallUserId(spmallUser.getId());
        
        if (cartList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        CartSummaryDto cartSummaryDto = new CartSummaryDto();
        Long CostSum=0L;
        Integer QuantitySum=0;
        
        for (SpmallCart spmallCart : cartList) {
        	CostSum += spmallCart.getSpmallProduct().getPrice();
        	QuantitySum+=spmallCart.getQuantity();
		}
        cartSummaryDto.setTotalCostSum(CostSum);
        cartSummaryDto.setTotalQuantitySum(QuantitySum);
        cartSummaryDto.setUserId(spmallUser.getId());
        return ResponseEntity.ok(cartSummaryDto);
    }

    @PostMapping("/modify/{id}")
    public ResponseEntity<List<CartListDto>> modifyCart(@PathVariable("id") Integer id, 
    		@Valid @RequestBody SpmallProductCartForm spmallProductCartForm, 
    		@AuthenticationPrincipal SpmallUser spmallUser) {
        log.info("카트번호 : {}번의 수정요청이 들어왔습니다.", id);
        
        SpmallCart spmallCart = this.spmallCartService.findbyId(id);
        if (spmallCart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        spmallCart.setQuantity(spmallProductCartForm.getQuantity());
        this.spmallCartService.save(spmallCart);
        log.info("카트의 변경이 완료되었습니다.");
        
        List<CartListDto> list = getCartList(spmallUser).getBody();
        
        return ResponseEntity.ok(list);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<List<CartListDto>> deleteCart(@PathVariable("id") Integer id, 
    		@Valid @RequestBody SpmallProductCartForm spmallProductCartForm,
    		@AuthenticationPrincipal SpmallUser spmallUser) {
        log.info("카트번호 : {}번의 삭제요청이 들어왔습니다.", id);

        SpmallCart spmallCart = this.spmallCartService.findbyId(id);
        if (spmallCart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        this.spmallCartService.delete(spmallCart);
        log.info("카트의 물품 삭제가 완료되었습니다.");
        List<CartListDto> list = getCartList(spmallUser).getBody();
        
        return ResponseEntity.ok(list);
    }

    @PostMapping("/cartToOrder")
    @Transactional
    public ResponseEntity<String> cartToOrder(@Valid @RequestBody CartToOrderForm cartToOrderForm, 
    		@AuthenticationPrincipal SpmallUser spmallUser) {
        log.info("주문을 시작합니다.");
        
        // 로그인 정보 확인
        if (!spmallUser.getId().equals(cartToOrderForm.getUserId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인 정보가 다릅니다.");
        }

        // 카트 상품 조회
        List<SpmallCart> spmallCarts = this.spmallCartService.findBySpmallUserId(spmallUser.getId());
        if (spmallCarts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("카트에 상품이 없습니다.");
        }

        // 특정 productId로 카트검색
        SpmallCart spmallCart = findCartByProductId(spmallCarts, cartToOrderForm.getProductId());
        if (spmallCart == null) {
            log.info("카트에 제품이 존재하지 않습니다: {}", cartToOrderForm.getProductId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("카트에 제품이 존재하지 않습니다: " + cartToOrderForm.getProductId());
        }


        SpmallProduct product = spmallCart.getSpmallProduct();
        
        // 재고 확인
        if (product.getQuantity() < cartToOrderForm.getQuantity()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("재고가 부족합니다: " + product.getProductName());
        }
        log.info("재고확인후 주문을 생성합니다");

        // 주문 생성
        SpmallOrder spmallOrder = new SpmallOrder();
        spmallOrder.setStatus(0);
        spmallOrder.setRequest(0);
        spmallOrder.setQuantity(cartToOrderForm.getQuantity());
        spmallOrder.setCreateDate(LocalDateTime.now());
        spmallOrder.setSpmallUser(spmallUser);
        spmallOrder.setSpmallProduct(product);
        this.spmallOrderService.save(spmallOrder);

        log.info("{}의 주문이 완료되었습니다.", product.getProductName());

        // 판매 수량 증가 및 재고 감소
        product.setSellCount(product.getSellCount() + cartToOrderForm.getQuantity());
        product.setQuantity(product.getQuantity() - cartToOrderForm.getQuantity());
        this.spmallProductService.save(product);
        
        log.info("주문이 완료되어 카트의 내용 중 주문내역을 제거합니다.");
        this.spmallCartService.delete(spmallCart);

        return ResponseEntity.ok("주문이 성공적으로 완료되었습니다.");
    }
    
    private SpmallCart findCartByProductId(List<SpmallCart> spmallCarts, Integer productId) {
    	 for (SpmallCart cart : spmallCarts) {
    	        if (cart.getSpmallProduct().getId().equals(productId)) {
    	            return cart; // 찾은 카트 반환
    	        }
    	    }
    	    return null;
	}


	
}
