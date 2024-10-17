package com.example.midprojectjava.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.midprojectjava.dto.SpmallAddressForm;
import com.example.midprojectjava.entity.SpmallAddress;
import com.example.midprojectjava.entity.SpmallUser;
import com.example.midprojectjava.service.SpmallAddressService;
import com.example.midprojectjava.service.SpmallCartService;
import com.example.midprojectjava.service.SpmallOrderService;
import com.example.midprojectjava.service.SpmallProductService;
import com.example.midprojectjava.service.SpmallUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/address")
@RequiredArgsConstructor
public class SpmallAddressController {

    private final SpmallAddressService spmallAddressService;
    private final SpmallUserService spmallUserService;

    @PostMapping("/create")
    public ResponseEntity<String> createAddress(@Valid @RequestBody SpmallAddressForm spmallAddressForm, @AuthenticationPrincipal SpmallUser spmallUser) {
        
        SpmallAddress spmallAddress = new SpmallAddress();
        spmallAddress.setBuildingNumber(spmallAddressForm.getBuildingNumber());
        spmallAddress.setStreetName(spmallAddressForm.getStreetName());
        spmallAddress.setDetailAddress(spmallAddressForm.getDetailAddress());
        spmallAddress.setCity(spmallAddressForm.getCity());
        spmallAddress.setSpmallUser(spmallUser);
        this.spmallAddressService.save(spmallAddress);
        log.info("주소 등록 완료: {}", spmallAddressForm);
        return ResponseEntity.ok("주소 등록이 완료되었습니다.");
    }

    @PostMapping("/list")
    public ResponseEntity<List<SpmallAddress>> listAddress(@AuthenticationPrincipal SpmallUser spmallUser) {
        List<SpmallAddress> list = this.spmallAddressService.findBySpmallUser(spmallUser);
        log.info("주소 리스트 요청이 들어왔습니다.");
        return ResponseEntity.ok(list);
    }
}

