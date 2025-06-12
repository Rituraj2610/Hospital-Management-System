package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.Nurse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.capgemini.hospital_management_system.projection.NursePositionCount;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Integer> {

	
	Page<Nurse> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

	@Query("SELECT n.position AS position, COUNT(n) AS count FROM Nurse n GROUP BY n.position")
	List<NursePositionCount> countNursesByPosition();

}
