package com.project.back_end.controllers;


import java.time.LocalDate;
import java.util.HashMap;
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

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppService;
import com.project.back_end.services.AppointmentService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final AppService appService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, AppService appService)
    {
        this.appointmentService = appointmentService;
        this.appService = appService;
    }


    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<Map<String, Object>> getAppointments(
            @PathVariable LocalDate date, 
            @PathVariable String patientName, 
            @PathVariable String token) {
        
        try {
            
            ResponseEntity<Map<String, String>> validation = appService.validateToken(token, "doctor");
            if (validation.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(validation.getStatusCode())
                    .body(Map.of("error", "Token is invalid or expired"));
            }
            
            Map<String, Object> result = appointmentService.getAppointment(patientName, date, token);
            
            String message = (String) result.get("message");
            if ("Success".equals(message)) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "An error has occured during getting appointments " + e.getMessage()));
        }
    }

    @PostMapping("/{token}")
    public ResponseEntity<Map<String, Object>> bookAppointment(
            @Valid @RequestBody Appointment appointment, 
            @PathVariable String token) {
        
        try {
            ResponseEntity<Map<String, String>> validation = appService.validateToken(token, "patient");
            if (validation.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(validation.getStatusCode())
                    .body(Map.of("error", "Token is invalid or expired"));
            }
            int result = appointmentService.bookAppointment(appointment);
            
            if (result == 1) {
                return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Appointment has been booked",
                    "appointment", appointment
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", "Doctor is not available at the moment"
                ));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "success", false,
                    "error", "An error has occured trying to book an appointment: " + e.getMessage()
                ));
        }
    }

    @PutMapping("/{token}")
    public ResponseEntity<Map<String, Object>> updateAppointment(
            @Valid @RequestBody Appointment appointment, 
            @PathVariable String token) {
        
        try {
            ResponseEntity<Map<String, String>> validation = appService.validateToken(token, "patient");
            if (validation.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(validation.getStatusCode())
                    .body(Map.of("error", "Token is invalid or expired"));
            }
            ResponseEntity<Map<String, String>> serviceResult = appointmentService.updateAppointment(appointment);
            

            Map<String, Object> responseBody = new HashMap<>();
            
            if (serviceResult.getStatusCode() == HttpStatus.OK) {

                responseBody.put("success", true);
                responseBody.put("message", "Appointment has been updated successfully");
                responseBody.put("appointment", appointment);
                return ResponseEntity.ok(responseBody);
            } else {

                responseBody.put("success", false);
                responseBody.put("message", "Failed to update an appointment. Try again later");
                return ResponseEntity.status(serviceResult.getStatusCode()).body(responseBody);
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "success", false,
                    "error", "An error has occured updating appointment: " + e.getMessage()
                ));
        }
    }


    @DeleteMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> cancelAppointment(
            @PathVariable Long appointmentId, 
            @PathVariable String token) {
        
        try {
            ResponseEntity<Map<String, String>> validation = appService.validateToken(token, "patient");
            if (validation.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(validation.getStatusCode())
                    .body(Map.of("error", "Token is invalid or expired"));
            }
            
            ResponseEntity<Map<String, String>> serviceResult = appointmentService.cancelAppointment(appointmentId, token);
            
            Map<String, Object> responseBody = new HashMap<>();
             
            if (serviceResult.getStatusCode() == HttpStatus.OK) {
                responseBody.put("success", true);
                responseBody.put("message", "Appointment canceled successfully");
                responseBody.put("appointmentId", appointmentId);
                return ResponseEntity.ok(responseBody);
            } else {
                responseBody.put("success", false);
                responseBody.put("message", "Failed to cancel appointment. Try again later");
                return ResponseEntity.status(serviceResult.getStatusCode()).body(responseBody);
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "success", false,
                    "error", "An error has occured trying to cancelling appointment: " + e.getMessage()
                ));
        }
    }

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller.
//    - Use `@RequestMapping("/appointments")` to set a base path for all appointment-related endpoints.
//    - This centralizes all routes that deal with booking, updating, retrieving, and canceling appointments.


// 2. Autowire Dependencies:
//    - Inject `AppointmentService` for handling the business logic specific to appointments.
//    - Inject the general `Service` class, which provides shared functionality like token validation and appointment checks.


// 3. Define the `getAppointments` Method:
//    - Handles HTTP GET requests to fetch appointments based on date and patient name.
//    - Takes the appointment date, patient name, and token as path variables.
//    - First validates the token for role `"doctor"` using the `Service`.
//    - If the token is valid, returns appointments for the given patient on the specified date.
//    - If the token is invalid or expired, responds with the appropriate message and status code.


// 4. Define the `bookAppointment` Method:
//    - Handles HTTP POST requests to create a new appointment.
//    - Accepts a validated `Appointment` object in the request body and a token as a path variable.
//    - Validates the token for the `"patient"` role.
//    - Uses service logic to validate the appointment data (e.g., check for doctor availability and time conflicts).
//    - Returns success if booked, or appropriate error messages if the doctor ID is invalid or the slot is already taken.


// 5. Define the `updateAppointment` Method:
//    - Handles HTTP PUT requests to modify an existing appointment.
//    - Accepts a validated `Appointment` object and a token as input.
//    - Validates the token for `"patient"` role.
//    - Delegates the update logic to the `AppointmentService`.
//    - Returns an appropriate success or failure response based on the update result.


// 6. Define the `cancelAppointment` Method:
//    - Handles HTTP DELETE requests to cancel a specific appointment.
//    - Accepts the appointment ID and a token as path variables.
//    - Validates the token for `"patient"` role to ensure the user is authorized to cancel the appointment.
//    - Calls `AppointmentService` to handle the cancellation process and returns the result.


}
