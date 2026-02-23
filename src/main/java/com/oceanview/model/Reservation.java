package com.oceanview.model;

import java.time.LocalDate;

public class Reservation {
    public int reservationId;
    public String guestName;
    public String phone;
    public String address;
    public String roomType; // SINGLE/DOUBLE/SUITE
    public LocalDate checkIn;
    public LocalDate checkOut;
    public int guests;
    public String notes;

    public int nights;
    public double totalAmount;
    public double deposit;
    public double balance;

    public String paymentMethod; // CASH/CARD/BANK_TRANSFER
    public String paymentStatus; // UNPAID/PARTIAL/PAID
    public String createdBy;
    public String status; // ACTIVE/CANCELLED
}