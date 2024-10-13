/**
 * 쇼핑몰 물품의 리뷰 엔티티입니다
 */
package com.example.midprojectjava.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Entity
@NoArgsConstructor
public class SpmallProReview {
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
		
	    private String content;

	    private LocalDateTime createDate;

	    @ManyToOne
	    private SpmallProduct spmallProduct;

	    @ManyToOne
	    private SpmallUser spmallUser;
}
