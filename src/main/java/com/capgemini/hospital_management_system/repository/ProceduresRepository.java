package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.Procedure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProceduresRepository extends JpaRepository<Procedure, Integer> {

    List<Procedure> code(Integer code);

    @Query("SELECT p FROM Procedure p JOIN p.trainedPhysicians tp WHERE tp.employeeId = :physicianId")
    Page<Procedure> findProceduresByPhysicianId(@Param("physicianId") Integer physicianId, Pageable pageable);

}