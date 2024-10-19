package com.example.midprojectjava.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.midprojectjava.dto.SpmallOrderForm;
import com.example.midprojectjava.dto.SpmallProductCartForm;
import com.example.midprojectjava.dto.SpmallProductForm;
import com.example.midprojectjava.dto.SpmallProductReviewForm;
import com.example.midprojectjava.entity.SpmallCart;
import com.example.midprojectjava.entity.SpmallOrder;
import com.example.midprojectjava.entity.SpmallProReview;
import com.example.midprojectjava.entity.SpmallProUser;
import com.example.midprojectjava.entity.SpmallProduct;
import com.example.midprojectjava.entity.SpmallUser;
import com.example.midprojectjava.service.SpmallCartService;
import com.example.midprojectjava.service.SpmallProReviewService;
import com.example.midprojectjava.service.SpmallProUserService;
import com.example.midprojectjava.service.SpmallProductService;
import com.example.midprojectjava.service.SpmallUserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/product")
public class SpmallProductController {
	
	@Autowired
	private SpmallProductService productService;
	@Autowired
	private SpmallProReviewService spmallProReviewService;
	@Autowired
	private SpmallUserService spmallUserService;
	@Autowired
	private SpmallCartService spmallCartService;
	@Autowired
	private SpmallProUserService spmallProUserService;
	
	 // 카테고리별 제품 목록
    @GetMapping("/list/{category}")
    public ResponseEntity<List<SpmallProduct>> getProductListByCategory(@PathVariable("category") String category) {
        List<SpmallProduct> productList;
        if ("all".equals(category)) {
            log.info("모든 자료에 대한 요청이 들어왔습니다.");
            productList = this.productService.getAllProduct();
        } else {
            log.info("{} 카테고리에 대한 요청이 들어왔습니다.", category);
            productList = this.productService.getProductListByCategory(category);
        }
        return ResponseEntity.ok(productList);
    }

    // 특정 제품 정보
    @GetMapping("/{productId}")
    public ResponseEntity<SpmallProduct> getProduct(@PathVariable("productId") Integer productId) {
        SpmallProduct product = this.productService.findById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        log.info("{}의 정보입니다.", product.getProductName());
        return ResponseEntity.ok(product);
    }
    
    @PostMapping("/create")
	public ResponseEntity<?> createProduct(@Valid @RequestBody SpmallProductForm spmallProductForm, HttpServletResponse response) {
		Map<String, String> responseBody = new HashMap<>();
		try {
			this.productService.create(spmallProductForm.getProductName(),
			spmallProductForm.getDescription(), spmallProductForm.getPrice(),
			spmallProductForm.getQuantity(), spmallProductForm.getCategory(), spmallProductForm.getImageUrl());
			log.info(spmallProductForm.getProductName()+"의 등록이 요청되었습니다.");
			responseBody.put("id", this.productService.findByProductName(spmallProductForm.getProductName()).getId().toString());
			responseBody.put("productName", spmallProductForm.getProductName());
		}
		catch(Exception e) {
            return ResponseEntity.status(500).body("물품 등록 오류입니다.");
        }
		return ResponseEntity.ok(responseBody);}
    
