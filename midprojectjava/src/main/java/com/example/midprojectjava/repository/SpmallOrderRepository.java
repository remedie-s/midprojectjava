package com.example.midprojectjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.midprojectjava.entity.SpmallOrder;

public interface SpmallOrderRepository extends JpaRepository<SpmallOrder, Integer> {

}
