/**
 * 쇼핑몰 보안 서비스입니다 로그인 관리를 합니다.
 */
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
public class SecurityService implements UserDetailsService {

    @Autowired
    private SpmallUserRepository spmallUserRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("사용자 인증 체크 [" + username + "]");

        // 사용자 조회
        Optional<SpmallUser> _user = this.spmallUserRepository.findByUsername(username);
        log.info("사용자 인증 체크 - 사용자 존재 여부: " + _user.isPresent());

        if (_user.isEmpty()) {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
        }

        SpmallUser spmallUser = _user.get();
        log.info("사용자 인증 체크 - 사용자 정보: " + spmallUser);
        log.info("사용자 인증 체크 - 비밀번호: " + spmallUser.getPassword()); // 비밀번호 출력 (보안상 주의)
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // 권한 설정
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(SpmallUserGrade.ADMIN.getValue()));
        } else if ("seller".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(SpmallUserGrade.SELLER.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(SpmallUserGrade.BRONZE.getValue()));
        }
        log.info("권한 부여 완료");

        return new User(spmallUser.getUsername(), spmallUser.getPassword(), authorities);
    }


}
