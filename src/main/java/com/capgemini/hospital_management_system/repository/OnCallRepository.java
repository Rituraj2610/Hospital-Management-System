package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.OnCallId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnCallRepository extends JpaRepository<OnCallRepository, OnCallId> {
}
