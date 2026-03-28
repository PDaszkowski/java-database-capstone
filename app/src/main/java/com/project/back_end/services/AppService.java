package com.project.back_end.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;



@Service
public class AppService {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public AppService(TokenService tokenService, AdminRepository adminRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, DoctorService doctorService, PatientService patientService)
    {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public ResponseEntity<Map<String, String>> validateToken(String token, String user)
    {
        try {
            boolean valid = tokenService.validateToken(token, user);
            if(!valid)
            {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or expired token"));
            }

            return ResponseEntity.ok(Map.of("message", "Token is valid"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error validating token"));
        }
    }

    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin)
    {
        try {
            Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());
            if(admin != null && admin.getPassword().equals(receivedAdmin.getPassword()))
            {
                String token = tokenService.generateToken(admin.getUsername());
                return ResponseEntity.ok(Map.of("token", token));
            }

            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid username or password"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Server error"));
        }
    }

    public Map<String, Object> filterDoctor(String name, String specialty, String time)
    {
        Map<String, Object> result = new HashMap<>();
        result.put("doctors", doctorService.filterDoctorsByNameSpecialtyandTime(name, specialty, time));
        return result;
    }

    public int validateAppointment(Appointment appointment)
    {
        Optional<Doctor> doctorOpt = doctorRepository.findById(appointment.getDoctor().getId());
        if(doctorOpt.isEmpty())
        {
            System.out.println("Doctor doesn't exist");
            return -1;
        }

        LocalDate appointmentDate = appointment.getAppointmentTime().toLocalDate();
        List<String> availableSlots = doctorService.getDoctorAvailability(appointment.getDoctor().getId(), appointmentDate);

        String requestedHour = String.format("%02d:00", appointment.getAppointmentTime().getHour());
        if(availableSlots.contains(requestedHour))
        {
            return 1;
        }
        else{
            return 0;
        }
    }

    public boolean validatePatient(Patient patient)
    {
        return patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone()) == null;
    }

    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login)
    {
        try {
            Patient patient = patientRepository.findByEmail(login.getEmail());
            if(patient != null && patient.getPassword().equals(login.getPassword()))
            {
                String token = tokenService.generateToken(patient.getEmail());
                return ResponseEntity.ok(Map.of("token", token));
            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid email or password"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Server error"));
        }
    }

    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token)
    {
       try {
            String email = tokenService.extractIdentifier(token);
            Patient patient = patientRepository.findByEmail(email);

            if(patient == null)
            {
                return ResponseEntity.badRequest().body(Map.of("message", "Patient not found"));
            }
            Long patientId = patient.getId();
    
            if(condition != null && name != null)
            {
                return patientService.filterByDoctorAndCondition(condition, name, patientId);
            }

            else if(condition != null)
            {
                return patientService.filterByCondition(condition, patientId);
            }

            else if(name != null)
            {
                return patientService.filterByDoctor(name, patientId);
            }
            
            else{
                return ResponseEntity.badRequest().body(Map.of("message", "At least one filter"));
            }

       } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error filtering appointments"));
       }
    }

}
