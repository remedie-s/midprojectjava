/**
 * 쇼핑몰 주소 서비스입니다
 */
package com.example.midprojectjava.service;

import java.time.DateTimeException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.midprojectjava.entity.SpmallAddress;
import com.example.midprojectjava.entity.SpmallUser;
import com.example.midprojectjava.repository.SpmallAddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpmallAddressService {
	 private final SpmallAddressRepository sAddressRepository;

	    public SpmallAddress findById(Integer id) {

	        Optional<SpmallAddress> byId = this.sAddressRepository.findById(id);
	        if (byId.isPresent()) {
	        	SpmallAddress sAddress = byId.get();
	            return sAddress;
	        }
	        throw new DateTimeException("주소가 없어요");

	    }

	    public SpmallAddress create(SpmallUser user, String streetName, String buildingNumber, String detailAddress, String city) {
	    	SpmallAddress sAddress = new SpmallAddress();
	        sAddress.setSpmallUser(user);
	        sAddress.setStreetName(streetName);
	        sAddress.setBuildingNumber(buildingNumber);
	        sAddress.setDetailAddress(detailAddress);
	        sAddress.setCity(city);
	        this.sAddressRepository.save(sAddress);
	        return sAddress;

	    }

	    public void modify(SpmallAddress sAddress) {
	        this.sAddressRepository.save(sAddress);
	    }

	    public void delete(SpmallAddress sAddress) {
	        this.sAddressRepository.delete(sAddress);
	    }

}
