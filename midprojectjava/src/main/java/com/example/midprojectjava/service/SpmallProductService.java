/**
 * 쇼핑몰 물품 서비스입니다
 */
package com.example.midprojectjava.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.midprojectjava.entity.SpmallProduct;
import com.example.midprojectjava.exception.DataNotFoundException;
import com.example.midprojectjava.repository.SpmallProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpmallProductService {
	 private final SpmallProductRepository productRepository;

	    public List<SpmallProduct> getAllProduct() {

	        return this.productRepository.findAll();
	    }

	    public Page<SpmallProduct> getAllProduct(Pageable pageable) {
	        return productRepository.findAll(pageable);
	    }

	    public SpmallProduct create(String productName, String description, Long price, Integer quantity, Integer category,
	            String imageUrl) {
	    	SpmallProduct p = new SpmallProduct();
	        p.setProductName(productName);
	        p.setDescription(description);
	        p.setPrice(price);
	        p.setQuantity(quantity);
	        p.setCategory(category);
	        p.setImageUrl(imageUrl);
	        p.setCreateDate(LocalDateTime.now());
	        p.setSellCount(0);
	        this.productRepository.save(p);
	        return p;
	    }

	    public void delete(SpmallProduct product) {
	        this.productRepository.delete(product);
	    }

	    public void modify(SpmallProduct product) {
	        this.productRepository.save(product);
	    }

	    public void save(SpmallProduct product) {
	        this.productRepository.save(product);
	    }

	    public SpmallProduct findById(Integer id) {
	        Optional<SpmallProduct> product = this.productRepository.findById(id);
	        if (product.isPresent()) {
	            return product.get();
	        }
	        throw new DataNotFoundException("product not found");
	    }
}
