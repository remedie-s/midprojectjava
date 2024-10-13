package com.example.midprojectjava.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.midprojectjava.entity.SpmallOrder;

public interface SpmallOrderRepository extends JpaRepository<SpmallOrder, Integer> {

	
	@Query("SELECT o FROM SpmallOrder o WHERE o.spmallUser.id = :spmallUser_Id")
    List<SpmallOrder> findBySpmallUser_Id(@Param("spmallUser_Id") Integer spmallUser_Id);

    @Query("SELECT o FROM SpmallOrder o WHERE o.spmallUser.id = :spmallUser_Id")
    Page<SpmallOrder> findBySpmallUser_Id(@Param("spmallUser_Id") Integer spmallUser_Id, Pageable pageable);

}
