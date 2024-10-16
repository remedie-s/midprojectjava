package com.example.midprojectjava.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.midprojectjava.dto.SpmallProductCartForm;
import com.example.midprojectjava.dto.SpmallProductForm;
import com.example.midprojectjava.dto.SpmallProductReviewForm;
import com.example.midprojectjava.entity.SpmallCart;
import com.example.midprojectjava.entity.SpmallProReview;
import com.example.midprojectjava.entity.SpmallProduct;
import com.example.midprojectjava.service.SpmallCartService;
import com.example.midprojectjava.service.SpmallProReviewService;
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
	

	@GetMapping("/list/{category}")
	public List<SpmallProduct> getProductListByCategory(@PathVariable("category") String category){
		if(category.equals("all")) {
			log.info("모든 자료에대한 요청이 들어왔습니다");
			return this.productService.getAllProduct(); //모든 자료 요청
		}	
		log.info(category+"에 대한 요청이 들어왔습니다");
		return this.productService.getProductListByCategory(category); // 자료 하나만 요청
	}
	@GetMapping("/{productId}")
	public SpmallProduct getProduct(@PathVariable("productId") Integer productId) {
		log.info(this.productService.findById(productId).getProductName()+"의 정보입니다");
		return this.productService.findById(productId);
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
		return ResponseEntity.ok(responseBody);
	}
	
	@PostMapping("/cart")
	public ResponseEntity<?> productToCart(@Valid @RequestBody SpmallProductCartForm spmallProductCartForm, HttpServletResponse response) {
		Map<String, Object> responseBody = new HashMap<>();
		try {
			// 유저 아이디로 카트리스트를 찾은 다음 물품 존재 여부 확인
			List<SpmallCart>cartlist=this.spmallCartService.findBySpmallUserId(spmallProductCartForm.getUserId());
			for (SpmallCart spmallCart : cartlist) {
				if(spmallCart.getSpmallProduct().getId()==spmallProductCartForm.getProductId()) {
					spmallCart.setQuantity(spmallCart.getQuantity()+spmallProductCartForm.getQuantity());
					log.info("장바구니에 이미 존재하여 갯수를 추가하였습니다");
				}
			}//TODO 고쳐야함
			 this.spmallCartService.create(spmallProductCartForm.getQuantity(),
			 this.productService.findById(spmallProductCartForm.getProductId()), this.spmallUserService.findById(spmallProductCartForm.getUserId()) );
			 
			 log.info("카트 등록에 성공했습니다.");
			 responseBody.put("userId", spmallProductCartForm.getUserId());
			 responseBody.put("productId", spmallProductCartForm.getProductId());	
		}
		catch(Exception e) {
            return ResponseEntity.status(500).body("카트 등록 오류입니다.");
        }
		return ResponseEntity.ok(responseBody);
	}
	
	@PostMapping("/review")
	public ResponseEntity<?> createProductReview(@Valid @RequestBody SpmallProductReviewForm spmallProductReivewForm, HttpServletResponse response) {
		Map<String, String> responseBody = new HashMap<>();
		try {
			this.spmallProReviewService.create(spmallProductReivewForm.getContent(),this.spmallUserService.findById(spmallProductReivewForm.getUserId()) 
					, this.productService.findById(spmallProductReivewForm.getProductId()));
			responseBody.put("id", spmallProductReivewForm.getUserId().toString());
			responseBody.put("content", spmallProductReivewForm.getContent());
		}
		catch(Exception e){
			return ResponseEntity.status(500).body("리뷰 입력 오류입니다.");
		}
		return ResponseEntity.ok(responseBody);
	}
	
	@GetMapping("/{productId}/reveiw")
	public List<SpmallProReview> getReviewList(Integer productId){
		return  this.spmallProReviewService.findBySpmallProduct_Id(productId);
	}
	
	
	

}
