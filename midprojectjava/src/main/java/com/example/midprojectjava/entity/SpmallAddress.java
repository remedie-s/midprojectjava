/**
 * 쇼핑몰 주소 엔티티입니다
 */
package com.example.midprojectjava.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class SpmallAddress {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	    private String streetName;
	    private Integer buildingNumber;
	    private String detailAddress;
	    private String city;
	    @ManyToOne
	    private SpmallUser spmallUser;
}
