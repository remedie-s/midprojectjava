package com.example.midprojectjava.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.midprojectjava.entity.SpmallProduct;

public interface SpmallProductRepository extends JpaRepository<SpmallProduct, Integer> {
//	TODO 쿼리문 써야할수도있음
	List<SpmallProduct> findAllByCategory(String category);

}
