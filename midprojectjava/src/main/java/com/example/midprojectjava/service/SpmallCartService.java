/**
 * 쇼핑몰 카트 서비스입니다
 */
package com.example.midprojectjava.service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.midprojectjava.entity.SpmallCart;
import com.example.midprojectjava.entity.SpmallProduct;
import com.example.midprojectjava.entity.SpmallUser;
import com.example.midprojectjava.exception.DataNotFoundException;
import com.example.midprojectjava.repository.SpmallCartRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpmallCartService {
	 private final SpmallCartRepository sCartRepository;

	    public SpmallCart findbyId(Integer cartId) {
	        Optional<SpmallCart> scart = this.sCartRepository.findById(cartId);
	        if (scart.isPresent()) {
	            return scart.get();
	        }
	        throw new DataNotFoundException("user not found");
	    }

	    public void delete(SpmallCart cart) {
	        this.sCartRepository.delete(cart);
	    }

	    
	    public void create(Integer quantity, SpmallProduct spmallProduct, SpmallUser spmallUser) {
	    	SpmallCart spmallCart = new SpmallCart();
	    	spmallCart.setQuantity(quantity);
	    	spmallCart.setSpmallProduct(spmallProduct);
	    	spmallCart.setSpmallUser(spmallUser);
	    	spmallCart.setCreateDate(LocalDateTime.now());
	    	this.sCartRepository.save(spmallCart);
	    }

	    
	    public List<SpmallCart> findBySpmallUserId(Integer userid) {

	        List<SpmallCart> carts = this.sCartRepository.findBySpmallUser_Id(userid);
	        if (carts.isEmpty()) {
	            throw new DataNotFoundException("cart 가 없어요");
	        }
	        return carts;
	    }

	    public Page<SpmallCart> findBySpmallUserId(Integer userid, Pageable pageable) {
	        Page<SpmallCart> carts = this.sCartRepository.findBySpmallUser_Id(userid, pageable);
	        if (carts.isEmpty()) {
	            throw new DataNotFoundException("cart 가 없어요");
	        }
	        return carts;
	    }

	    @Transactional
	    public void deleteBySpmallUserId(Integer userid) {
	        List<SpmallCart> carts = this.sCartRepository.findBySpmallUser_Id(userid);
	        if (carts.isEmpty()) {
	            throw new DataNotFoundException("cart 가 없어요");
	        }
	        this.sCartRepository.deleteAll(carts);
	    }

		public void save(SpmallCart spmallCart) {
			this.sCartRepository.save(spmallCart);
			
		}
}
