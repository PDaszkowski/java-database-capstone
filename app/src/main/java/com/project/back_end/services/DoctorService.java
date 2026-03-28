package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;



@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    private final AppointmentRepository appointmentRepository;

    private final TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, TokenService tokenService)
    {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Transactional(readOnly = true)
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date)
    {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);

        Set<Integer> bookedHours = appointments.stream().map(a -> a.getAppointmentTime().getHour()).collect(Collectors.toSet());

        List<String> availableSlots = new ArrayList<>();
        for(int hour = 9; hour<17; hour++)
        {
            if(!bookedHours.contains(hour)){
                availableSlots.add(String.format("%02d:00", hour));
            }
        }
        return availableSlots;
    }

    @Transactional
    public int saveDoctor(Doctor doctor){
        if(doctorRepository.findByEmail(doctor.getEmail()) != null)
        {
            System.out.println("Doctor already exists");
            return -1;
        }
        try {
            doctorRepository.save(doctor);
            System.out.println("Doctor saved successfully!");
            return 1;
        } catch (Exception e) {
            System.out.println("Error has occured during saving doctor: " +e.getMessage());
            return 0;
        }
    }

    @Transactional
    public int updateDoctor(Doctor doctor)
    {
        if(!doctorRepository.existsById(doctor.getId()))
        {
            System.out.println("Doctor not found");
            return -1;
        }
        try {
            doctorRepository.save(doctor);
            System.out.println("Doctor updated successfully");
            return 1;
        } catch (Exception e) {
            System.out.println("Error has occured during updating doctor: " +e.getMessage());
            return 0;
        }
    }

    @Transactional(readOnly = true)
    public List<Doctor> getDoctors()
    {
        List<Doctor> doctors = doctorRepository.findAll();
        doctors.forEach(doc -> doc.getAvailableTimes().size());
        return doctorRepository.findAll();
    }

    @Transactional
    public int deleteDoctor(Long id)
    {
        if(!doctorRepository.existsById(id))
        {
            System.out.println("Doctor not found");
            return -1;
        }
        try {
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            System.out.println("Doctor and appointments deleted successfully!");
            return 1;
        } catch (Exception e) {
            System.out.println("Error has occured during deleting doctor: " +e.getMessage());
            return 0;
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, String>> validateDoctor(Login login)
    {
        Doctor doctor = doctorRepository.findByEmail(login.getEmail());
        if(doctor == null || !doctor.getPassword().equals(login.getPassword()))
        {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials"));
        }

        String token = tokenService.generateToken(doctor.getEmail());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> findDoctorByName(String name){
        List<Doctor> doctors = doctorRepository.findByNameLike(name);
        return Map.of("doctors", doctors);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorsByNameSpecialtyandTime(String name, String specialty, String amOrPm)
    {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        doctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", doctors);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByNameAndTime(String name, String amOrPm)
    {
        List<Doctor> doctors = doctorRepository.findByNameLike(name);
        doctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", doctors);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByNameAndSpecialty(String name, String specialty)
    {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return Map.of("doctors", doctors);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByTimeAndSpecialty(String specialty, String amOrPm)
    {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        doctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", doctors);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorBySpecialty(String specialty)
    {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return Map.of("doctors", doctors);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorsByTime(String amOrPm)
    {
        List<Doctor> doctors = doctorRepository.findAll();
        doctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", doctors);
    }

    @Transactional(readOnly = true)
public List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {
    if ("all".equalsIgnoreCase(amOrPm)) {
        return doctors;
    }
    
    return doctors.stream()
        .filter(d -> d.getAvailableTimes().stream()
            .anyMatch(time -> {
                int hour = Integer.parseInt(time.split(":")[0]);
                return "AM".equalsIgnoreCase(amOrPm) ? hour < 12 : hour >= 12;
            }))
        .collect(Collectors.toList());
}
}
