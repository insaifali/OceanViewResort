package com.oceanview.servlet;

import com.oceanview.model.Reservation;
import com.oceanview.service.ReservationService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/receipt")
public class ReceiptServlet extends HttpServlet {
    private final ReservationService service = new ReservationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?msg=Please+login+to+continue");
            return;
        }

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Reservation r = service.getReservation(id);
            if (r == null) {
                resp.sendRedirect(req.getContextPath() + "/reservation-view.jsp?msg=Reservation+not+found");
                return;
            }

            req.setAttribute("rid", String.format("OVR-%06d", r.reservationId));
            req.setAttribute("guest", r.guestName);
            req.setAttribute("phone", r.phone);
            req.setAttribute("roomType", r.roomType);
            req.setAttribute("checkIn", String.valueOf(r.checkIn));
            req.setAttribute("checkOut", String.valueOf(r.checkOut));
            req.setAttribute("nights", String.valueOf(r.nights));
            req.setAttribute("total", String.format("%.2f", r.totalAmount));
            req.setAttribute("deposit", String.format("%.2f", r.deposit));
            req.setAttribute("balance", String.format("%.2f", r.balance));
            req.setAttribute("paymentMethod", r.paymentMethod);
            req.setAttribute("paymentStatus", r.paymentStatus);
            req.setAttribute("rid", String.valueOf(r.reservationId));

            req.getRequestDispatcher("/receipt.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/reservation-view.jsp?msg=Receipt+error");
        }
    }
}