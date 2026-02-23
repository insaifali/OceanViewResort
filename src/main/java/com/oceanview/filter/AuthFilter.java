package com.oceanview.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthFilter implements Filter {

    // Pages that do NOT need login
    private static final Set<String> PUBLIC_PATHS =
            new java.util.HashSet<>(java.util.Arrays.asList(
                    "/login.jsp",
                    "/register.jsp",
                    "/login",
                    "/register",
                    "/"
            ));

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String ctx = req.getContextPath();
        String uri = req.getRequestURI();              // e.g., /OceanViewResort/dashboard.jsp
        String path = uri.substring(ctx.length());     // e.g., /dashboard.jsp

        // allow static assets and public pages
        if (isPublic(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("username") != null;

        if (!loggedIn) {
            resp.sendRedirect(ctx + "/login.jsp?msg=Please+login+to+continue");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPublic(String path) {
        // allow assets folder
        if (path.startsWith("/assets/")) return true;

        // allow exact public paths
        if (PUBLIC_PATHS.contains(path)) return true;

        // allow home
        return path.equals("") || path.equals("/");
    }
}