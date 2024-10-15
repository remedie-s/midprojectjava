package com.example.midprojectjava.config;

//TODO 리다이렉트 위치 바꿔야함
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.midprojectjava.service.TokenProvider;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter{
	 private final TokenProvider tokenProvider;

	    private final static String ACCESS_TOKEN_PREFIX = "Bearer";
	    private final static String HEADER_AUTH = "Authentication";
	    
	    //추가
	    
	    @Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	            throws ServletException, IOException, java.io.IOException {

	        String url = request.getRequestURI();
	        if (url.startsWith("/spmallOrder") || url.startsWith("/spmallUser") || url.startsWith("/homepage")
	                || url.startsWith("/spmallProduct") || url.startsWith("/dist")
	                || url.startsWith("/plugins") || url.startsWith("/favicon.ico")) {
	            filterChain.doFilter(request, response);
	            return;
	        }

	        String accessToken = extractTokenFromHeader(request);
	        String refreshToken = extractRefreshTokenFromCookies(request);

	        // 토큰이 비어있는지 확인
	        if (accessToken == null || accessToken.isEmpty()) {
	            System.out.println("액세스 토큰이 없습니다.");
	            handleTokenAbsence(response, refreshToken);
	            return;
	        }

	        // 액세스 토큰 검증
	        if (tokenProvider.isValidToken(accessToken)) {
	            System.out.println("유효한 액세스 토큰");
	            filterChain.doFilter(request, response);
	        } else {
	            System.out.println("유효하지 않은 액세스 토큰");
	            handleTokenAbsence(response, refreshToken);
	        }
	    }

	    private String extractTokenFromHeader(HttpServletRequest request) {
	        String authHeader = request.getHeader("Authorization");
	        if (authHeader != null && authHeader.startsWith(ACCESS_TOKEN_PREFIX + " ")) {
	            return authHeader.substring(ACCESS_TOKEN_PREFIX.length() + 1);
	        }
	        return null;
	    }

	    private String extractRefreshTokenFromCookies(HttpServletRequest request) {
	        Cookie[] cookies = request.getCookies();
	        if (cookies != null) {
	            for (Cookie cookie : cookies) {
	                if (cookie.getName().equals("refreshToken")) {
	                    return cookie.getValue();
	                }
	            }
	        }
	        return null;
	    }


	    private void handleTokenAbsence(HttpServletResponse response, String refreshToken) throws IOException, java.io.IOException {
	        if (refreshToken == null || refreshToken.isEmpty()) {
	            System.out.println("리프레시 토큰 문제 발생");
	            response.sendRedirect("/spmallUser/signup");
	        } else if (!tokenProvider.isValidToken(refreshToken)) {
	            System.out.println("리프레시 토큰이 디비에 등록 X");
	            response.sendRedirect("/spmallUser/signup");
	        } else {
	            response.sendRedirect("/spmallUser/reissue/" + refreshToken);
	        }
	    }


}

