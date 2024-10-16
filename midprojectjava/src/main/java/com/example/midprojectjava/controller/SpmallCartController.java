package com.example.midprojectjava.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.midprojectjava.dto.SpmallProductCartForm;
import com.example.midprojectjava.entity.SpmallCart;
import com.example.midprojectjava.entity.SpmallOrder;
import com.example.midprojectjava.service.SpmallCartService;
import com.example.midprojectjava.service.SpmallOrderService;
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

    @GetMapping("/list/{id}")
    public ResponseEntity<List<SpmallCart>> getCartListById(@PathVariable("id") Integer id) {
        log.info("회원번호 : {}번의 카트리스트 요청이 들어왔습니다.", id);
        List<SpmallCart> cartList = this.spmallCartService.findBySpmallUserId(id);
        
        if (cartList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(cartList);
    }

    @PostMapping("/modify/{id}")
    public ResponseEntity<List<SpmallCart>> modifyCart(@PathVariable("id") Integer id, @Valid @RequestBody SpmallProductCartForm spmallProductCartForm) {
        log.info("카트번호 : {}번의 수정요청이 들어왔습니다.", id);
        
        SpmallCart spmallCart = this.spmallCartService.findbyId(id);
        if (spmallCart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        spmallCart.setQuantity(spmallProductCartForm.getQuantity());
        this.spmallCartService.save(spmallCart);
        log.info("카트의 변경이 완료되었습니다.");
        
        List<SpmallCart> updatedCartList = this.spmallCartService.findBySpmallUserId(spmallProductCartForm.getUserId());
        return ResponseEntity.ok(updatedCartList);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<List<SpmallCart>> deleteCart(@PathVariable("id") Integer id, @Valid @RequestBody SpmallProductCartForm spmallProductCartForm) {
        log.info("카트번호 : {}번의 삭제요청이 들어왔습니다.", id);

        SpmallCart spmallCart = this.spmallCartService.findbyId(id);
        if (spmallCart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        this.spmallCartService.delete(spmallCart);
        log.info("카트의 물품 삭제가 완료되었습니다.");

        List<SpmallCart> updatedCartList = this.spmallCartService.findBySpmallUserId(spmallProductCartForm.getUserId());
        return ResponseEntity.ok(updatedCartList);
    }

    @PostMapping("/cartToOrder/{id}")
    @Transactional
    public ResponseEntity<String> cartToOrder(@PathVariable("id") Integer id) {
        List<SpmallCart> spmallCarts = this.spmallCartService.findBySpmallUserId(id);
        if (spmallCarts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("카트에 상품이 없습니다.");
        }

        for (SpmallCart spmallCart : spmallCarts) {
            SpmallOrder spmallOrder = new SpmallOrder();
            spmallOrder.setStatus(0);
            spmallOrder.setRequest(0);
            spmallOrder.setQuantity(spmallCart.getQuantity());
            spmallOrder.setCreateDate(LocalDateTime.now());
            spmallOrder.setSpmallUser(this.spmallUserService.findById(id));
            spmallOrder.setSpmallProduct(spmallCart.getSpmallProduct());
            this.spmallOrderService.save(spmallOrder);
            log.info("{}의 주문이 완료되었습니다.", spmallCart.getSpmallProduct().getProductName());
        }

        return ResponseEntity.ok("카트에서 주문으로 변환이 완료되었습니다.");
    }
}
