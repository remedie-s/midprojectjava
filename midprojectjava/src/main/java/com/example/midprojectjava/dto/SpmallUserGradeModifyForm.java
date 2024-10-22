package com.example.midprojectjava.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpmallUserGradeModifyForm {
	@Min(0)
	private Integer userId;
	@Min(0) @Max(4)
	private Integer userGrade;

}
