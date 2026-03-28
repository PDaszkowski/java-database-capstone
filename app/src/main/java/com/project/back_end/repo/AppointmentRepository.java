package com.project.back_end.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.back_end.models.Appointment;

import jakarta.transaction.Transactional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

   @Query("Select a from Appointment a " +
         "left join fetch a.doctor d " +
         "left join fetch d.availableTimes " +
         "where d.id = :doctorId " +
         "and a.appointmentTime between :start and :end")
   List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);

   @Query("Select a from Appointment a " +
         "left join fetch a.doctor d " +
         "left join fetch a.patient p " +
         "where d.id = :doctorId " +
         "and lower(p.name) like lower(concat('%', :patientName, '%')) " +
         "and a.appointmentTime between :start and :end")
   List<Appointment> findByDoctorAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(Long doctorId, String patientName, LocalDateTime start, LocalDateTime end);

   @Modifying
   @Transactional
   void deleteAllByDoctorId(Long doctorId);

   List<Appointment> findByPatientId(Long patientId);

   List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(Long patientId, int status);

   List<Appointment> findByPatientIdAndStatus(Long patientId, int status);

   @Query("Select a from Appointment a " +
         "where lower(a.doctor.name) like lower(concat('%', :doctorName, '%')) " +
         "and a.patient.id = :patientId")
   List<Appointment> filterByDoctorNameAndPatientId(String doctorName, Long patientId);

   @Query("Select a from Appointment a " +
         "where lower(a.doctor.name) like lower(concat('%', :doctorName, '%')) " +
         "and a.patient.id = :patientId " +
         "and a.status = :status")
   List<Appointment> filterByDoctorNameAndPatientIdAndStatus(String doctorName, Long patientId, int status);

   List<Appointment> findByPatientIdAndDoctor_NameContainingIgnoreCase(Long patientId, String doctorName);

   List<Appointment> findByPatientIdAndDoctor_NameContainingIgnoreCaseAndStatus(Long patientId, String doctorName, int status);



   @Modifying
   @Transactional
   @Query("update Appointment a set a.status = :status where a.id = :id")
   void updateStatus(int status, long id);

}
