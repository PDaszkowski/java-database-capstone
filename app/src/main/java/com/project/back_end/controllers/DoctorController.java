package com.project.back_end.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.AppService;
import com.project.back_end.services.DoctorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final AppService appService;

    @Autowired
    public DoctorController(DoctorService doctorService, AppService appService)
    {
        this.doctorService = doctorService;
        this.appService = appService;
    }

    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(@PathVariable String user, @PathVariable Long doctorId, @PathVariable LocalDate date, @PathVariable String token)
    {
        try {
            ResponseEntity<Map<String, String>> validation = appService.validateToken(token, user);
            if(validation.getStatusCode() != HttpStatus.OK)
            {
                return ResponseEntity.status(validation.getStatusCode()).body(Map.of("success", false, "error", "Token is invalid or wrong"));
            }

            List<String> availableSlots = doctorService.getDoctorAvailability(doctorId, date);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("doctorId", doctorId);
            response.put("date", date.toString());
            response.put("availableSlots", availableSlots);

            return ResponseEntity.ok(response);

            
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "error", "Error has occurred while getting doctor availability: " +e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctors()
    {
        try {
            List<Doctor> doctors = doctorService.getDoctors();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("doctors", doctors);

            return ResponseEntity.ok(response);


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "error", "Error has occurred while getting doctors: " +e.getMessage()));
        }
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, Object>> saveDoctor(@Valid @RequestBody Doctor doctor, @PathVariable String token)
    {
        try {
            ResponseEntity<Map<String, String>> validation = appService.validateToken(token, "admin");
            if(validation.getStatusCode() != HttpStatus.OK)
            {
                return ResponseEntity.status(validation.getStatusCode()).body(Map.of("success", false, "error", "Token is invalid or wrong"));
            }

            int result = doctorService.saveDoctor(doctor);

            return switch (result) {
                case 1 -> ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                        "success", true,
                        "message", "Doctor added to database",
                        "doctor", doctor
                ));
                case -1 -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                        "success", false,
                        "message", "Doctor already exists"
                ));
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "success", false,
                        "message", "Error has occurred during saving Doctor"
                ));
            };

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "error", "Error has occurred during saving Doctor: " +e.getMessage()
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> doctorLogin(@Valid @RequestBody Login login)
    {
        try {
            ResponseEntity<Map<String, String>> validation = doctorService.validateDoctor(login);
            if(validation.getStatusCode() == HttpStatus.OK)
            {
                Map<String, String> validationBody = validation.getBody();
                if(validationBody != null && validationBody.containsKey("token")) {
                    return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Doctor logged in successfully",
                        "token", validationBody.get("token")
                    ));
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "success", false,
                        "error", "Token generation failed"
                    ));
                }
            }
        else 
        {
            return ResponseEntity.status(validation.getStatusCode()).body(Map.of(
                "success", false,
                "error", "Invalid credentials"
            ));
        }

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
            "success", false,
            "error", "Error has occurred during doctor logging in: " + e.getMessage()
            ));
        }
    }
    

    @PutMapping("/{token}")
    public ResponseEntity<Map<String, Object>> updateDoctor(@Valid @RequestBody Doctor doctor, @PathVariable String token)
    {
        try {
            ResponseEntity<Map<String, String>> validation = appService.validateToken(token, "admin");
            if(validation.getStatusCode() != HttpStatus.OK)
            {
                return ResponseEntity.status(validation.getStatusCode()).body(Map.of("success", false, "error", "Token is invalid or wrong"));
            }

            int result = doctorService.updateDoctor(doctor);
            return switch (result) {
                case 1 -> ResponseEntity.status(HttpStatus.OK).body(Map.of(
                        "success", true,
                        "message", "Doctor updated",
                        "doctor", doctor
                ));
                case -1 -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Doctor not found"
                ));
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "success", false,
                        "message", "Error has occurred during updating Doctor"
                ));
            };


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "error", "Error has occurred during updating Doctor: " +e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, Object>> deleteDoctor(@PathVariable Long id, @PathVariable String token)
    {
        try {
            ResponseEntity<Map<String, String>> validation = appService.validateToken(token, "admin");
            if(validation.getStatusCode() != HttpStatus.OK)
            {
                return ResponseEntity.status(validation.getStatusCode()).body(Map.of("success", false, "error", "Token is invalid or wrong"));
            }

            int result = doctorService.deleteDoctor(id);
            return switch (result) {
                case 1 -> ResponseEntity.status(HttpStatus.OK).body(Map.of(
                        "success", true,
                        "message", "Doctor and appointments deleted successfully!"
                ));
                case -1 -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Doctor not found"
                ));
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "success", false,
                        "message", "Error has occurred during deleting Doctor"
                ));
            };


        } catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "error", "Error has occurred during deleting Doctor: " +e.getMessage()
            ));
        }
    }

@GetMapping("/filter/{name}/{time}/{specialty}")
public ResponseEntity<Map<String, Object>> filterDoctors(
        @PathVariable String name,
        @PathVariable String time,
        @PathVariable String specialty) {
    try {
        System.out.println("Received filter request - name: " + name + ", time: " + time + ", specialty: " + specialty); 
        
        Map<String, Object> result;

        if ("all".equalsIgnoreCase(name) && "all".equalsIgnoreCase(time) && "all".equalsIgnoreCase(specialty)) {
            result = Map.of("doctors", doctorService.getDoctors());
        } else if ("all".equalsIgnoreCase(time)) {
            result = doctorService.filterDoctorByNameAndSpecialty(name, specialty);
        } else if ("all".equalsIgnoreCase(specialty)) {
            result = doctorService.filterDoctorByNameAndTime(name, time);
        } else if ("all".equalsIgnoreCase(name)) {
            result = doctorService.filterDoctorByTimeAndSpecialty(specialty, time);
        } else {
            result = doctorService.filterDoctorsByNameSpecialtyandTime(name, specialty, time);
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "doctors", result.get("doctors")
        ));
    } catch (Exception e) {
        e.printStackTrace(); 
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of(
                "success", false,
                "error", "Error filtering doctors: " + e.getMessage()
            ));
    }
}



}
