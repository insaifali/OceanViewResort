package com.oceanview.servlet;

import com.oceanview.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final AuthService auth = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String fullName = req.getParameter("fullName");
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String role = req.getParameter("role");

            auth.register(fullName, username, password, role);

            resp.sendRedirect(req.getContextPath() + "/login.jsp?msg=Account+created.+Please+login");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/register.jsp?msg=" + url(e.getMessage()));
        }
    }

    private String url(String s) {
        try { return java.net.URLEncoder.encode(String.valueOf(s), "UTF-8"); }
        catch (Exception e) { return "Error"; }
    }
}