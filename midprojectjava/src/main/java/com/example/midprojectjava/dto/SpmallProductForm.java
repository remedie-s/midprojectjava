package com.example.midprojectjava.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpmallProductForm {
	@NotEmpty
    private String productName;
	@NotEmpty
    private String description;
    private Long price;
    private Integer quantity;
    @NotEmpty
    private String imageUrl;
    @NotEmpty
    private String category;

}
