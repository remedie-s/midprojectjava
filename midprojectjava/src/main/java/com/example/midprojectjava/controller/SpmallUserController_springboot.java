package com.example.midprojectjava.controller;
//
//import java.time.Duration;
//
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.example.midprojectjava.config.JwtProperties;
//import com.example.midprojectjava.dto.SpmallUserForm;
//import com.example.midprojectjava.entity.SpmallUser;
//import com.example.midprojectjava.service.SpmallUserService;
//import com.example.midprojectjava.service.TokenProvider;
//import com.example.midprojectjava.service.TokenService;
//import com.example.midprojectjava.service.UtilService;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
////
//@Slf4j
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/spmallUser")
public class SpmallUserController_springboot {
//	private final SpmallUserService sUserService;
//    private final TokenProvider tokenProvider;
//    private final TokenService tokenService;
//    private final JwtProperties jwtProperties;
//    private final UtilService utilService;
//    
//
//    @GetMapping("/{id}")
//    public String usermain(@PathVariable("id") Integer id, Model model) {
//        this.sUserService.findById(id);
//        return "user_main";
//    }
//
//    @GetMapping("/signup")
//    public String signup(Model model) {
//        model.addAttribute("spmallUserForm", new SpmallUserForm());
//        return "signup_form";
//    }
//
//    @PostMapping("/signup")
//    public String signup(@Valid SpmallUserForm spmallUserForm, BindingResult bindingResult, HttpServletResponse response) {
//        System.out.println("가입요청");
//        if (bindingResult.hasErrors()) {
//            System.out.println("에러발생" + bindingResult.getAllErrors().size());
//            for (ObjectError err : bindingResult.getAllErrors()) {
//                log.info(err.getDefaultMessage());
//            }
//            return "signup_form";
//        }
//        System.out.println("비번체크");
//        if (!spmallUserForm.getPassword1().equals(spmallUserForm.getPassword2())) {
//            bindingResult.rejectValue("password", "passwordInCorrect", "비밀번호가 서로다릅니다.");
//            System.out.println("비밀번호가 다릅니다.");
//            return "signup_form";
//        }
//        SpmallUser spmallUser;
//        try {
//            System.out.println("회원가입 진행");
//            spmallUser = this.sUserService.create(spmallUserForm.getUsername(), spmallUserForm.getPassword1(),
//            		spmallUserForm.getFirstName(),
//            		spmallUserForm.getLastName(), spmallUserForm.getPhoneNumber(),
//            		spmallUserForm.getEMail());
//            System.out.println("회원가입 완료");
//        } catch (DataIntegrityViolationException e) {
//            bindingResult.reject("signupError", "이미 사용중인 회원정보입니다.");
//            return "signup_form";
//        } catch (Exception e) {
//            bindingResult.reject("signupError", "회원가입 오류입니다.");
//            return "signup_form";
//        }
//        String accessToken = this.tokenProvider.generateToken(spmallUser, Duration.ofDays(7));
//        String refreshToken = this.tokenProvider.generateToken(spmallUser, Duration.ofDays(30));
//        System.out.println(spmallUser.getId());
//        this.tokenService.saveRefreshToken(spmallUser.getId(), refreshToken);
//        utilService.setCookie("access_token", accessToken, utilService.toSecondOfDay(7), response);
//        utilService.setCookie("refresh_token", refreshToken, utilService.toSecondOfDay(30), response);
//
//        return "redirect:/spmallUser/login";
//    }
//
//    @GetMapping("/login")
//    public String login(HttpServletRequest httpServletRequest) {
//        httpServletRequest.getCookies();
//        return "login_form";
//    }
//
//    @GetMapping("/reissue/{rToken}")
//    public String reissue(@PathVariable("rToken") String rToken, HttpServletResponse httpServletResponse) {
//        String accessToken = tokenService.createNewAccessToken(rToken, 24 * 7);
//        utilService.setCookie("access_token", accessToken, utilService.toSecondOfDay(7), httpServletResponse);
//        return "redirect:/";
//    }
}
