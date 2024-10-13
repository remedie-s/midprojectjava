package com.example.midprojectjava.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpmallUserForm {
    @Size(min = 3, max = 30)
    @NotEmpty(message = "아이디를 입력하세요")
    private String username;
    private String password1;
    private String password2;
    @NotEmpty(message = "이름을 입력하세요")
    private String firstName;
    @NotEmpty(message = "성을 입력하세요")
    private String lastName;
    private String phoneNumber;
    @Email
    @NotEmpty(message = "이메일을 입력하세요")
    private String email;


}
