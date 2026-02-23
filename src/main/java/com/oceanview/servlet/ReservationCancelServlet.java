package com.oceanview.servlet;

import com.oceanview.service.ReservationService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/reservation/cancel")
public class ReservationCancelServlet extends HttpServlet {
    private final ReservationService service = new ReservationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        String role = (String) session.getAttribute("role");

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            service.cancelReservation(id, role);

            resp.sendRedirect(req.getContextPath() + "/reservation/search?msg=Reservation+cancelled");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/reservation/search?msg=" + url(e.getMessage()));
        }
    }

    private String url(String s) {
        try { return java.net.URLEncoder.encode(String.valueOf(s), "UTF-8"); }
        catch (Exception e) { return "Error"; }
    }
}