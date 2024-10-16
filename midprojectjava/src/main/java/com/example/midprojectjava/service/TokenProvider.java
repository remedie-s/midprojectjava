/**
 * 쇼핑몰 이용자 토큰공급자 서비스입니다
 */
package com.example.midprojectjava.service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.example.midprojectjava.config.JwtProperties;
import com.example.midprojectjava.entity.RefreshToken;
import com.example.midprojectjava.entity.SpmallUser;
import com.example.midprojectjava.repository.RefreshTokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    @Lazy
    private final SecurityService securityService; // UserService 대신 SecurityService 주입
    public String generateToken(SpmallUser user, Duration expriedAt) {
        Date now = new Date();
        return createToken(new Date(now.getTime() + expriedAt.toMillis()), user);
    }

    private String createToken(Date expriry, SpmallUser user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expriry)
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();

    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            e.printStackTrace();
            return false;
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
            return false;
        } catch (MalformedJwtException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        SpmallUser user = this.securityService.findUserByUsername(claims.getSubject()); // SecurityService 사용
        return new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
    }

    // 리프레시 토큰 유효성
    public boolean isValidRefreshToken(String refreshToken) {
        Optional<RefreshToken> opt = this.refreshTokenRepository.findByRefreshToken(refreshToken);
        if (opt.isPresent()) {
            return true;
        }
        return false;
    }

    public String createToken(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        log.info("Authentication principal class: " + principal.getClass().getName());

        if (principal instanceof SpmallUser) {
            SpmallUser user = (SpmallUser) principal;
            
            // Set expiration time (e.g., 1 hour)
            Duration expirationTime = Duration.ofHours(1);
            
            return generateToken(user, expirationTime);
        } else {
            throw new ClassCastException("Authentication principal is not an instance of SpmallUser");
        }
    }
    public String createRefreshToken(SpmallUser user) {
        String refreshToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + Duration.ofDays(30).toMillis())) // 예: 30일
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();

        // 리프레시 토큰을 DB에 저장하는 로직 추가
        refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken)); // 필요시

        return refreshToken;
    }


}
