package com.example.midprojectjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.midprojectjava.entity.SpmallProduct;

public interface SpmallProductRepository extends JpaRepository<SpmallProduct, Integer> {

}
