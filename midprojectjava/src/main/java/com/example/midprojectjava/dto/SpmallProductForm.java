package com.example.midprojectjava.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpmallProductForm {
    private String productName;
    private String description;
    private Long price;
    private Integer quantity;
    private String imageUrl;
    private String category;

}
