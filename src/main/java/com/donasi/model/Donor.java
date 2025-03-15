package com.donasi.model;

import java.time.LocalDateTime;

public class Donor {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime registrationDate;
    
    public Donor() {
        this.registrationDate = LocalDateTime.now();
    }
    
    public Donor(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.registrationDate = LocalDateTime.now();
    }
    
    public Donor(int id, String name, String email, String phone, String address, LocalDateTime registrationDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
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

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    @Override
    public String toString() {
        return name + " (" + email + ")";
    }
}