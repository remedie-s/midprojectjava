package com.example.midprojectjava.dto;

import com.example.midprojectjava.entity.SpmallProduct;
import com.example.midprojectjava.entity.SpmallUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpmallProductReviewForm {
	
	
	private String content;
	private float rating;
	private Integer userId;
	private Integer productId;

}
