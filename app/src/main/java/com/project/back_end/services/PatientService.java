package com.project.back_end.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    private final AppointmentRepository appointmentRepository;

    private final TokenService tokenService;

    public PatientService(PatientRepository patientRepository, AppointmentRepository appointmentRepository, TokenService tokenService)
    {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public int createPatient(Patient patient)
    {
        if(patientRepository.findByEmail(patient.getEmail()) != null)
        {
            System.out.println("This patient already exists!");
            return -1;
        }
        try {
            patientRepository.save(patient);
            System.out.println("Patient created successfully!");
            return 1;
        } catch (Exception e) {
            System.err.println("An error has occured during creating patient");
            return 0;
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long id, String token) {
    try {
        String email = tokenService.extractIdentifier(token);
        Patient patient = patientRepository.findByEmail(email);

        if (patient == null || !patient.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Unauthorized access"));
        }

        List<Appointment> appointments = appointmentRepository.findByPatientId(id);
        List<AppointmentDTO> dtoList = appointments.stream()
            .map(AppointmentDTO::fromEntity)
            .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("appointments", dtoList));

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("message", "Error retrieving appointments"));
    }
    }

@Transactional(readOnly = true)
public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long patientId) {
    try {
        List<Appointment> appointments;
        LocalDateTime now = LocalDateTime.now();
        
        switch(condition.toLowerCase()) {
            case "all":
                appointments = appointmentRepository.findByPatientId(patientId);
                break;
            case "upcoming":
            case "future":
                appointments = appointmentRepository.findByPatientId(patientId)
                    .stream()
                    .filter(app -> app.getAppointmentTime().isAfter(now))
                    .collect(Collectors.toList());
                break;
            case "past":
                appointments = appointmentRepository.findByPatientId(patientId)
                    .stream()
                    .filter(app -> app.getAppointmentTime().isBefore(now))
                    .collect(Collectors.toList());
                break;
            default:
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid condition: " + condition));
        }

        List<AppointmentDTO> dtoList = appointments.stream()
                                               .map(AppointmentDTO::fromEntity)
                                               .sorted((a, b) -> a.getAppointmentTime().compareTo(b.getAppointmentTime()))
                                               .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("appointments", dtoList));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                         .body(Map.of("message", "Error filtering appointments by condition"));
    }
}

@Transactional(readOnly = true) 
public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(String condition, String name, Long patientId) {
    try {
        List<Appointment> appointments;
        LocalDateTime now = LocalDateTime.now();

        List<Appointment> doctorFiltered = appointmentRepository
            .findByPatientIdAndDoctor_NameContainingIgnoreCase(patientId, name);

        switch(condition.toLowerCase()) {
            case "all":
                appointments = doctorFiltered;
                break;
            case "upcoming": 
            case "future":
                appointments = doctorFiltered.stream()
                    .filter(app -> app.getAppointmentTime().isAfter(now))
                    .collect(Collectors.toList());
                break;
            case "past":
                appointments = doctorFiltered.stream()
                    .filter(app -> app.getAppointmentTime().isBefore(now))
                    .collect(Collectors.toList());
                break;
            default:
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid condition: " + condition));
        }

        List<AppointmentDTO> dtoList = appointments.stream()
                                            .map(AppointmentDTO::fromEntity)
                                            .sorted((a, b) -> a.getAppointmentTime().compareTo(b.getAppointmentTime()))
                                            .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("appointments", dtoList));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("message", "Error filtering appointments by doctor and condition"));
    }
}
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> filterByDoctor(String name, Long patientId) {
        try {
            List<Appointment> appointments = appointmentRepository.findByPatientIdAndDoctor_NameContainingIgnoreCase(patientId, name);
            List<AppointmentDTO> dtoList = appointments.stream()
                                                   .map(AppointmentDTO::fromEntity)
                                                   .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of("appointments", dtoList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(Map.of("message", "Error filtering appointments by doctor"));
        }
    }


    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {
        try {
            String email = tokenService.extractIdentifier(token);
            Patient patient = patientRepository.findByEmail(email);

            if(patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("message", "Patient not found"));
            }

            return ResponseEntity.ok(Map.of("patient", patient));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(Map.of("message", "Error retrieving patient details"));
        }
    }
}
