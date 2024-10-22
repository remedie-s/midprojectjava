package com.example.midprojectjava.controller;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.midprojectjava.config.LoginResponse;
import com.example.midprojectjava.config.SpmallUserGrade;
import com.example.midprojectjava.dto.SpmallUserForm;
import com.example.midprojectjava.dto.SpmallUserGradeModifyForm;
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
//    @GetMapping("/signup")
//    public String signupForm() {
//        return "signup_form";
//    }

    // REST API: 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SpmallUserForm spmallUserForm, HttpServletResponse response) {
    	System.out.println("회원가입 시작");
//    	System.out.println(spmallUserForm.getEmail());
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
        // 응답 객체 생성
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("id", spmallUser.getId());
        responseBody.put("username", spmallUser.getUsername());
//        responseBody.put("accessToken", accessToken);
//        responseBody.put("refreshToken", refreshToken);

        // 회원가입 성공 및 토큰 반환
        return ResponseEntity.ok(responseBody);
    }
    
    // REST API: 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SpmallUserLoginForm spmallUserLoginForm, HttpServletResponse response) {
        String username = spmallUserLoginForm.getUsername();
        String password = spmallUserLoginForm.getPassword();
        log.info("로그인 요청: " + username);

        // 인증 처리 (Spring Security의 AuthenticationManager 사용)
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );

        // 사용자 정보를 가져옴
        SpmallUser user = sUserService.findByUsername(username);
        log.info("사용자 ID: " + user.getId());

        // 토큰 발급
        String accessToken = tokenProvider.createToken(authentication); // 액세스 토큰
        String refreshToken = tokenProvider.createRefreshToken(user); // 리프레시 토큰

        // 응답 객체 생성
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("id", user.getId());
        responseBody.put("username", username);
        responseBody.put("email", user.getEmail());
        responseBody.put("accessToken", accessToken);
        responseBody.put("refreshToken", refreshToken);

        // 토큰을 클라이언트에게 반환
        return ResponseEntity.ok(responseBody);
    }
    
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // 세션 무효화
    	log.info("로그아웃 요청이 들어왔습니다");
        request.getSession().invalidate();

        // 기타 로그아웃 관련 처리
        return ResponseEntity.ok("로그아웃 성공");
    }

//    // 추가: 유저 정보 조회 (예시)
//    @GetMapping("/{id}")
//    public ResponseEntity<SpmallUser> getUser(@PathVariable("id") Integer id) {
//        SpmallUser user = sUserService.findById(id);
//        return ResponseEntity.ok(user);
//    }

    // 추가: 리프레시 토큰 처리
    @GetMapping("/reissue/{rToken}")
    public ResponseEntity<?> reissue(@PathVariable("rToken") String rToken, HttpServletResponse response) {
        String accessToken = tokenService.createNewAccessToken(rToken, 24 * 7);
        utilService.setCookie("access_token", accessToken, utilService.toSecondOfDay(7), response);
        return ResponseEntity.ok("Access token renewed");
    }
//  @GetMapping("/signup")
//  public String login(HttpServletRequest httpServletRequest) {
//      httpServletRequest.getCookies();
//      return "login_form";
//  }
    
    @GetMapping("/userList")
    public ResponseEntity<List<SpmallUser>> userList(@AuthenticationPrincipal SpmallUser spmallUser) {
    	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	    log.info("Current authentication: {}", authentication);
    	  if (spmallUser == null) {
    	        log.error("AuthenticationPrincipal is null");
    	    } else {
    	        log.info("Authenticated user: {}", spmallUser.getUsername());}
//    	log.info(spmallUser.getUsername());
//        if (!spmallUser.getUserGrade().equals(0)) {
//            log.info("당신의 권한은 {}입니다. 권한이 부족합니다.", SpmallUserGrade.getRoleByGrade(spmallUser.getUserGrade()));
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // 본문에 null 반환
//        }
        log.info("유저정보 조회요청이 들어왔습니다.");
        List<SpmallUser> all = this.sUserService.findAll();
        return ResponseEntity.ok(all);
    }

    @PostMapping("/modifyGrade/{id}")
    public ResponseEntity<List<SpmallUser>> ModifyAuthByAdmin(@PathVariable("id") Integer id,
    		@AuthenticationPrincipal SpmallUser spmallUser,
                                                               @RequestBody SpmallUserGradeModifyForm modifyForm) {
//        if (!spmallUser.getUserGrade().equals(0)) {
//            log.info("당신의 권한은 {}입니다. 권한이 부족합니다.", SpmallUserGrade.getRoleByGrade(spmallUser.getUserGrade()));
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // 본문에 null 반환
//        }

        SpmallUser user = this.sUserService.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 유저를 찾지 못했을 경우
        }

        user.setUserGrade(modifyForm.getUserGrade());
        this.sUserService.save(user);
        log.info("{}님에 대한 유저 정보 변경이 완료되었습니다.", user.getUsername());

        List<SpmallUser> all = this.sUserService.findAll();
        return ResponseEntity.ok(all); // 전체 유저 리스트 반환
    }

    
}
