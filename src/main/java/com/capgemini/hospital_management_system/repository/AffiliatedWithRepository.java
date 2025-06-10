package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.AffiliatedWith;
import com.capgemini.hospital_management_system.model.AffiliatedWithId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AffiliatedWithRepository extends JpaRepository<AffiliatedWith, AffiliatedWithId> {

}
