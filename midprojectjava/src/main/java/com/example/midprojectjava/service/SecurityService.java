package com.example.midprojectjava.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.midprojectjava.config.SpmallUserGrade;
import com.example.midprojectjava.entity.SpmallUser;
import com.example.midprojectjava.repository.SpmallUserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SecurityService {

    @Autowired
    private SpmallUserRepository spmallUserRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("사용자 인증 체크 " + username);

        Optional<SpmallUser> _user = this.spmallUserRepository.findByUsername(username);
        log.info("사용자 인증 체크 " + _user.isPresent());
        if (_user.isEmpty()) {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
        }
        log.info("사용자 인증 체크-1");
        SpmallUser sUser = _user.get();
        log.info("사용자 인증 체크 " + sUser);
        log.info("사용자 인증 체크0");
        List<GrantedAuthority> authorities = new ArrayList<>();
        log.info("사용자 인증 체크1");
        if ("admin".equals(username)) {// TODO 유저그레이드 바꿔야함
            log.info("사용자 인증 체크2");
            authorities.add(new SimpleGrantedAuthority(SpmallUserGrade.ADMIN.getValue()));
        } else if ("seller".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(SpmallUserGrade.SELLER.getValue()));
            log.info("사용자 인증 체크3");
        } else {
            authorities.add(new SimpleGrantedAuthority(SpmallUserGrade.BRONZE.getValue()));
            log.info("사용자 인증 체크4");
        }

        return new User(sUser.getUsername(), sUser.getPassword(), authorities);
    }

}
