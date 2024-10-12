package com.example.midprojectjava.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.midprojectjava.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
