package com.project.back_end.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

@Entity
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @NotNull
  private Doctor doctor;

  @ManyToOne
  @NotNull
  private Patient patient;

  @Future(message = "Appointment must be in the future")
  private LocalDateTime appointmentTime;

  @NotNull
  private int status;

  public Appointment()
  {

  }

  @Transient
  public LocalDateTime getEndTime()
  {
    return this.appointmentTime.plusHours(1);
  }

  @Transient
  public LocalDate getAppointmentDate()
  {
    return this.appointmentTime.toLocalDate();
  }

  @Transient
  public LocalTime getAppointmentTimeOnly()
  {
    return this.appointmentTime.toLocalTime();
  }

  public Long getId()
  {
    return this.id;
  }

  public Doctor getDoctor()
  {
    return this.doctor;
  }

  public Patient getPatient()
  {
    return this.patient;
  }

  public LocalDateTime getAppointmentTime()
  {
    return this.appointmentTime;
  }

  public int getStatus()
  {
    return this.status;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public void setDoctor(Doctor doctor)
  {
    this.doctor = doctor;
  }

  public void setPatient(Patient patient)
  {
    this.patient = patient;
  }

  public void setAppointmentTime(LocalDateTime appointmentTime)
  {
    this.appointmentTime = appointmentTime;
  }

  public void setStatus(int status)
  {
    this.status = status;
  }



}

