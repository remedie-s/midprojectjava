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
        	cartListDto.setImageUrl(spmallCart.getSpmallProduct().getImageUrl());
        	cartListDto.setPrice(spmallCart.getSpmallProduct().getPrice());
        	cartListDto.setProductName(spmallCart.getSpmallProduct().getProductName());
        	cartListDto.setQuantity(spmallCart.getQuantity());
        	cartListDtos.add(cartListDto);
		}
        

        return ResponseEntity.ok(cartListDtos);
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
    public ResponseEntity<String> cartToOrder(@Valid CartToOrderForm cartToOrderForm, @AuthenticationPrincipal SpmallUser spmallUser) {
    	//보안을 위해 스프링시큐리티 유저정보에서 아이디 정보를 가져옴 @AuthenticationPrincipal SpmallUser spmallUser 
    	List<Integer> productIds = cartToOrderForm.getProductIdList();
    	// 로그인 정보 확인
    	if(!spmallUser.getId().equals(cartToOrderForm.getUserId()) ) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인 정보가 다릅니다.");
    	}
    	// 카트 상품 조회
        List<SpmallCart> spmallCarts = this.spmallCartService.findBySpmallUserId(spmallUser.getId());
        if (spmallCarts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("카트에 상품이 없습니다.");
        }
        for (Integer productId : productIds) {
        	for (SpmallCart spmallCart : spmallCarts) {
        	SpmallProduct product = spmallCart.getSpmallProduct();
        	if(product.getId().equals(productIds)) {
        	 if (product.getQuantity() < spmallCart.getQuantity()) {
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                		 ("재고가 부족합니다: " + product.getProductName());
             }
            SpmallOrder spmallOrder = new SpmallOrder();
            spmallOrder.setStatus(0);
            spmallOrder.setRequest(0);
            spmallOrder.setQuantity(spmallCart.getQuantity());
            spmallOrder.setCreateDate(LocalDateTime.now());
            spmallOrder.setSpmallUser(spmallUser);
            spmallOrder.setSpmallProduct(spmallCart.getSpmallProduct());
            this.spmallOrderService.save(spmallOrder);
            log.info("{}의 주문이 완료되었습니다.", spmallCart.getSpmallProduct().getProductName());            
//            log.info("{}의 판매 카운트를 증가시킵니다.",spmallCart.getSpmallProduct().getProductName());
            product.setSellCount((product.getSellCount()+spmallCart.getQuantity()));
//            log.info("{}의 재고 수량을 감소시킵니다.",spmallCart.getSpmallProduct().getProductName());
            product.setQuantity(product.getQuantity()-spmallCart.getQuantity());
            this.spmallProductService.save(product);
            log.info("주문이 완료되어 카트의 내용 중 주문내역을 제거합니다.");
            this.spmallCartService.delete(spmallCart);
        		}
        	}
        }
        
        return ResponseEntity.ok("카트에서 주문으로 변환이 완료되었습니다.");
    }
}
