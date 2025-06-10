package com.capgemini.hospital_management_system.repository;


import com.capgemini.hospital_management_system.model.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhysicianRepository extends JpaRepository<Physician, Integer> {

	Optional<Physician> findByName(String name);

	List<Physician> findAllByPosition(String position);

	@Query("SELECT p.position, p.name FROM Physician p")
	List<Object[]> findPositionAndPhysicianNames();

  Optional<Physician> findByEmployeeId(Integer employeeId);


}
