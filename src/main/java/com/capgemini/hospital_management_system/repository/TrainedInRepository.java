package com.capgemini.hospital_management_system.repository;



import com.capgemini.hospital_management_system.model.TrainedIn;
import com.capgemini.hospital_management_system.model.TrainedInId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;

import java.util.List;

@Repository
public interface TrainedInRepository extends JpaRepository<TrainedIn, TrainedInId> {
	
    List<TrainedIn> findByPhysicianEmployeeId(Integer physicianId); 
    
    List<TrainedIn> findByTreatmentCode(Integer procedureId);
    
    List<TrainedIn> findByPhysicianEmployeeIdAndCertificationExpiresBetween(Integer physicianId, LocalDateTime start, LocalDateTime end);


    @Query("""
      SELECT ti
        FROM TrainedIn ti
        JOIN FETCH ti.physician p
        JOIN FETCH ti.treatment  t
       WHERE ti.certificationDate  >= :start
         AND ti.certificationExpires <= :end
    """)
    Page<TrainedIn> findByDates(
            @Param("start") LocalDateTime start,
            @Param("end")   LocalDateTime end,
            Pageable pageable
    );
}