package com.example.midprojectjava.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderListDto {
	
	
	private Integer id;
	private Integer userId;
	private Integer productId;
	private String  productUrl;
	private Long productPrice;
    private Integer quantity;
    private Integer status;
    private Integer request;
    private LocalDateTime createDate;
	

}
