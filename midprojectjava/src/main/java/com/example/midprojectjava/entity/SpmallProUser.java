/**
 * 쇼핑몰 물품의 구매 유저 관리 엔티티입니다
 */
package com.example.midprojectjava.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class SpmallProUser {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private SpmallUser spmallUser;

    @ManyToOne
    private SpmallProduct spmallProduct;

    private LocalDateTime createDate;
}
