package com.project.back_end.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    @NotNull
    @Email(message = "email should follow only valid format: e.g., patient@example.com")
    private String email;

    @NotNull
    @Size(min = 6)
    private String password;

    @NotNull
    @Pattern(regexp = "^[0-9]{10}$")
    private String phone;

    @NotNull
    @Size(max = 255)
    private String address;

    public Long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
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

    public String getAddress()
    {
        return this.address;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public void setAddress(String address)
    {
        this.address = address;
    }


}
