package com.example.midprojectjava.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.midprojectjava.entity.SpmallUser;

public interface SpmallUserRepository extends JpaRepository<SpmallUser, Integer> {

	Optional<SpmallUser> findByUsername(String username);

}
