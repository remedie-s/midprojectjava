package com.example.midprojectjava.controller;

import java.time.LocalDateTime;
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

import com.example.midprojectjava.dto.SpmallOrderForm;
import com.example.midprojectjava.entity.SpmallOrder;
import com.example.midprojectjava.entity.SpmallUser;
import com.example.midprojectjava.service.SpmallOrderService;
import com.example.midprojectjava.service.SpmallProUserService;
import com.example.midprojectjava.service.SpmallProductService;
import com.example.midprojectjava.service.SpmallUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/order")
@RestController
public class SpmallOrderController {
    private final SpmallUserService spmallUserService;
    private final SpmallProductService spmallProductService;
    private final SpmallOrderService spmallOrderService;
    private final SpmallProUserService spmallProUserService;

    @GetMapping("/list/")
    public ResponseEntity<List<SpmallOrder>> orderList(@AuthenticationPrincipal SpmallUser spmallUser) {
        log.info("{}님의 주문 내역 리스트 요청이 들어왔습니다.", spmallUser.getFirstName());
        List<SpmallOrder> orders = spmallOrderService.findBySpmallUser_Id(spmallUser.getId());
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/list/admin")
    public ResponseEntity<List<SpmallOrder>> orderListAdmin() {
    	//TODO 인증정보 갈라야함
        log.info("모든 이용자의 주문 내역 리스트 요청이 들어왔습니다.");
        List<SpmallOrder> orders = spmallOrderService.getAllOrder();
        return ResponseEntity.ok(orders);
    }
    @Transactional
    @PostMapping("/modify/{id}") // 주문 수정 및 리뷰 권한 부여
    public ResponseEntity<List<SpmallOrder>> modifyOrder(@PathVariable("id") Integer id, @RequestBody SpmallOrderForm spmallOrderForm) {
        try {
            SpmallOrder spmallOrder = spmallOrderService.findById(id);
            if (spmallOrder == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // 주문 내용 수정
            spmallOrder.setRequest(spmallOrderForm.getRequest());
            spmallOrder.setStatus(spmallOrderForm.getStatus());
            spmallOrderService.save(spmallOrder);

            // 리뷰 권한 부여 로직 (구매자 목록에 추가)
            if(spmallOrderForm.getStatus().equals(100)) {
	            if (!spmallProUserService.findBySpmallUser_Id(spmallOrderForm.getUserId()).getSpmallProduct()
	                    .equals(spmallProductService.findById(spmallOrderForm.getProductId()))) {
	                spmallProUserService.create(spmallUserService.findById(spmallOrderForm.getUserId()),
	                        spmallProductService.findById(spmallOrderForm.getProductId()), LocalDateTime.now());
	                log.info("구매자 목록에 유저를 추가하였습니다.");
	            }
            }

            log.info("{}번의 주문 내용 수정 요청이 들어왔습니다.", id);
            List<SpmallOrder> list = spmallOrderService.findBySpmallUser_Id(spmallOrderForm.getUserId());
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("주문 수정 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<List<SpmallOrder>> deleteOrder(@PathVariable("id") Integer id, @RequestBody SpmallOrderForm spmallOrderForm) {
        try {
            SpmallOrder spmallOrder = spmallOrderService.findById(id);
            if (spmallOrder == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            spmallOrderService.delete(spmallOrder);
            log.info("{}번의 주문 내용 삭제 요청이 들어왔습니다.", id);

            List<SpmallOrder> list = spmallOrderService.findBySpmallUser_Id(spmallOrderForm.getUserId());
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("주문 삭제 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
