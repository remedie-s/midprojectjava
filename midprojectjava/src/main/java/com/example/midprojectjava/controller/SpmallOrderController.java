package com.example.midprojectjava.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.midprojectjava.dto.OrderListDto;
import com.example.midprojectjava.dto.SpmallOrderForm;
import com.example.midprojectjava.entity.SpmallOrder;
import com.example.midprojectjava.entity.SpmallProUser;
import com.example.midprojectjava.entity.SpmallProduct;
import com.example.midprojectjava.entity.SpmallUser;
import com.example.midprojectjava.service.SpmallOrderService;
import com.example.midprojectjava.service.SpmallProUserService;
import com.example.midprojectjava.service.SpmallProductService;
import com.example.midprojectjava.service.SpmallUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/order")
@RestController
public class SpmallOrderController {
    private final SpmallUserService spmallUserService;
    private final SpmallProductService spmallProductService;
    private final SpmallOrderService spmallOrderService;
    private final SpmallProUserService spmallProUserService;

    @GetMapping("/list")
    public ResponseEntity<List<OrderListDto>> orderList(@AuthenticationPrincipal SpmallUser spmallUser) {
    	if (spmallUser == null) {
 	        log.error("AuthenticationPrincipal is null");
 	    } else {
 	        log.info("Authenticated user: {}", spmallUser.getUsername());}
    	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	    log.info("Current authentication: {}", authentication);
        log.info("{}님의 주문 내역 리스트 요청이 들어왔습니다.", spmallUser.getFirstName());
        List<SpmallOrder> orders = spmallOrderService.findBySpmallUser_Id(spmallUser.getId());
        if (orders.isEmpty()) {
        	log.info("주문 내역이 없어요!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        // 오더 리스트디티오 리턴 메소드 유저와 오더리스트 파라미터
        List<OrderListDto> orderListDtos = returnOrderListDtoByUser(spmallUser, orders);
        
        return ResponseEntity.ok(orderListDtos);
    }

    @GetMapping("/list/admin")
    public ResponseEntity<List<OrderListDto>> orderListAdmin(@AuthenticationPrincipal SpmallUser spmallUser) {
    	 if (spmallUser == null) {
 	        log.error("AuthenticationPrincipal is null");
 	    } else {
 	        log.info("Authenticated user: {}", spmallUser.getUsername());}
//		TODO 인증정보 도입시 집어넣을것 다른데에도 집어넣을수있을듯     	
//    	Integer userGrade = spmallUser.getUserGrade();
//    	if(userGrade!=3&&userGrade!=4) {
//    		log.info("당신의 권한이 부족합니다");
//    		 return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//    	}
        log.info("모든 이용자의 주문 내역 리스트 요청이 들어왔습니다.");
        List<SpmallOrder> orders = spmallOrderService.getAllOrder();
     // 오더 리스트디티오 리턴 메소드 유저와 오더리스트 파라미터
        List<OrderListDto> orderListDtos = returnOrderListDtoByUser(spmallUser, orders);
        
        return ResponseEntity.ok(orderListDtos);
    }

	
    @Transactional
    @PostMapping("/modify/{id}") // 주문 수정 및 리뷰 권한 부여
    public ResponseEntity<List<OrderListDto>> modifyOrder(@PathVariable("id") Integer id, @RequestBody SpmallOrderForm spmallOrderForm) {
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
            if (spmallOrderForm.getStatus().equals(4)) {
                if (spmallOrderForm.getUserId() == null) {
                    log.error("유저 ID가 없습니다.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }

                // 유저와 제품 정보 조회
                SpmallUser user = spmallOrder.getSpmallUser();
                SpmallProduct product = spmallOrder.getSpmallProduct();

                // 유효성 검사: 유저와 제품이 없을 경우 처리
                if (user == null) {
                    log.error("유효하지 않은 유저 ID: {}", spmallOrder.getSpmallUser().getId());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
                if (product == null) {
                    log.error("유효하지 않은 제품 ID: {}", spmallOrder.getSpmallProduct().getId());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }

                // 구매자 목록에 있는지 검사
                SpmallProUser pUser = spmallProUserService.findBySpmallUserAndSpmallProduct(user, product);

                // 구매자가 없는 경우에만 추가
                if (pUser == null) {
                    spmallProUserService.create(user, product, LocalDateTime.now());
                    log.info("구매자 목록에 유저 {}를 추가하였습니다.", user.getId());
                } else {
                    log.info("유저 {}는 이미 제품 {}의 구매자 목록에 있습니다.", user.getId(), product.getId());
                }
            }
            
            log.info("{}번의 주문 내용 수정 요청이 들어왔습니다.", id);
            
            List<OrderListDto> orderListDtos = retrunOrderList(spmallOrderForm);
            return ResponseEntity.ok(orderListDtos);
        } catch (Exception e) {
            log.error("주문 수정 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<List<OrderListDto>> deleteOrder(@PathVariable("id") Integer id, @RequestBody SpmallOrderForm spmallOrderForm) {
        try {
            SpmallOrder spmallOrder = spmallOrderService.findById(id);
            if (spmallOrder == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            spmallOrderService.delete(spmallOrder);
            log.info("{}번의 주문 내용 삭제 요청이 들어왔습니다.", id);

            List<OrderListDto> orderListDtos = retrunOrderList(spmallOrderForm);
            return ResponseEntity.ok(orderListDtos);
            
        } catch (Exception e) {
            log.error("주문 삭제 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    
    
    // 오더 리스트디티오 리턴 메소드 오더폼을 넣고 바꿈
	private List<OrderListDto> retrunOrderList(SpmallOrderForm spmallOrderForm) {
		List<SpmallOrder> orders = spmallOrderService.findBySpmallUser_Id(spmallOrderForm.getUserId());
		List<OrderListDto> orderListDtos = new ArrayList<>();
		for (SpmallOrder spmallOrder1 : orders) {
			OrderListDto orderListDto = new OrderListDto();
			orderListDto.setId(spmallOrder1.getId());
			orderListDto.setUserId(spmallOrder1.getSpmallUser().getId());
			orderListDto.setProductId(spmallOrder1.getSpmallProduct().getId());
			orderListDto.setProductUrl(spmallOrder1.getSpmallProduct().getImageUrl());
			orderListDto.setProductPrice(spmallOrder1.getSpmallProduct().getPrice());
			orderListDto.setQuantity(spmallOrder1.getQuantity());
			orderListDto.setStatus(spmallOrder1.getStatus());
			orderListDto.setRequest(spmallOrder1.getRequest());
			orderListDto.setCreateDate(spmallOrder1.getCreateDate());
			orderListDtos.add(orderListDto);
		}
		return orderListDtos;
	}
	 // 오더 리스트디티오 리턴 메소드 유저와 오더리스트 넣고 바꿈
	private List<OrderListDto> returnOrderListDtoByUser(SpmallUser spmallUser, List<SpmallOrder> orders) {
		List<OrderListDto> orderListDtos = new ArrayList<>();
        for (SpmallOrder spmallOrder : orders) {
        	OrderListDto orderListDto = new OrderListDto();
        	orderListDto.setId(spmallOrder.getId());
        	orderListDto.setUserId(spmallUser.getId());
        	orderListDto.setProductId(spmallOrder.getSpmallProduct().getId());
        	orderListDto.setProductUrl(spmallOrder.getSpmallProduct().getImageUrl());
        	orderListDto.setProductPrice(spmallOrder.getSpmallProduct().getPrice());
        	orderListDto.setQuantity(spmallOrder.getQuantity());
        	orderListDto.setStatus(spmallOrder.getStatus());
        	orderListDto.setRequest(spmallOrder.getRequest());
        	orderListDto.setCreateDate(spmallOrder.getCreateDate());
        	orderListDtos.add(orderListDto);
		}
		return orderListDtos;
	}
    
   
}
