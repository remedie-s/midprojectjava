package com.example.midprojectjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.midprojectjava.entity.SpmallUser;

@Service
public class AuthenticationService {
    
    private final TokenProvider tokenProvider;
    private final SpmallUserService spmallUserService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(TokenProvider tokenProvider, SpmallUserService spmallUserService, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.spmallUserService = spmallUserService;
        this.authenticationManager = authenticationManager;
    }

    public String login(String username, String password) {
        // Spring Security의 AuthenticationManager를 사용하여 인증 처리
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );

        // 인증이 성공하면 토큰 생성
        String token = tokenProvider.createToken(authentication);
        return token;
    }

    public Authentication getAuthentication(String token) {
        return tokenProvider.getAuthentication(token);
    }

    // 필요에 따라 추가적인 메서드를 작성
}