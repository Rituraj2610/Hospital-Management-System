package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.Nurse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Integer> {

	
	Page<Nurse> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
