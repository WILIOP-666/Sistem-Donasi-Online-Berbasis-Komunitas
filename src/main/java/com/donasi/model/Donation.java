package com.donasi.model;

import java.time.LocalDateTime;

public class Donation {
    private int id;
    private int donorId;
    private int recipientId;
    private String itemType;
    private String description;
    private int quantity;
    private String status; // Pending, In Transit, Delivered, Cancelled
    private LocalDateTime donationDate;
    private String deliveryLocation;
    private double latitude;
    private double longitude;
    
    public Donation() {
        this.donationDate = LocalDateTime.now();
        this.status = "Pending";
    }
    
    public Donation(int donorId, int recipientId, String itemType, String description, 
                   int quantity, String deliveryLocation, double latitude, double longitude) {
        this.donorId = donorId;
        this.recipientId = recipientId;
        this.itemType = itemType;
        this.description = description;
        this.quantity = quantity;
        this.status = "Pending";
        this.donationDate = LocalDateTime.now();
        this.deliveryLocation = deliveryLocation;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public Donation(int id, int donorId, int recipientId, String itemType, String description, 
                   int quantity, String status, LocalDateTime donationDate, 
                   String deliveryLocation, double latitude, double longitude) {
        this.id = id;
        this.donorId = donorId;
        this.recipientId = recipientId;
        this.itemType = itemType;
        this.description = description;
        this.quantity = quantity;
        this.status = status;
        this.donationDate = donationDate;
        this.deliveryLocation = deliveryLocation;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDonorId() {
        return donorId;
    }

    public void setDonorId(int donorId) {
        this.donorId = donorId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(LocalDateTime donationDate) {
        this.donationDate = donationDate;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    @Override
    public String toString() {
        return itemType + " - " + description + " (" + status + ")";
    }
}