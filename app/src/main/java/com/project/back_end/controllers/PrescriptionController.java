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

}
