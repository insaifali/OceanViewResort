<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String ctx = request.getContextPath();
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");
%>
<% if ("ADMIN".equals(role) || "RECEPTIONIST".equals(role)) { %>
<a class="nav-chip" href="<%= ctx %>/availability/check">Availability</a>
<% } %>

<div class="nav">
    <div class="nav-inner">
        <div class="brand">
            <div class="brand-badge"></div>
            <div>OceanViewResort</div>
            <span class="badge"><%= role != null ? role : "GUEST" %></span>
        </div>

        <div class="nav-links">
            <a class="nav-chip" href="<%= ctx %>/dashboard.jsp">Dashboard</a>
            <a class="nav-chip" href="<%= ctx %>/reservation-form.jsp">New Booking</a>
            <a class="nav-chip" href="<%= ctx %>/reservation-view.jsp">Search / View</a>
            <a class="nav-chip" href="<%= ctx %>/availability">Availability</a>
            <a class="nav-chip" href="<%= ctx %>/help.jsp">Help</a>

            <% if (username == null) { %>
            <a class="nav-chip" href="<%= ctx %>/login.jsp">Login</a>
            <a class="nav-chip" href="<%= ctx %>/register.jsp">Register</a>
            <% } else { %>
            <span class="nav-chip" style="opacity:.9;">Signed in as <b><%= username %></b></span>
            <a class="nav-chip" href="<%= ctx %>/logout">Logout</a>
            <% } %>
        </div>
    </div>
</div>