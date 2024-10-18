/**
 * 카트 정보 전송을 위한 DTO입니다
 */
package com.example.midprojectjava.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartListDto {

    private Integer id;
    private String imageUrl;
    private String productName;
    private Long price;
    private Integer quantity;
	private Integer productId;
}