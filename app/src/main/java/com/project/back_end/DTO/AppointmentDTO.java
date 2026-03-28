package com.project.back_end.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.project.back_end.models.Appointment;

public class AppointmentDTO {

    private Long id;

    private Long doctorId;

    private String doctorName;

    private Long patientId;

    private String patientName;

    private String patientEmail;

    private String patientPhone;

    private String patientAddress;
    
    private LocalDateTime appointmentTime;

    private int status;

    private LocalDate appointmentDate;
    
    private LocalTime appointmentTimeOnly;

    private LocalTime endTime;

    public AppointmentDTO(Long id, Long doctorId, String doctorName, Long patientId, String patientName, String patientEmail, String patientPhone, String patientAddress, LocalDateTime appointmentTime, int status)
    {
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientPhone = patientPhone;
        this.patientAddress = patientAddress;
        this.appointmentTime = appointmentTime;
        this.status = status;

        this.appointmentDate = appointmentTime.toLocalDate();
        this.appointmentTimeOnly = appointmentTime.toLocalTime();
        this.endTime = appointmentTime.toLocalTime().plusHours(1);

    }

    public static AppointmentDTO fromEntity(Appointment a) {
        return new AppointmentDTO(
            a.getId(),
            a.getDoctor().getId(),
            a.getDoctor().getName(),
            a.getPatient().getId(),
            a.getPatient().getName(),
            a.getPatient().getEmail(),
            a.getPatient().getPhone(),
            a.getPatient().getAddress(),
            a.getAppointmentTime(),
            a.getStatus()
        );
    }
    
    public Long getId()
    {
        return this.id;
    }

    public Long getDoctorId()
    {
        return this.doctorId;
    }

    public String getDoctorName()
    {
        return this.doctorName;
    } 

    public Long getPatientId()
    {
        return this.patientId;
    }

    public String getPatientName()
    {
        return this.patientName;
    }

    public String getPatientEmail()
    {
        return this.patientEmail;
    }

    public String getPatientPhone()
    {
        return this.patientPhone;
    }

    public String getPatientAddress()
    {
        return this.patientAddress;
    }

    public int getStatus()
    {
        return this.status;
    }

    public LocalDateTime getAppointmentTime()
    {
        return this.appointmentTime;
    }

    public LocalDate getAppointmentDate()
    {
        return this.appointmentDate;
    }

    public LocalTime getAppointmentTimeOnly()
    {
        return this.appointmentTimeOnly;
    }

    public LocalTime getEndTime()
    {
        return this.endTime;
    }           


}
