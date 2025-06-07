package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.dto.PatientAppointmentDTO;
import com.capgemini.hospital_management_system.model.Appointment;
import com.capgemini.hospital_management_system.model.Patient;
import com.capgemini.hospital_management_system.model.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByStart(LocalDateTime startdate);

    @Query("SELECT a.patient FROM Appointment a WHERE a.appointmentId = :appointmentid")
    Optional<Patient> findPatientByAppointmentId(@Param("appointmentid") Integer appointmentid);

    @Query("SELECT a.physician FROM Appointment a WHERE a.appointmentId = :appointmentid")
    Optional<Physician> findPhysicianByAppointmentId(@Param("appointmentid") Integer appointmentid);
}