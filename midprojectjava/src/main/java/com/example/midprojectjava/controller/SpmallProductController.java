package com.example.midprojectjava.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.midprojectjava.dto.SpmallProductForm;
import com.example.midprojectjava.entity.SpmallProduct;
import com.example.midprojectjava.service.SpmallProductService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/product")
public class SpmallProductController {
	
	@Autowired
	private SpmallProductService productService;
	
	@GetMapping
	public List<SpmallProduct> getProductList(){
		return this.productService.getAllProduct();
	}
	@GetMapping
	public List<SpmallProduct> getProductListByCategory(String category){
		return this.productService.getProductListByCategory(category);
	}
	@GetMapping
	public SpmallProduct getProduct(Integer productId) {
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
	

}
