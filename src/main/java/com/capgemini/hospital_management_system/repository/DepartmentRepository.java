package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    List<Department> findAllByHeadEmployeeId(Integer headId);
    boolean existsByHeadEmployeeId(Integer headId);


}
