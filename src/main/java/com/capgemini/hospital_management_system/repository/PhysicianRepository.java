package com.capgemini.hospital_management_system.repository;


import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.projection.ProcedureTrainingCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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



	@Query("SELECT t.name AS procedureName, COUNT(p) AS physicianCount " +
			"FROM Physician p JOIN p.trainedProcedures t " +
			"GROUP BY t.name")
	List<ProcedureTrainingCount> countTrainedProceduresByName();

	@Query("SELECT p FROM Physician p JOIN p.trainedProcedures tp WHERE tp.code = :procedureId")
	Page<Physician> findPhysiciansByProcedureId(@Param("procedureId") Integer procedureId, Pageable pageable);


}
