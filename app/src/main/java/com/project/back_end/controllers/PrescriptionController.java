package com.project.back_end.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppService;
import com.project.back_end.services.PrescriptionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {
    
    private final PrescriptionService prescriptionService;
    private final AppService appService;

    public PrescriptionController(PrescriptionService prescriptionService, AppService appService)
    {
        this.prescriptionService = prescriptionService;
        this.appService = appService;
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, Object>> savePrescription(@Valid @RequestBody Prescription prescription, @PathVariable String token) {
        try {
            ResponseEntity<Map<String, String>> validation = appService.validateToken(token, "doctor");
            if (validation.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(validation.getStatusCode())
                        .body(Map.of("success", false, "error", "Token is invalid or wrong"));
            }

            ResponseEntity<Map<String, String>> result = prescriptionService.savePrescription(prescription);

            if (result.getStatusCode() == HttpStatus.CREATED) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Prescription saved successfully");
                response.put("prescription", prescription);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
                
            } else if (result.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                        "success", false,
                        "message", "Prescription already exists for this appointment"
                ));
            } else {
                return ResponseEntity.status(result.getStatusCode()).body(Map.of(
                        "success", false,
                        "message", "Error occurred during saving prescription"
                ));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "Error has occurred during saving prescription: " + e.getMessage()));
        }
    }

    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> getPrescription(@PathVariable Long appointmentId, @PathVariable String token) {
        try {
            ResponseEntity<Map<String, String>> validation = appService.validateToken(token, "doctor");
            if (validation.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(validation.getStatusCode())
                        .body(Map.of("success", false, "error", "Token is invalid or wrong"));
            }

            ResponseEntity<Map<String, Object>> result = prescriptionService.getPrescription(appointmentId);
            
            if (result.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.putAll(result.getBody());
                response.put("appointmentId", appointmentId);
                return ResponseEntity.ok(response);
            } else if (result.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "No prescription found for this appointment"
                ));
            } else {
                return ResponseEntity.status(result.getStatusCode()).body(Map.of(
                        "success", false,
                        "message", "Error occurred while retrieving prescription"
                ));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "Error has occurred while retrieving prescription: " + e.getMessage()));
        }
    }


    
// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller.
//    - Use `@RequestMapping("${api.path}prescription")` to set the base path for all prescription-related endpoints.
//    - This controller manages creating and retrieving prescriptions tied to appointments.


// 2. Autowire Dependencies:
//    - Inject `PrescriptionService` to handle logic related to saving and fetching prescriptions.
//    - Inject the shared `Service` class for token validation and role-based access control.
//    - Inject `AppointmentService` to update appointment status after a prescription is issued.


// 3. Define the `savePrescription` Method:
//    - Handles HTTP POST requests to save a new prescription for a given appointment.
//    - Accepts a validated `Prescription` object in the request body and a doctor’s token as a path variable.
//    - Validates the token for the `"doctor"` role.
//    - If the token is valid, updates the status of the corresponding appointment to reflect that a prescription has been added.
//    - Delegates the saving logic to `PrescriptionService` and returns a response indicating success or failure.


// 4. Define the `getPrescription` Method:
//    - Handles HTTP GET requests to retrieve a prescription by its associated appointment ID.
//    - Accepts the appointment ID and a doctor’s token as path variables.
//    - Validates the token for the `"doctor"` role using the shared service.
//    - If the token is valid, fetches the prescription using the `PrescriptionService`.
//    - Returns the prescription details or an appropriate error message if validation fails.


}
