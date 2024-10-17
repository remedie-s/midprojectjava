/**
 * 쇼핑몰 보안 서비스입니다 로그인 관리를 합니다.
 */
package com.example.midprojectjava.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.midprojectjava.entity.SpmallUser;
import com.example.midprojectjava.repository.SpmallUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {

    private final SpmallUserRepository spmallUserRepository;

    @Override
    @Transactional(readOnly = true)
    public SpmallUser loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("사용자 인증 체크 [{}]", username);

        Optional<SpmallUser> _user = this.spmallUserRepository.findByUsername(username);
        log.info("사용자 인증 체크 - 사용자 존재 여부: {}", _user.isPresent());

        if (_user.isEmpty()) {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
        }

        SpmallUser spmallUser = _user.get();
        log.info("사용자 인증 체크 - 사용자 정보: {}", spmallUser);

        return spmallUser; // SpmallUser 객체 반환
    }
    public SpmallUser findUserByUsername(String username) {
        Optional<SpmallUser> userOpt = this.spmallUserRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }
        throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
    }
}
