package com.oceanview.servlet;

import com.oceanview.model.AvailabilityResult;
import com.oceanview.service.ReservationService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/availability/check")
public class AvailabilityCheckServlet extends HttpServlet {
    private final ReservationService service = new ReservationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String fromRaw = req.getParameter("from");
            String toRaw = req.getParameter("to");

            if (fromRaw == null || toRaw == null || fromRaw.trim().isEmpty() || toRaw.trim().isEmpty()) {
                req.getRequestDispatcher("/availability.jsp").forward(req, resp);
                return;
            }

            LocalDate from = LocalDate.parse(fromRaw);
            LocalDate to = LocalDate.parse(toRaw);

            List<AvailabilityResult> results = service.checkAvailability(from, to);
            req.setAttribute("from", fromRaw);
            req.setAttribute("to", toRaw);
            req.setAttribute("results", results);

            req.getRequestDispatcher("/availability.jsp").forward(req, resp);

        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/availability.jsp?msg=" + url(e.getMessage()));
        }
    }

    private String url(String s) {
        try { return java.net.URLEncoder.encode(String.valueOf(s), "UTF-8"); }
        catch (Exception e) { return "Error"; }
    }
}