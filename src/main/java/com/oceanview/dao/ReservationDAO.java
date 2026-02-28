package com.oceanview.dao;

import com.oceanview.model.Reservation;

import java.util.List;

public interface ReservationDAO {
    boolean hasOverlap(String roomType, java.sql.Date checkIn, java.sql.Date checkOut) throws Exception;
    int create(Reservation r) throws Exception;
    Reservation findById(int id) throws Exception;
    List<Reservation> search(String phone, Integer reservationId) throws Exception;
    boolean cancel(int reservationId, String cancelledBy) throws Exception;
    boolean addPayment(int reservationId, double addAmount) throws Exception;
    boolean updatePayment(int reservationId, double newDeposit, double newBalance, String newStatus) throws Exception;
    java.util.List<String[]> getAvailability() throws Exception;
    int countOverlapsForType(String roomType, java.sql.Date from, java.sql.Date to) throws Exception;
    java.util.List<String[]> overlappingBookings(String roomType, java.sql.Date from, java.sql.Date to) throws Exception;
}