    @PostMapping("/cart")
    public ResponseEntity<?> productToCart(@Valid @RequestBody SpmallProductCartForm spmallProductCartForm, BindingResult result, HttpServletResponse response) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        Map<String, Object> responseBody = new HashMap<>();
        try {
            // 유저 아이디로 카트리스트를 찾은 다음 물품 존재 여부 확인
            List<SpmallCart> cartlist = this.spmallCartService.findBySpmallUserId(spmallProductCartForm.getUserId());
            SpmallCart existingCart = null;

            for (SpmallCart spmallCart : cartlist) {
                if (spmallCart.getSpmallProduct().getId().equals(spmallProductCartForm.getProductId())) {
                    existingCart = spmallCart; // 이미 존재하는 카트 항목을 찾음
                    break;
                }
            }

            if (existingCart != null) {
                // 기존 카트 항목의 수량을 업데이트
                existingCart.setQuantity(existingCart.getQuantity() + spmallProductCartForm.getQuantity());
                log.info("수량 업데이트: " + existingCart.getQuantity());
                this.spmallCartService.save(existingCart); // 업데이트
            } else {
                // 새로운 카트 항목 생성
                SpmallProduct product = this.productService.findById(spmallProductCartForm.getProductId());
                SpmallUser user = this.spmallUserService.findById(spmallProductCartForm.getUserId());

                SpmallCart newCart = new SpmallCart();
                newCart.setQuantity(spmallProductCartForm.getQuantity());
                newCart.setSpmallProduct(product);
                newCart.setSpmallUser(user);
                newCart.setCreateDate(LocalDateTime.now()); // 생성일 추가
                this.spmallCartService.save(newCart); // 새로운 카트 항목 생성
                log.info("카트 등록에 성공했습니다.");
            }

            responseBody.put("userId", spmallProductCartForm.getUserId());
            responseBody.put("productId", spmallProductCartForm.getProductId());
        } catch (Exception e) {
            log.error("카트 등록 중 오류 발생: ", e);
            return ResponseEntity.status(500).body("카트 등록 중 오류가 발생했습니다.");
        }
        return ResponseEntity.ok(responseBody);
    }

	
	@PostMapping("/modify/{id}")
    public ResponseEntity<List<SpmallProduct>> modifyProduct(@PathVariable("id") Integer id, @Valid @RequestBody SpmallProductForm spmallProductForm) {
        log.info("상품번호 : {}번의 수정요청이 들어왔습니다.", id);
        
        SpmallProduct spmallProduct = this.productService.findById(id);
        if (spmallProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        spmallProduct.setQuantity(spmallProductForm.getQuantity());
        spmallProduct.setCategory(spmallProductForm.getCategory());
        spmallProduct.setDescription(spmallProductForm.getDescription());
        spmallProduct.setPrice(spmallProductForm.getPrice());
        spmallProduct.setProductName(spmallProductForm.getProductName());
        spmallProduct.setImageUrl(spmallProductForm.getImageUrl());
        this.productService.save(spmallProduct);
        log.info("물품의 변경이 완료되었습니다.");
        
        List<SpmallProduct> updatedProductList = this.productService.getProductListByCategory(spmallProductForm.getCategory());
        return ResponseEntity.ok(updatedProductList);
    }
	
	
	 @PostMapping("/delete/{id}")
	    public ResponseEntity<List<SpmallProduct>> deleteProduct(@PathVariable("id") Integer id, @RequestBody SpmallProductForm spmallProductForm) {
	        try {
	            SpmallProduct spmallProduct = this.productService.findById(id);
	            if (spmallProduct == null) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	            }

	            productService.delete(spmallProduct);
	            log.info("{}번의 상품 삭제 요청이 들어왔습니다.", id);

	            List<SpmallProduct> list = this.productService.getProductListByCategory(spmallProductForm.getCategory());
	            return ResponseEntity.ok(list);
	        } catch (Exception e) {
	            log.error("상품 삭제 중 오류 발생: ", e);
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }
	
	@PostMapping("/review")
	public ResponseEntity<?> createProductReview(@Valid @RequestBody SpmallProductReviewForm spmallProductReivewForm, HttpServletResponse response, @AuthenticationPrincipal SpmallUser spmallUser) {
		Map<String, Object> responseBody = new HashMap<>();
		
		log.info("{}님의 리뷰등록 요청이 들어왔습니다.", spmallUser.getFirstName());
		if(
		this.spmallProUserService.findBySpmallUserAndSpmallProduct(spmallUser, this.productService.findById(spmallProductReivewForm.getProductId()))==null
		) {
			log.error("물품 구매 리스트에 {}님이 없어요.",spmallUser.getFirstName());
			return ResponseEntity.status(500).body("물품 유저 리스트에 해당 유저가 없습니다.");
		}
		
		try {
			this.spmallProReviewService.create(spmallProductReivewForm.getContent(),spmallProductReivewForm.getRating(),spmallUser 
					, this.productService.findById(spmallProductReivewForm.getProductId()));
			responseBody.put("id", spmallProductReivewForm.getUserId());
			responseBody.put("rating", spmallProductReivewForm.getRating());
			responseBody.put("content", spmallProductReivewForm.getContent());
		}
		catch(Exception e){
			log.error("리뷰 등록 중 오류가 발생하였습니다.: ", e);
			return ResponseEntity.status(500).body("리뷰 입력 오류입니다.");
		}
		return ResponseEntity.ok(responseBody);
	}
	
	// 제품 리뷰 목록
    @GetMapping("/{productId}/review")
    public ResponseEntity<List<SpmallProReview>> getReviewList(@PathVariable("productId") Integer productId) {
        List<SpmallProReview> reviews = this.spmallProReviewService.findBySpmallProduct_Id(productId);
        if (reviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(reviews);
    }
	
	

}
