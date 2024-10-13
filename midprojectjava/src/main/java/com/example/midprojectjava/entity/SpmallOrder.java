/**
 * 쇼핑몰 주문 엔티티입니다
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
public class SpmallOrder {
	
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    private Integer status;
	    private Integer request;
	    private Integer quantity;
	    private LocalDateTime createDate;
	    public SpmallOrder(Integer status, Integer request, Integer quantity, LocalDateTime createDate) {
			super();
			this.status = status;
			this.request = request;
			this.quantity = quantity;
			this.createDate = createDate;
		}
		@ManyToOne
	    private SpmallUser spmallUser;
	    @ManyToOne
	    private SpmallProduct spmallProduct;

}
