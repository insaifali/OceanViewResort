package com.oceanview.servlet;

import com.oceanview.model.User;
import com.oceanview.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService auth = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            User u = auth.login(username, password);
            if (u == null) {
                resp.sendRedirect(req.getContextPath() + "/login.jsp?msg=Invalid+credentials");
                return;
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("username", u.username);
            session.setAttribute("role", u.role);

            resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?msg=Login+failed");
        }
    }
}