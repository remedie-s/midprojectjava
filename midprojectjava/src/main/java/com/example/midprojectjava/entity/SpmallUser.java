/**
 * 쇼핑몰 유저관리 엔티티입니다
 */
package com.example.midprojectjava.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
public class SpmallUser {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String phoneNumber;
    @Column(name = "email", unique = true)
    private String email;
    private LocalDateTime createDate;
//    유저 그레이드를 0, 1, 2, 10, 11까지 관리 0 : 브론즈, 1 : 실버, 2 : 골드, 10 : 셀러, 11 : 관리자   
    private Integer userGrade = 0;

    

    public SpmallUser(String username, String password, String firstName, String lastName, String phoneNumber,
			String email, LocalDateTime createDate, Integer userGrade) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.createDate = createDate;
		this.userGrade = userGrade;
	}
	@JsonIgnore
    @OneToMany(mappedBy = "spmallUser", cascade = CascadeType.REMOVE)
    private List<SpmallCart> cartList;
    @JsonIgnore
    @OneToMany(mappedBy = "spmallUser", cascade = CascadeType.REMOVE)
    private List<SpmallAddress> addressList;
    @JsonIgnore
    @OneToMany(mappedBy = "spmallUser", cascade = CascadeType.REMOVE)
    private List<SpmallOrder> orderList;
    @JsonIgnore
    @OneToMany(mappedBy = "spmallUser", cascade = CascadeType.REMOVE)
    private List<SpmallProReview> reviewList;
    	
}
