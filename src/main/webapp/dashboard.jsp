<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String ctx = request.getContextPath();
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");

    if (username == null) {
        response.sendRedirect(ctx + "/login.jsp?msg=Please+login+to+continue");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard • OceanViewResort</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jspf" />

<div class="container">
    <div class="hero">
        <h1>Welcome, <%= username %></h1>
        <p>Role-based dashboard. Only relevant actions appear for your role.</p>
    </div>

    <div class="grid">
        <div class="card span-4">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Quick Actions</h2>
                    <p class="card-sub">Start from the most common tasks.</p>
                </div>
            </div>
            <div class="card-body">
                <a class="btn btn-primary" href="<%= ctx %>/reservation-form.jsp">+ New Reservation</a>
                <a class="btn" href="<%= ctx %>/reservation-view.jsp">Search / View</a>
            </div>
        </div>

        <div class="card span-4">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Your Role</h2>
                    <p class="card-sub">Access is controlled by session role.</p>
                </div>
                <span class="badge ok"><%= role %></span>
            </div>
            <div class="card-body">
                <div class="notice">
                    <strong>Note:</strong> In the next step, role will be validated from the DB.
                </div>
            </div>
        </div>

        <div class="card span-4">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Today</h2>
                    <p class="card-sub">This will be dynamic later.</p>
                </div>
            </div>
            <div class="card-body">
                <div class="kpi">
                    <strong>—</strong>
                    <span>Reservations created today</span>
                </div>
            </div>
        </div>

        <% if ("ADMIN".equals(role)) { %>
        <div class="card span-6">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Admin Panel</h2>
                    <p class="card-sub">Manage users and view all reservations (next step).</p>
                </div>
                <span class="badge warn">ADMIN</span>
            </div>
            <div class="card-body">
                <div class="notice">
                    Admin features to be added: user management, full report export.
                </div>
            </div>
        </div>
        <% } %>

        <% if ("RECEPTIONIST".equals(role) || "ADMIN".equals(role)) { %>
        <div class="card span-6">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Front Desk</h2>
                    <p class="card-sub">Create bookings, receive payment, generate receipt.</p>
                </div>
                <span class="badge ok">STAFF</span>
            </div>
            <div class="card-body">
                <div class="notice">
                    Recommended workflow: Create reservation → Confirm payment → Print receipt.
                </div>
            </div>
        </div>
        <% } %>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jspf" />
</body>
</html>