package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.AffiliatedWith;
import com.capgemini.hospital_management_system.model.AffiliatedWithId;
import com.capgemini.hospital_management_system.model.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface AffiliatedWithRepository extends JpaRepository<AffiliatedWith, AffiliatedWithId> {


        List<AffiliatedWith> findByDepartment_DepartmentId(Integer deptId);



}
