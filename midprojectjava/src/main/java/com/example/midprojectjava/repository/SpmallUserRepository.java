package com.example.midprojectjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.midprojectjava.entity.SpmallUser;

public interface SpmallUserRepository extends JpaRepository<SpmallUser, Integer> {

}
