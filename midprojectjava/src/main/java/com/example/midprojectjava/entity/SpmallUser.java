/**
 * 쇼핑몰 유저관리 엔티티입니다
 */
package com.example.midprojectjava.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.example.midprojectjava.config.SpmallUserGrade;
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
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class SpmallUser implements UserDetails 
	{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String phoneNumber;

    @Column(name = "email", unique = true)
    private String email;

    private LocalDateTime createDate;

    // 유저 그레이드
    private Integer userGrade = 0;

    // 필요한 경우 생성자 추가
    public SpmallUser(String username, String password, String firstName, String lastName, String phoneNumber,
                      String email, LocalDateTime createDate, Integer userGrade) {
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
    
    public static String getRoleByGrade(Integer grade) {
        switch (grade) {
            case 0: // 예: 브론즈
                return SpmallUserGrade.BRONZE.getValue();
            case 1: // 예: 실버
                return SpmallUserGrade.SILVER.getValue();
            case 2: // 예: 골드
                return SpmallUserGrade.GOLD.getValue();
            case 3: // 예: 관리자
                return SpmallUserGrade.ADMIN.getValue();
            case 4: // 예: 판매자
                return SpmallUserGrade.SELLER.getValue();
            default:
                return SpmallUserGrade.BRONZE.getValue(); // 기본값
        }
    }
    // UserDetails 인터페이스 구현
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(SpmallUserGrade.getRoleByGrade(this.userGrade)));
        return authorities;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부
    }
}

