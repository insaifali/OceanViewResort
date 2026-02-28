package com.oceanview.util;

import com.oceanview.model.Reservation;

public final class MailTemplates {
    private MailTemplates(){}

    public static String bookingConfirm(Reservation r, String receiptNo) {
        return "<div style='font-family:Arial,sans-serif;line-height:1.5'>" +
                "<h2>Ocean View Resort — Booking Confirmation</h2>" +
                "<p>Hi " + esc(r.guestName) + ",</p>" +
                "<p>Your booking is confirmed.</p>" +
                "<p><b>Receipt:</b> " + esc(receiptNo) + "<br>" +
                "<b>Room:</b> " + esc(r.roomType) + "<br>" +
                "<b>Dates:</b> " + r.checkIn + " → " + r.checkOut + " (" + r.nights + " nights)<br>" +
                "<b>Total:</b> LKR " + String.format("%.2f", r.totalAmount) + "<br>" +
                "<b>Paid:</b> LKR " + String.format("%.2f", r.deposit) + "<br>" +
                "<b>Balance:</b> LKR " + String.format("%.2f", r.balance) + "</p>" +
                "<p>Thank you.</p>" +
                "</div>";
    }

    public static String paymentUpdate(Reservation r, String receiptNo) {
        return "<div style='font-family:Arial,sans-serif;line-height:1.5'>" +
                "<h2>Ocean View Resort — Payment Updated</h2>" +
                "<p>Receipt: <b>" + esc(receiptNo) + "</b></p>" +
                "<p><b>Status:</b> " + esc(r.paymentStatus) + "<br>" +
                "<b>Total:</b> LKR " + String.format("%.2f", r.totalAmount) + "<br>" +
                "<b>Paid:</b> LKR " + String.format("%.2f", r.deposit) + "<br>" +
                "<b>Balance:</b> LKR " + String.format("%.2f", r.balance) + "</p>" +
                "</div>";
    }

    public static String cancelled(Reservation r, String receiptNo) {
        return "<div style='font-family:Arial,sans-serif;line-height:1.5'>" +
                "<h2>Ocean View Resort — Reservation Cancelled</h2>" +
                "<p>Receipt: <b>" + esc(receiptNo) + "</b></p>" +
                "<p>Your reservation has been cancelled. If this was a mistake, please contact reception.</p>" +
                "</div>";
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}