package com.example.midprojectjava.config;

import java.util.Arrays;

// TODO 쇼핑몰 http 보안설정 바꿔야함

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.midprojectjava.service.TokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig   {
    private final TokenProvider tokenProvider;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors() // CORS 활성화
            .and()
            .authorizeHttpRequests((a) -> a
//                .requestMatchers(new AntPathRequestMatcher("/spmallUser/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/spmallUser/login")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/spmallUser/signup")).permitAll() 
                .requestMatchers(new AntPathRequestMatcher("/homepage/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/spmallProduct/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/spmallOrder/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/dist/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/plugins/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/spmallCart/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
            .csrf().disable() // CSRF 비활성화
            .headers((c) -> c.addHeaderWriter(new XFrameOptionsHeaderWriter()))
            // .formLogin().disable() // 폼 로그인을 비활성화하고 JSON으로 로그인 처리
//            .logout((logout) -> logout
//                .logoutRequestMatcher(new AntPathRequestMatcher("/spmallUser/logout"))
//                .logoutSuccessUrl("/spmallUser/login")
//                .logoutSuccessUrl("http://localhost:3000/")  // 리다이렉트 URL 변경
//                .invalidateHttpSession(true))//cors 오류로 그냥 메소드 처리함
            .addFilterBefore(new TokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 클라이언트 도메인
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization"); // 만약 Authorization 헤더에 접근해야 하면 추가

        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 CORS 적용
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

