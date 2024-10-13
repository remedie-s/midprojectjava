package com.example.midprojectjava.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.midprojectjava.entity.SpmallCart;

public interface SpmallCartRepository extends JpaRepository<SpmallCart, Integer> {

	
		@Query("SELECT c FROM SpmallCart c WHERE c.spmallUser.id = :spmallUser_Id")
	    List<SpmallCart> findBySpmallUser_Id(@Param("spmallUser_Id") Integer spmallUser_Id);

	    @Query("SELECT c FROM SpmallCart c WHERE c.spmallUser.id = :spmallUser_Id")
	    Page<SpmallCart> findBySpmallUser_Id(@Param("spmallUser_Id") Integer spmallUser_Id, Pageable pageable);

}
