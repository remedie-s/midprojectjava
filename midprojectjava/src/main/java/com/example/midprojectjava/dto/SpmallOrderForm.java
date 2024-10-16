package com.example.midprojectjava.dto;

import java.time.LocalDateTime;

import com.example.midprojectjava.entity.SpmallUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpmallOrderForm {
	
	private Integer status;
    private Integer request;
    private Integer quantity;
    private LocalDateTime createDate;
    private Integer userId;
    private Integer productId;
}
