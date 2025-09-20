package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import jakarta.transaction.Transactional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final PatientRepository patientRepository;

    private final DoctorRepository doctorRepository;

    private final TokenService tokenService;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, TokenService tokenService)
    {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public int bookAppointment(Appointment appointment)
    {
        LocalDateTime start = appointment.getAppointmentTime();
        LocalDateTime end = appointment.getAppointmentTime().plusHours(1);
        List<Appointment> overlapping = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(appointment.getDoctor().getId(), start, end);

        if(!overlapping.isEmpty())
        {
            System.out.println("Doctor is not available at this moment");
            return 0;
        }
        try {
            appointmentRepository.save(appointment);
            System.out.println("Appointment saved successfully!");
            return 1;
        } catch (Exception e) {
            System.err.println("Error has occured during booking an appointment: " +e.getMessage());
            return 0;
        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment)
    {
        
        Optional<Appointment> existingOpt = appointmentRepository.findById(appointment.getId());
        if(existingOpt.isPresent())
        {
            Appointment existing = existingOpt.get();
            if(!existing.getPatient().getId().equals(appointment.getPatient().getId()))
            {
                return ResponseEntity.badRequest().body(Map.of("message", "Patient mismatch"));
            }

            LocalDateTime newStart = appointment.getAppointmentTime();
            LocalDateTime newEnd = newStart.plusHours(1);

            List<Appointment> overlapping = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(appointment.getDoctor().getId(), newStart, newEnd);

            boolean conflict = overlapping.stream().anyMatch(a -> !a.getId().equals(appointment.getId()));

            if(conflict)
            {
                return ResponseEntity.badRequest().body(Map.of("message", "Doctor is not available at that time"));
            }

            appointmentRepository.save(appointment);
            return ResponseEntity.ok(Map.of("message", "Appointment updated successfully"));
        }
        else{
            return ResponseEntity.badRequest().body(Map.of("message", "Appointment not found"));
        }
    }

    @Transactional
    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token)
    {
    
        if(!tokenService.validateToken(token, "patient")){
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid token"));
        }
        
        Optional<Appointment> existingOpt = appointmentRepository.findById(id);
        if(existingOpt.isEmpty())
        {
            return ResponseEntity.badRequest().body(Map.of("message", "Appointment not found"));
        }
        
        Appointment appointment = existingOpt.get();
        
        String userEmail = tokenService.extractIdentifier(token);
        Patient patient = patientRepository.findByEmail(userEmail);

        if(patient == null)
        {
            return ResponseEntity.badRequest().body(Map.of("message", "Patient not found"));
        }
        
       if(!appointment.getPatient().getEmail().equals(userEmail)){
        return ResponseEntity.badRequest().body(Map.of("message", "Patient mismatch"));
       }

       appointmentRepository.delete(appointment);
       return ResponseEntity.ok(Map.of("message", "Appointment cancelled successfully"));
    }

@Transactional
public Map<String, Object> getAppointment(String pname, LocalDate date, String token) {
    Map<String, Object> response = new HashMap<>();

    System.out.println("=== DEBUG getAppointment ===");
    System.out.println("pname: " + pname);
    System.out.println("date: " + date);
    System.out.println("token: " + token.substring(0, 20) + "...");

    if(!tokenService.validateToken(token, "doctor")){
        response.put("message", "invalid Token");
        response.put("appointments", List.of());
        return response;
    }

    String doctorEmail = tokenService.extractIdentifier(token);
    Doctor doctor = doctorRepository.findByEmail(doctorEmail);
    System.out.println("Doctor email: " + doctorEmail);
    System.out.println("Doctor found: " + (doctor != null ? doctor.getName() : "null"));
    
    if(doctor == null) {
        response.put("message", "Doctor not found");
        response.put("appointments", List.of());
        return response;
    }
    
    Long doctorId = doctor.getId();
    System.out.println("Doctor ID: " + doctorId);

    LocalDateTime start = date.atStartOfDay();
    LocalDateTime end = date.atTime(LocalTime.MAX);
    System.out.println("Searching between: " + start + " and " + end);

    List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
    System.out.println("Found appointments before filtering: " + appointments.size());
    appointments.forEach(a -> System.out.println("Appointment: " + a.getId() + " at " + a.getAppointmentTime()));

    if(pname != null && !pname.isEmpty() && !"null".equals(pname)) {
        System.out.println("Filtering by patient name: " + pname);
        appointments = appointments.stream()
            .filter(a -> a.getPatient().getName().toLowerCase().contains(pname.toLowerCase()))
            .toList();
        System.out.println("Found appointments after filtering: " + appointments.size());
    }

    response.put("message", "Success");
    response.put("appointments", appointments);
    
    System.out.println("Final response appointments count: " + appointments.size());
    return response;
}

    @Transactional
    public ResponseEntity<Map<String, String>> changeAppointmentStatus(Long appointmentId, int newStatus){
        Optional<Appointment> existingOpt = appointmentRepository.findById(appointmentId);

        if(existingOpt.isEmpty())
        {
            return ResponseEntity.badRequest().body(Map.of("message", "Appointment not found"));
        }

        Appointment existing = existingOpt.get();
        existing.setStatus(newStatus);
        appointmentRepository.save(existing);

        return ResponseEntity.ok(Map.of("message", "Appointment status updated successfully!"));

    }


// 1. **Add @Service Annotation**:
//    - To indicate that this class is a service layer class for handling business logic.
//    - The `@Service` annotation should be added before the class declaration to mark it as a Spring service component.
//    - Instruction: Add `@Service` above the class definition.

// 2. **Constructor Injection for Dependencies**:
//    - The `AppointmentService` class requires several dependencies like `AppointmentRepository`, `Service`, `TokenService`, `PatientRepository`, and `DoctorRepository`.
//    - These dependencies should be injected through the constructor.
//    - Instruction: Ensure constructor injection is used for proper dependency management in Spring.

// 3. **Add @Transactional Annotation for Methods that Modify Database**:
//    - The methods that modify or update the database should be annotated with `@Transactional` to ensure atomicity and consistency of the operations.
//    - Instruction: Add the `@Transactional` annotation above methods that interact with the database, especially those modifying data.

// 4. **Book Appointment Method**:
//    - Responsible for saving the new appointment to the database.
//    - If the save operation fails, it returns `0`; otherwise, it returns `1`.
//    - Instruction: Ensure that the method handles any exceptions and returns an appropriate result code.

// 5. **Update Appointment Method**:
//    - This method is used to update an existing appointment based on its ID.
//    - It validates whether the patient ID matches, checks if the appointment is available for updating, and ensures that the doctor is available at the specified time.
//    - If the update is successful, it saves the appointment; otherwise, it returns an appropriate error message.
//    - Instruction: Ensure proper validation and error handling is included for appointment updates.

// 6. **Cancel Appointment Method**:
//    - This method cancels an appointment by deleting it from the database.
//    - It ensures the patient who owns the appointment is trying to cancel it and handles possible errors.
//    - Instruction: Make sure that the method checks for the patient ID match before deleting the appointment.

// 7. **Get Appointments Method**:
//    - This method retrieves a list of appointments for a specific doctor on a particular day, optionally filtered by the patient's name.
//    - It uses `@Transactional` to ensure that database operations are consistent and handled in a single transaction.
//    - Instruction: Ensure the correct use of transaction boundaries, especially when querying the database for appointments.

// 8. **Change Status Method**:
//    - This method updates the status of an appointment by changing its value in the database.
//    - It should be annotated with `@Transactional` to ensure the operation is executed in a single transaction.
//    - Instruction: Add `@Transactional` before this method to ensure atomicity when updating appointment status.


}
