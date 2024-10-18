package com.example.midprojectjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.midprojectjava.entity.SpmallProUser;
import com.example.midprojectjava.entity.SpmallProduct;
import com.example.midprojectjava.entity.SpmallUser;

public interface SpmallProUserRepository extends JpaRepository<SpmallProUser, Integer> {
	
	@Query("SELECT o FROM SpmallProUser o WHERE o.spmallUser.id = :spmallUser_Id")
    SpmallProUser findBySpmallUser_Id(@Param("spmallUser_Id") Integer spmallUser_Id);


	SpmallProUser findBySpmallUserAndSpmallProduct(SpmallUser user, SpmallProduct product);
}
