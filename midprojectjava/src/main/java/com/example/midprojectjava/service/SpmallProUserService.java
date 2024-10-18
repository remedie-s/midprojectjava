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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpmallProUserService {
	 private final SpmallProUserRepository pUserRepository;

	    public void create(SpmallUser sUser, SpmallProduct product, LocalDateTime localDateTime) {
	    	SpmallProUser user = new SpmallProUser();
	        user.setSpmallUser(sUser);
	        user.setSpmallProduct(product);
	        user.setCreateDate(LocalDateTime.now());
	        log.info("구매자목록에 추가합니다.");
	           this.pUserRepository.save(user);
	        
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
	        	log.info("구매자 목록에 주문을 완료한 유저가 없어 유저를 추가합니다.");
	            pUser= new SpmallProUser();
	        }
	        return pUser;
	    }

		public SpmallProUser findBySpmallUserAndSpmallProduct(SpmallUser user, SpmallProduct product) {
			SpmallProUser spmallProUser=this.pUserRepository.findBySpmallUserAndSpmallProduct(user,product);
			return spmallProUser;
		}
}
