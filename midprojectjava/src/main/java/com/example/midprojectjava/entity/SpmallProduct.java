package com.example.midprojectjava.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class SpmallProduct {
	
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	    private String productName;
	    private String description;
	    private Long price;
	    private Integer quantity;
	    private String imageUrl;
	    private LocalDateTime createDate;
	    private Integer category;
	    private Integer sellCount=0;
	    
	    
	    public SpmallProduct(String productName, String description, Long price, Integer quantity, String imageUrl,
				LocalDateTime createDate, Integer category, Integer sellCount) {
			super();
			this.productName = productName;
			this.description = description;
			this.price = price;
			this.quantity = quantity;
			this.imageUrl = imageUrl;
			this.createDate = createDate;
			this.category = category;
			this.sellCount = sellCount=0;
		}
		@JsonIgnore
	    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
	    private List<SpmallProReview> reviewList;
	    @JsonIgnore
	    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
	    private List<SpmallProUser> userList;
	    @JsonIgnore
	    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
	    private List<SpmallOrder> orderList;
}
