/**
 * 쇼핑몰 리프레시 토큰 엔티티 입니다
 */
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
	    @Column(name = "spmallUserId", updatable = false)
	    private Integer spmallUserId;

	    @Column(nullable = false)
	    private String refreshToken;

	    private LocalDateTime createDate;

	    public RefreshToken(Integer spmallUserId, String refreshToken) {
	        super();
	        this.spmallUserId = spmallUserId;
	        this.refreshToken = refreshToken;
	        this.createDate = LocalDateTime.now();
	    }

	    public RefreshToken update(String newRefreshToken) {
	        this.refreshToken = newRefreshToken;
	        return this;
	    }
}
