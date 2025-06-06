package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.TrainedIn;
import com.capgemini.hospital_management_system.model.TrainedInId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainedInRepository extends JpaRepository<TrainedIn, TrainedInId> {
}