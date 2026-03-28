package com.project.back_end.services;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository)
    {
        this.prescriptionRepository = prescriptionRepository;
    }

    
    public ResponseEntity<Map<String, String>> savePrescription(Prescription prescription)
    {
        try {
            List<Prescription> existing = prescriptionRepository.findByAppointmentId(prescription.getAppointmentId());
            if(!existing.isEmpty())
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Prescription already exists"));
            }

            prescriptionRepository.save(prescription);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Prescription saved"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error saving prescription"));
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getPrescription(Long appointmentId)
    {
        try {
            List<Prescription> prescriptions = prescriptionRepository.findByAppointmentId(appointmentId);
            if(prescriptions.isEmpty())
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No prescriptions found for this id"));
            }

            return ResponseEntity.ok(Map.of("prescriptions", prescriptions));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error retrieving prescriptions"));
        }

    }
    
}
