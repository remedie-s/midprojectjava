package com.example.midprojectjava.dto;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartToOrderForm {
	
	private Integer userId;
	private List<Integer> productIdList;
	

}
