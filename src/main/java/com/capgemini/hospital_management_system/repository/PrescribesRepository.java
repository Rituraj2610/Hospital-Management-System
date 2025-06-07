package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.Prescribes;
import com.capgemini.hospital_management_system.model.PrescribesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescribesRepository extends JpaRepository<Prescribes, PrescribesId> {
}