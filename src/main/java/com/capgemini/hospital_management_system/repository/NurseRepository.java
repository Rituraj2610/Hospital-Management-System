package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.Nurse;
import com.capgemini.hospital_management_system.projection.NursePositionCount;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Integer> {
	
	@Query("SELECT n.position AS position, COUNT(n) AS count FROM Nurse n GROUP BY n.position")
	List<NursePositionCount> countNursesByPosition();

}
