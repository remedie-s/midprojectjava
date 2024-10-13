package com.example.midprojectjava.controller;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.midprojectjava.config.LoginResponse;
import com.example.midprojectjava.dto.SpmallUserForm;
import com.example.midprojectjava.dto.SpmallUserLoginForm;
import com.example.midprojectjava.entity.SpmallUser;
import com.example.midprojectjava.service.SpmallUserService;
import com.example.midprojectjava.service.TokenProvider;
import com.example.midprojectjava.service.TokenService;
import com.example.midprojectjava.service.UtilService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/spmallUser")
public class SpmallUserController {
    private final SpmallUserService sUserService;
    private final TokenProvider tokenProvider;
    private final TokenService tokenService;
    private final UtilService utilService;
    @Autowired
    private AuthenticationManager authenticationManager;
    
    
    // 기존의 회원가입 화면으로 가는 메서드
    @GetMapping("/signup")
    public String signupForm() {
        return "signup_form";
    }

    // REST API: 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SpmallUserForm spmallUserForm, HttpServletResponse response) {
    	System.out.println("회원가입 시작");
    	System.out.println(spmallUserForm.getEmail());
        if (!spmallUserForm.getPassword1().equals(spmallUserForm.getPassword2())) {
            return ResponseEntity.badRequest().body("비밀번호가 서로 다릅니다.");
        }

        SpmallUser spmallUser;
        try {
            spmallUser = this.sUserService.create(spmallUserForm.getUsername(), spmallUserForm.getPassword1(),
                spmallUserForm.getFirstName(), spmallUserForm.getLastName(), 
                spmallUserForm.getPhoneNumber(), spmallUserForm.getEmail());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("이미 사용 중인 회원정보입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("회원가입 오류입니다.");
        }

        String accessToken = this.tokenProvider.generateToken(spmallUser, Duration.ofDays(7));
        String refreshToken = this.tokenProvider.generateToken(spmallUser, Duration.ofDays(30));
        this.tokenService.saveRefreshToken(spmallUser.getId(), refreshToken);
        utilService.setCookie("access_token", accessToken, utilService.toSecondOfDay(7), response);
        utilService.setCookie("refresh_token", refreshToken, utilService.toSecondOfDay(30), response);

        return ResponseEntity.ok("회원가입 성공");
    }
    
    // REST API: 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SpmallUserLoginForm spmallUserLoginForm, HttpServletResponse response) {
    	 String username = spmallUserLoginForm.getUsername();
    	    String password = spmallUserLoginForm.getPassword();
    	    log.info(username+password);
    	    // 인증 처리 (Spring Security의 AuthenticationManager를 사용)
    	    Authentication authentication = authenticationManager.authenticate(
    	        new UsernamePasswordAuthenticationToken(username, password)
    	    );

    	    // 사용자 정보를 담은 응답 객체
    	    Map<String, String> responseBody = new HashMap<>();
    	    log.info(this.sUserService.findByUsername(username).getId().toString());
    	    responseBody.put("id", this.sUserService.findByUsername(username).getId().toString());
    	    responseBody.put("username", username);

    	    // 토큰 발급
//    	    log.info("토큰 발급");
//    	    String token = tokenProvider.createToken(authentication); // 토큰 발급 에러뜸
//    	    log.info("토큰 발급완료");
    	    // 토큰을 클라이언트에게 반환
    	    return ResponseEntity.ok(responseBody);
//    	    return ResponseEntity.ok(new LoginResponse(token)); 
    }

    // 추가: 유저 정보 조회 (예시)
    @GetMapping("/{id}")
    public ResponseEntity<SpmallUser> getUser(@PathVariable("id") Integer id) {
        SpmallUser user = sUserService.findById(id);
        return ResponseEntity.ok(user);
    }

    // 추가: 리프레시 토큰 처리
    @GetMapping("/reissue/{rToken}")
    public ResponseEntity<?> reissue(@PathVariable("rToken") String rToken, HttpServletResponse response) {
        String accessToken = tokenService.createNewAccessToken(rToken, 24 * 7);
        utilService.setCookie("access_token", accessToken, utilService.toSecondOfDay(7), response);
        return ResponseEntity.ok("Access token renewed");
    }
}
