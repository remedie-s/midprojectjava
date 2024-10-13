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
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class SpmallUser //implements UserDetails 
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
//
//    // UserDetails 인터페이스 구현
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        // 유저 권한을 반환하는 로직 필요 (예: ROLE_USER)
//        return List.of(() -> "ROLE_USER"); // 여기에 실제 권한을 반환해야 합니다
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true; // 계정 만료 여부
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true; // 계정 잠금 여부
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true; // 자격 증명 만료 여부
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true; // 계정 활성화 여부
//    }
}

