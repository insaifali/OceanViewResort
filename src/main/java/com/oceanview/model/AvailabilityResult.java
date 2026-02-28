package com.oceanview.model;

import java.util.List;

public class AvailabilityResult {
    public String roomType;
    public int capacity;
    public int bookedCount;
    public int availableCount;
    public boolean available;
    public List<String[]> bookings; // [id, guest, checkIn, checkOut]
}