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
    @Column(unique = true)
    private String eMail;
    private LocalDateTime createDate;
    private Integer userGrade = 0;

    

    public SpmallUser(String username, String password, String firstName, String lastName, String phoneNumber,
			String eMail, LocalDateTime createDate, Integer userGrade) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.eMail = eMail;
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
