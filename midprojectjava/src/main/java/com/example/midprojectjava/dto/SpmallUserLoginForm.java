package com.example.midprojectjava.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpmallUserLoginForm {
	  @Size(min = 3, max = 30)
	    @NotEmpty(message = "아이디를 입력하세요")
	    private String username;
	  private String password;
}
