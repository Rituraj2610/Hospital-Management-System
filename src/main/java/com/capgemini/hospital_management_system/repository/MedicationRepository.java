package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {
}
