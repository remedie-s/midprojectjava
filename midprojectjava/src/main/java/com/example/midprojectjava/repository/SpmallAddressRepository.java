package com.example.midprojectjava.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.midprojectjava.entity.SpmallAddress;
import com.example.midprojectjava.entity.SpmallUser;

public interface SpmallAddressRepository extends JpaRepository<SpmallAddress, Integer> {

	List<SpmallAddress> findAllBySpmallUser(SpmallUser spmallUser);

}
