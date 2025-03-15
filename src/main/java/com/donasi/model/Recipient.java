package com.donasi.model;

import java.time.LocalDateTime;

public class Recipient {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String description;
    private LocalDateTime registrationDate;
    
    public Recipient() {
        this.registrationDate = LocalDateTime.now();
    }
    
    public Recipient(String name, String email, String phone, String address, String description) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.description = description;
        this.registrationDate = LocalDateTime.now();
    }
    
    public Recipient(int id, String name, String email, String phone, String address, String description, LocalDateTime registrationDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.description = description;
        this.registrationDate = registrationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    @Override
    public String toString() {
        return name + " (" + description + ")";
    }
}