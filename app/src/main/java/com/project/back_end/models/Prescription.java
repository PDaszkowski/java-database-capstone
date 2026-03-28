package com.project.back_end.models;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Document(collection = "prescriptions")
public class Prescription {

  @Id
  private String id;

  @NotNull
  @Size(min = 3, max = 100)
  private String patientName;

  @NotNull
  private Long appointmentId;

  @NotNull
  @Size(min = 3, max = 100)
  private String medication;

  @NotNull
  private String dosage;

  @Size(max = 200)
  private String doctorNotes;
  
  public Prescription()
  {
    
  }
  
  public Prescription(String patientName, Long appointmentId, String medication, String dosage, String doctorNotes)
  {
    this.patientName = patientName;
    this.appointmentId = appointmentId;
    this.medication = medication;
    this.dosage = dosage;
    this.doctorNotes = doctorNotes;
  }

  public String getId()
  {
    return this.id;
  }

  public String getPatientName()
  {
    return this.patientName;
  }

  public Long getAppointmentId()
  {
    return this.appointmentId;
  }

  public String getMedication()
  {
    return this.medication;
  }

  public String getDosage()
  {
    return this.dosage;
  }

  public String getDoctorNotes()
  {
    return this.doctorNotes;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public void setPatientName(String patientName)
  {
    this.patientName = patientName;
  }

  public void setAppointmentId(Long appointmentId)
  {
    this.appointmentId = appointmentId;
  }

  public void setMedication(String medication)
  {
    this.medication = medication;
  }

  public void setDosage(String dosage)
  {
    this.dosage = dosage;
  }

  public void setDoctorNotes(String doctorNotes)
  {
    this.doctorNotes = doctorNotes;
  }


}
