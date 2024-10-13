package com.example.midprojectjava.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.midprojectjava.entity.SpmallProReview;

public interface SpmallProReviewRepository extends JpaRepository<SpmallProReview, Integer> {

	List<SpmallProReview> findBySpmallProduct_Id(Integer id);

}
