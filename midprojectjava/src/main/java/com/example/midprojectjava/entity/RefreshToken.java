package com.example.midprojectjava.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "suserid", updatable = false)
	    private Integer suserid;

	    @Column(nullable = false)
	    private String refreshToken;

	    private LocalDateTime createDate;

	    public RefreshToken(Integer suserid, String refreshToken) {
	        super();
	        this.suserid = suserid;
	        this.refreshToken = refreshToken;
	        this.createDate = LocalDateTime.now();
	    }

	    public RefreshToken update(String newRefreshToken) {
	        this.refreshToken = newRefreshToken;
	        return this;
	    }
}
