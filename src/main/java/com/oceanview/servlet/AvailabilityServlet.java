package com.oceanview.servlet;

import com.oceanview.service.ReservationService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/availability")
public class AvailabilityServlet extends HttpServlet {
    private final ReservationService service = new ReservationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            req.setAttribute("availability", service.availability());
            req.getRequestDispatcher("/availability.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/dashboard.jsp?msg=Availability+error");
        }
    }
}