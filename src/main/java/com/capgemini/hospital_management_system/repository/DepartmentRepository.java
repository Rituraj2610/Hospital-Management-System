package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
