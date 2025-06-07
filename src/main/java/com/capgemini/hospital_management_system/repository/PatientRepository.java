package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
