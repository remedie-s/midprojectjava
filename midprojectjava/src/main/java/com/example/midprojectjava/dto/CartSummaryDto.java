package com.example.midprojectjava.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartSummaryDto {
	private Long totalCostSum;
	private Integer totalQuantitySum;
	private Integer userId;

}
