package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhysicianRepository extends JpaRepository<Physician, Integer> {
	Optional<Physician> findByName(String name);
<<<<<<< HEAD
	List<Physician> findAllByPosition(String position);

=======
    Optional<Physician> findByEmployeeId(Integer employeeId);
>>>>>>> 5540ef09807a6f6f62cb507007d191622d2611ce
}
