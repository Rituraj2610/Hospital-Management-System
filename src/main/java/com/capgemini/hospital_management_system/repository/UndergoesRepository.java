package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.Undergoes;
import com.capgemini.hospital_management_system.model.UndergoesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UndergoesRepository extends JpaRepository<Undergoes, UndergoesId> {
}