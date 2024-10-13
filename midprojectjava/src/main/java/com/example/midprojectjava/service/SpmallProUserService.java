/**
 * 쇼핑몰 물품 리뷰 유저관리 서비스입니다
 */
package com.example.midprojectjava.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.midprojectjava.entity.SpmallProUser;
import com.example.midprojectjava.entity.SpmallProduct;
import com.example.midprojectjava.entity.SpmallUser;
import com.example.midprojectjava.exception.DataNotFoundException;
import com.example.midprojectjava.repository.SpmallProUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpmallProUserService {
	 private final SpmallProUserRepository pUserRepository;

	    public SpmallProUser create(SpmallUser sUser, SpmallProduct product, LocalDateTime localDateTime) {
	    	SpmallProUser user = new SpmallProUser();
	        user.setSpmallUser(sUser);
	        user.setSpmallProduct(product);
	        user.setCreateDate(LocalDateTime.now());
	        try {
	            this.pUserRepository.save(user);
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	            e.printStackTrace();
	        }
	        return user;
	    }

	    public SpmallProUser findbyId(Integer userId) {
	        Optional<SpmallProUser> user = this.pUserRepository.findById(userId);
	        if (user.isPresent()) {
	            return user.get();
	        }
	        throw new DataNotFoundException("user not found");
	    }

	    public void deleteByPUser(SpmallProUser pUser) {
	        this.pUserRepository.delete(pUser);
	    }

	    public SpmallProUser findBySpmallUser_Id(Integer userId) {
	    	SpmallProUser pUser = this.pUserRepository.findBySpmallUser_Id(userId);
	        if (pUser == null) {
	            throw new DataNotFoundException("구매리스트에 없어요");
	        }
	        return pUser;
	    }
}
