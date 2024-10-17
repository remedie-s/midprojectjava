/**
 * 쇼핑몰 카트 엔티티입니다
 */
package com.example.midprojectjava.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity

@NoArgsConstructor
public class SpmallCart {
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	    private LocalDateTime createDate;
	    private Integer quantity;

	    @ManyToOne
	    private SpmallUser spmallUser;

	    @ManyToOne
	    private SpmallProduct spmallProduct;
}
