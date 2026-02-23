package com.oceanview.servlet;

import com.oceanview.service.ReservationService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/payment/add")
public class PaymentAddServlet extends HttpServlet {
    private final ReservationService service = new ReservationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String idRaw = req.getParameter("id");
            int id = Integer.parseInt(idRaw.replaceAll("\\D", ""));
            double amount = Double.parseDouble(req.getParameter("amount"));

            boolean ok = service.addPayment(id, amount);
            if (ok) {
                resp.sendRedirect(req.getContextPath() + "/receipt?id=" + id + "&msg=Payment+updated");
            } else {
                resp.sendRedirect(req.getContextPath() + "/receipt?id=" + id + "&msg=Payment+failed");
            }
        } catch (Exception e) {
            e.printStackTrace(); // shows real error in IntelliJ console
            resp.sendRedirect(req.getContextPath() + "/reservation-view.jsp?msg=" + url("Payment error: " + e.getMessage()));
        }
    }
    private String url(String s) {
        try {
            return java.net.URLEncoder.encode(String.valueOf(s), "UTF-8");
        } catch (Exception e) {
            return "Error";
        }
    }
}