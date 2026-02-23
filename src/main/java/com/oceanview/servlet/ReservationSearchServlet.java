package com.oceanview.servlet;

import com.oceanview.model.Reservation;
import com.oceanview.service.ReservationService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/reservation/search")
public class ReservationSearchServlet extends HttpServlet {
    private final ReservationService service = new ReservationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?msg=Please+login+to+continue");
            return;
        }

        try {
            String phone = req.getParameter("phone");
            String ridRaw = req.getParameter("reservationId");

            Integer rid = null;
            if (ridRaw != null && !ridRaw.trim().isEmpty()) rid = Integer.parseInt(ridRaw.trim());

            List<Reservation> results = service.search(phone, rid);
            req.setAttribute("results", results);

            // forward back to JSP to render table
            try {
                req.getRequestDispatcher("/reservation-view.jsp").forward(req, resp);
            } catch (Exception e) {
                resp.sendRedirect(req.getContextPath() + "/reservation-view.jsp?msg=Render+error");
            }
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/reservation-view.jsp?msg=Search+failed");
        }
    }
}