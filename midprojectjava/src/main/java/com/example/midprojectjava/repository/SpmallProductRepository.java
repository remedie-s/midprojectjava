package com.example.midprojectjava.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.midprojectjava.entity.SpmallProduct;


public interface SpmallProductRepository extends JpaRepository<SpmallProduct, Integer> {
//	TODO 쿼리문 써야할수도있음
	List<SpmallProduct> findAllByCategory(String category);
//	TODO 쿼리문 써야할수도있음
//	@Query("SELECT p FROM SmallProduct p WHERE p.productName=?1")
	Optional<SpmallProduct> findByProductName( String productName);

}
