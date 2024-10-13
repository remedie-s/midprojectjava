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

	        Cookie[] list = request.getCookies();
	        String accessToken = "";
	        String refreshToken = "";
	        for (Cookie cookie : list) {
	            if (cookie.getName().equals("access_token")) {
	                accessToken = cookie.getValue();
	            } else if (cookie.getName().equals("refreshToken")) {
	                refreshToken = cookie.getValue();
	            }
	        }

	        if (tokenProvider.isValidToken (accessToken)) {
	            System.out.println("유효한 토큰");
	            filterChain.doFilter(request, response);
	        } else {
	            System.out.println("유효하지 않은 토큰");
	            if (!tokenProvider.isValidToken (refreshToken)) {
	                System.out.println("리프레시토큰 문제 발생");
	                response.sendRedirect("/spmallUser/signup");
	                return;
	            }

	            if (!tokenProvider.isValidToken (refreshToken)) {
	                System.out.println("리프레시 토큰이 디비에 등록 X");
	                response.sendRedirect("/spmallUser/signup");
	                return;
	            }

	            response.sendRedirect("/spmallUser/reissue/" + refreshToken);
	        }

	    }
}
