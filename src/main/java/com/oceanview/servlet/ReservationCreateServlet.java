package com.oceanview.servlet;

import com.oceanview.model.Reservation;
import com.oceanview.service.ReservationService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/reservation/create")
public class ReservationCreateServlet extends HttpServlet {
    private final ReservationService service = new ReservationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?msg=Please+login+to+continue");
            return;
        }

        try {
            Reservation r = new Reservation();
            r.guestName = req.getParameter("guestName");
            r.phone = req.getParameter("phone");
            r.email = req.getParameter("email");
            r.address = req.getParameter("address");
            r.roomType = req.getParameter("roomType");
            r.checkIn = LocalDate.parse(req.getParameter("checkIn"));
            r.checkOut = LocalDate.parse(req.getParameter("checkOut"));
            r.guests = Integer.parseInt(req.getParameter("guests"));
            r.notes = req.getParameter("notes");
            r.paymentMethod = req.getParameter("paymentMethod");

            String depRaw = req.getParameter("deposit");
            r.deposit = (depRaw == null || depRaw.trim().isEmpty()) ? 0 : Double.parseDouble(depRaw);

            r.createdBy = (String) session.getAttribute("username");

            int id = service.createReservation(r);

            // fetch full reservation from DB for accurate totals + status
            var saved = service.getReservation(id);
            String receiptNo = "OVR-" + String.format("%06d", id);

            com.oceanview.util.MailUtil.sendAsync(
                    saved.email,
                    "Ocean View Resort — Booking Confirmation (" + receiptNo + ")",
                    com.oceanview.util.MailTemplates.bookingConfirm(saved, receiptNo)
            );

            resp.sendRedirect(req.getContextPath() + "/receipt?id=" + id);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/reservation-form.jsp?msg=" + url(e.getMessage()));
        }
    }

    private String url(String s) {
        try { return java.net.URLEncoder.encode(String.valueOf(s), "UTF-8"); }
        catch (Exception e) { return "Error"; }
    }
}