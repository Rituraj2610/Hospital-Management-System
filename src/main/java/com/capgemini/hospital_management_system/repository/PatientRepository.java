package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.Patient;
import com.capgemini.hospital_management_system.projection.PhysicianPatientCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Optional<List<Patient>> findByPCP_employeeId(int pcp_id);

    Optional<Patient> findByPCP_EmployeeIdAndSsn(int pcp_id, int ssn);

    @Query("SELECT p.PCP.name AS physicianName, COUNT(p) AS patientCount " +
            "FROM Patient p GROUP BY p.PCP.name")
    List<Object[]> countPatientsByPhysician();
}
