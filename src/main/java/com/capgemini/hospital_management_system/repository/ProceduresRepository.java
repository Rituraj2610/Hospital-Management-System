package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProceduresRepository extends JpaRepository<Procedure, Integer> {

    List<Procedure> code(Integer code);
}