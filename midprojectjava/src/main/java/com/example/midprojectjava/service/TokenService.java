/**
 * 쇼핑몰 토큰 서비스입니다
 */
package com.example.midprojectjava.service;
import java.time.Duration;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.example.midprojectjava.entity.RefreshToken;
import com.example.midprojectjava.entity.SpmallUser;
import com.example.midprojectjava.exception.DataNotFoundException;
import com.example.midprojectjava.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;

    private final SpmallUserService spmallUserService;

    private final RefreshTokenRepository refreshTokenRepository;

    public String createNewAccessToken(String refreshToken, int hour) {
        if (!tokenProvider.isValidToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }
        if (!tokenProvider.isValidRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }
        //TODO 바꿔야함
        Integer suserId = findByRefreshToken(refreshToken).getSpmallUserId();
        SpmallUser spmallUser = spmallUserService.findById(suserId);
        return tokenProvider.generateToken(spmallUser, Duration.ofHours(hour));
    }

    public RefreshToken findByRefreshToken(String refreshToken) {
        Optional<RefreshToken> opt = this.refreshTokenRepository.findByRefreshToken(refreshToken);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new DataNotFoundException("user not found");
    }

    public RefreshToken findByUserId(Integer sUserid) {
        Optional<RefreshToken> opt = this.refreshTokenRepository.findById(sUserid);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new DataNotFoundException("user not found");
    }

    public void saveRefreshToken(Integer id, String token) {
        System.out.println("Saving refresh token: spmallUserId=" + id + ", token=" + token);
        RefreshToken refreshToken = new RefreshToken(id, token);
        this.refreshTokenRepository.save(refreshToken);
    }

}
