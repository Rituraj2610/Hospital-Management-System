package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.dto.PatientAppointmentDTO;
import com.capgemini.hospital_management_system.model.Appointment;
import com.capgemini.hospital_management_system.model.Nurse;
import com.capgemini.hospital_management_system.model.Patient;
import com.capgemini.hospital_management_system.model.Physician;
import com.capgemini.hospital_management_system.projection.PhysicianAppointmentCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    Page<Appointment> findByStart(LocalDateTime startdate, Pageable pageable);

    @Query("SELECT a.patient FROM Appointment a WHERE a.appointmentId = :appointmentid")
    Optional<Patient> findPatientByAppointmentId(@Param("appointmentid") Integer appointmentid);

    @Query("SELECT a.physician FROM Appointment a WHERE a.appointmentId = :appointmentid")
    Optional<Physician> findPhysicianByAppointmentId(@Param("appointmentid") Integer appointmentid);

    @Query("SELECT a.prepNurse FROM Appointment a WHERE a.appointmentId = :appointmentid")
    Optional<Nurse> fetchNurseByAppointmentId(@Param("appointmentid") Integer appointmentid);

    @Query("SELECT a.examinationRoom FROM Appointment a WHERE a.appointmentId = :id")
    String findExaminationRoomByAppointmentId(@Param("id") Integer appointmentId);

    Page<Appointment> findByPatient_Ssn(Integer ssn, Pageable pageable);

    Optional<Appointment> findByStartAndPatient_Ssn(LocalDateTime start, Integer ssn);

    Page<Appointment> findByPhysician_employeeId(Integer employeeId, Pageable pageable);

    Page<Appointment> findByPhysician_employeeIdAndStart(Integer physicianEmployeeId, LocalDateTime start, Pageable pageable);

    Page<Appointment> findByPrepNurse_employeeIdAndStart(Integer prepNurseEmployeeId, LocalDateTime start, Pageable pageable);

    Page<Appointment> findByPrepNurse_employeeId(Integer employeeId, Pageable pageable);

    @Query("SELECT a.examinationRoom FROM Appointment a WHERE a.patient.ssn = :patientId AND a.start = :date")
    Optional<String> findByPatientIdAndStartDate(@Param("patientId") Integer patientId, @Param("date") LocalDateTime date);

    @Query("SELECT a.examinationRoom FROM Appointment a WHERE a.physician.employeeId = :physicianId AND a.start = :date")
    Page<String> findByPhysicianIdAndStartDate(@Param("physicianId") Integer physicianId, @Param("date") LocalDateTime date, Pageable pageable);

    @Query("SELECT a.examinationRoom FROM Appointment a WHERE a.prepNurse.employeeId = :nurseId AND a.start = :date")
    Page<String> findByNurseIdAndStartDate(@Param("nurseId") Integer nurseId, @Param("date") LocalDateTime date, Pageable pageable);

    @Query("SELECT a.patient FROM Appointment a WHERE a.physician.employeeId = :employeeId AND a.patient.ssn = :patientId")
    Optional<Patient> findByPhysicianIdAndPatientId(@Param("employeeId") Integer employeeId, @Param("patientId") Integer patientId);

    @Query("SELECT a.patient FROM Appointment a WHERE a.prepNurse.employeeId = :employeeId AND a.patient.ssn = :patientId")
    Optional<Patient> findByNurseIdAndPatientId(@Param("nurseId") Integer nurseId, @Param("patientId") Integer patientId);

    @Query("SELECT a.physician.name AS physicianName, COUNT(a) AS appointmentCount " +
            "FROM Appointment a " +
            "GROUP BY a.physician.name")
    List<Object[]> countAppointmentsPerPhysician();
}
