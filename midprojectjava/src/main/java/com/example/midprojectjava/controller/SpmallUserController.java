package com.example.midprojectjava.controller;

import java.time.Duration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.midprojectjava.dto.SpmallUserForm;
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
    public ResponseEntity<?> login(@RequestBody SpmallUserForm spmallUserForm, HttpServletResponse response) {
        // 로그인 처리 로직 구현
        // 성공 시 토큰 발급 및 쿠키 설정
        return ResponseEntity.ok("로그인 성공");
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
