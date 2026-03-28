package com.project.back_end.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "specialty")
    private String specialty;

    @NotNull
    @Email(message = "email should follow only valid format: e.g., doctor@example.com")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 6, message = "password must be at least 6 characters long")
    private String password;

    @NotNull
    @Pattern(regexp = "^[0-9]{10}$")
    private String phone;

    @ElementCollection
    private List<String> availableTimes;

    public Long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getSpecialty()
    {
        return this.specialty;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getPassword()
    {
        return this.password;
    }

    public String getPhone()
    {
        return this.phone;
    }

    @ElementCollection
    public List<String> getAvailableTimes()
    {
        return this.availableTimes;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSpecialty(String specialty)
    {
        this.specialty = specialty;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public void setAvailableTimes(List<String> availableTimes)
    {
        this.availableTimes = availableTimes;
    }



}

