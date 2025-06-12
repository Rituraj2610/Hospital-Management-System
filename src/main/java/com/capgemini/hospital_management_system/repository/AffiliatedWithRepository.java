package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.AffiliatedWith;
import com.capgemini.hospital_management_system.model.AffiliatedWithId;
import com.capgemini.hospital_management_system.model.Physician;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Repository
public interface AffiliatedWithRepository extends JpaRepository<AffiliatedWith, AffiliatedWithId> {


        List<AffiliatedWith> findByDepartment_DepartmentId(Integer deptId);

        List<AffiliatedWith> findByPhysician_EmployeeId(Long physicianId);

        boolean existsByPhysicianEmployeeIdAndPrimaryAffiliationTrue(Integer physicianId);

        Optional<AffiliatedWith> findByPhysician_EmployeeId(Integer physicianId);

        Optional<AffiliatedWith> findByPhysicianEmployeeIdAndDepartmentDepartmentId(Integer physicianId, Integer departmentId);

        @Query("SELECT a FROM AffiliatedWith a WHERE " +
                "(:physicianName IS NULL OR a.physician.name LIKE %:physicianName%) AND " +
                "(:departmentName IS NULL OR a.department.name LIKE %:departmentName%)")
        Page<AffiliatedWith> findAllWithFilters(
                @Param("physicianName") String physicianName,
                @Param("departmentName") String departmentName,
                Pageable pageable);

}
