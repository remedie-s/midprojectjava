/**
 * 쇼핑몰 이용자 관리 서비스입니다
 */
package com.example.midprojectjava.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.midprojectjava.entity.SpmallUser;
import com.example.midprojectjava.exception.DataNotFoundException;
import com.example.midprojectjava.repository.SpmallUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpmallUserService {
	
	  private final SpmallUserRepository spmallUserRepository;
	  
	  private final PasswordEncoder passwordEncoder;

	    public SpmallUser findById(Integer id) {
	        Optional<SpmallUser> byId = this.spmallUserRepository.findById(id);
	        if (byId.isPresent()) {
	            return byId.get();
	        } else {
	            throw new DataNotFoundException("user not found");
	        }

	    }

	    public SpmallUser findByUsername(String name) {
	        Optional<SpmallUser> user = this.spmallUserRepository.findByUsername(name);
	        if (user.isPresent()) {
	            return user.get();
	        }
	        throw new DataNotFoundException("user not found");
	    }

	    public SpmallUser create(String username, String password, String firstName, String lastName,
	            String phoneNumber, String email) {
	    	SpmallUser user = new SpmallUser();
	        user.setUsername(username);
	        user.setPassword(this.passwordEncoder.encode(password));
	        user.setFirstName(firstName);
	        user.setLastName(lastName);
	        user.setPhoneNumber(phoneNumber);
	        System.out.println(phoneNumber);
	        user.setEmail(email);
	        System.out.println(email);
	        user.setUserGrade(0);
	        user.setCreateDate(LocalDateTime.now());
	        try {
	            this.spmallUserRepository.save(user);
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	            e.printStackTrace();
	        }
	        return user;
	    }
}
