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

import com.example.midprojectjava.dto.SpmallProductForm;
import com.example.midprojectjava.dto.SpmallProductReivewForm;
import com.example.midprojectjava.entity.SpmallProReview;
import com.example.midprojectjava.entity.SpmallProduct;
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
	
	
	@GetMapping("/list")
	public List<SpmallProduct> getProductList(){
		return this.productService.getAllProduct();
	}
	@GetMapping("/list/category")
	public List<SpmallProduct> getProductListByCategory(String category){
		return this.productService.getProductListByCategory(category);
	}
	@GetMapping("/{productId}")
	public SpmallProduct getProduct(@PathVariable Integer productId) {
		return this.productService.findById(productId);
	}
	
	@PostMapping
	public ResponseEntity<?> createProduct(@Valid @RequestBody SpmallProductForm spmallProductForm, HttpServletResponse response) {
		Map<String, String> responseBody = new HashMap<>();
		try {this.productService.create(spmallProductForm.getProductName(),
				spmallProductForm.getDescription(), spmallProductForm.getPrice(),
				spmallProductForm.getQuantity(), spmallProductForm.getCategory(), spmallProductForm.getImageUrl());
		 
		
 	    responseBody.put("id", spmallProductForm.getId().toString());
 	    responseBody.put("productName", spmallProductForm.getProductName());
		}
		catch(Exception e) {
            return ResponseEntity.status(500).body("회원가입 오류입니다.");
        }
		return ResponseEntity.ok(responseBody);
	}
	
	@PostMapping("/review")
	public ResponseEntity<?> createProductReview(@Valid @RequestBody SpmallProductReivewForm spmallProductReivewForm, HttpServletResponse response) {
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
