package com.project.back_end.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.services.AppService;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/patient")
public class PatientController {
    
    private final PatientService patientService;
    private final AppService appService;
    private final TokenService tokenService;
    private final PatientRepository patientRepository;

    @Autowired
    public PatientController(PatientService patientService, AppService appService, TokenService tokenService, PatientRepository patientRepository)
    {
        this.patientService = patientService;
        this.appService = appService;
        this.tokenService = tokenService;
        this.patientRepository = patientRepository;
    }

    @GetMapping("/{token}")
    public ResponseEntity<Map<String, Object>> getPatient(@PathVariable String token)
    {
        try {
            ResponseEntity<Map<String, String>> validation = appService.validateToken(token, "patient");
            if(validation.getStatusCode() != HttpStatus.OK)
            {
                return ResponseEntity.status(validation.getStatusCode()).body(Map.of("success", false, "error", "Token is invalid or wrong"));
            }

            ResponseEntity<Map<String, Object>> patientDetails = patientService.getPatientDetails(token);
            if(patientDetails.getStatusCode() == HttpStatus.OK)
            {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.putAll(patientDetails.getBody());
                return ResponseEntity.ok(response);
            }
            else{
                return ResponseEntity.status(patientDetails.getStatusCode()).body(Map.of(
                    "success", false,
                    "error", "Error retrieving patient details"
                ));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "error", "Error has occured retrieving patient details: " +e.getMessage()
            ));
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPatient(@Valid @RequestBody Patient patient)
    {
        try {
            int result = patientService.createPatient(patient);

            return switch(result)
            {
                case 1 -> ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Patient created successfully",
                    "patient", patient
                ));
                case -1 -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "success", false,
                    "message", "Patient already exists"
                ));
                case 0 -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Database error occurred during creating patient"
                ));
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error has occured during creating patient"
                ));

            };
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "error", "Error has occured during creating patient: " +e.getMessage()
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> patientLogin(@Valid @RequestBody Login login)
    {
        try {
            ResponseEntity<Map<String, String>> validation = appService.validatePatientLogin(login);
            if(validation.getStatusCode() == HttpStatus.OK)
            {
                Map<String, String> validationBody = validation.getBody();
                if(validationBody!=null && validationBody.containsKey("token"))
                {
                    return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Patient logged in successfully",
                        "token", validationBody.get("token")
                    ));
                }
                else{
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "success", false,
                        "error", "Token generation failed"
                    ));
                }
            }
            else{
                return ResponseEntity.status(validation.getStatusCode()).body(Map.of(
                    "success", false,
                    "error", "Invalid credentials"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "error", "Error has occured during patient logging in"
            ));
        }
    }

    @GetMapping("/{id}/{token}")
    public ResponseEntity<Map<String, Object>> getPatientAppointment(@PathVariable Long id, @PathVariable String token)
    {
        try {
            ResponseEntity<Map<String, String>> validation = appService.validateToken(token, "patient");
            if(validation.getStatusCode() != HttpStatus.OK)
            {
                return ResponseEntity.status(validation.getStatusCode()).body(Map.of(
                    "success", false,
                    "error", "Token is invalid or is wrong"
                ));
            }

            ResponseEntity<Map<String, Object>> appointments = patientService.getPatientAppointment(id, token);
            if(appointments.getStatusCode() == HttpStatus.OK)
            {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.putAll(appointments.getBody());
                return ResponseEntity.ok(response);
            }
            else{
                return ResponseEntity.status(appointments.getStatusCode()).body(Map.of(
                    "success", false,
                    "error", "Error retrieving appointments"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "error", "Error has occurred while getting appointments: " +e.getMessage()
            ));
        }
    }

        @GetMapping("/appointment/filter/{condition}/{name}/{token}")
    public ResponseEntity<Map<String, Object>> filterPatientAppointment(@PathVariable String condition, @PathVariable String name, @PathVariable String token) {
        try {
            ResponseEntity<Map<String, String>> validation = appService.validateToken(token, "patient");
            if (validation.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(validation.getStatusCode())
                        .body(Map.of("success", false, "error", "Token is invalid or wrong"));
            }

            String email = tokenService.extractIdentifier(token);
            Patient patient = patientRepository.findByEmail(email);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "error", "Patient not found"));
            }

            Long patientId = patient.getId();
            ResponseEntity<Map<String, Object>> filteredAppointments;

            if ("all".equalsIgnoreCase(name)) {
                filteredAppointments = patientService.filterByCondition(condition, patientId);
            } 
            else if ("all".equalsIgnoreCase(condition)) {
                filteredAppointments = patientService.filterByDoctor(name, patientId);
            }
            else {
                filteredAppointments = patientService.filterByDoctorAndCondition(condition, name, patientId);
            }

            if (filteredAppointments.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.putAll(filteredAppointments.getBody());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(filteredAppointments.getStatusCode())
                        .body(Map.of("success", false, "error", "Error filtering appointments"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "Error has occurred while filtering appointments: " + e.getMessage()));
        }
    }


// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller for patient-related operations.
//    - Use `@RequestMapping("/patient")` to prefix all endpoints with `/patient`, grouping all patient functionalities under a common route.


// 2. Autowire Dependencies:
//    - Inject `PatientService` to handle patient-specific logic such as creation, retrieval, and appointments.
//    - Inject the shared `Service` class for tasks like token validation and login authentication.


// 3. Define the `getPatient` Method:
//    - Handles HTTP GET requests to retrieve patient details using a token.
//    - Validates the token for the `"patient"` role using the shared service.
//    - If the token is valid, returns patient information; otherwise, returns an appropriate error message.


// 4. Define the `createPatient` Method:
//    - Handles HTTP POST requests for patient registration.
//    - Accepts a validated `Patient` object in the request body.
//    - First checks if the patient already exists using the shared service.
//    - If validation passes, attempts to create the patient and returns success or error messages based on the outcome.


// 5. Define the `login` Method:
//    - Handles HTTP POST requests for patient login.
//    - Accepts a `Login` DTO containing email/username and password.
//    - Delegates authentication to the `validatePatientLogin` method in the shared service.
//    - Returns a response with a token or an error message depending on login success.


// 6. Define the `getPatientAppointment` Method:
//    - Handles HTTP GET requests to fetch appointment details for a specific patient.
//    - Requires the patient ID, token, and user role as path variables.
//    - Validates the token using the shared service.
//    - If valid, retrieves the patient's appointment data from `PatientService`; otherwise, returns a validation error.


// 7. Define the `filterPatientAppointment` Method:
//    - Handles HTTP GET requests to filter a patient's appointments based on specific conditions.
//    - Accepts filtering parameters: `condition`, `name`, and a token.
//    - Token must be valid for a `"patient"` role.
//    - If valid, delegates filtering logic to the shared service and returns the filtered result.



}


