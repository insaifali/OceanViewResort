<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%
    String ctx = request.getContextPath();
    List<String[]> av = (List<String[]>) request.getAttribute("availability");
    if (av == null) av = new ArrayList<>();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Availability • OceanViewResort</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp" />

<div class="container">
    <div class="hero">
        <h1>Room Availability (Booked Ranges)</h1>
        <p>Shows active reservations grouped by room type with date ranges.</p>
    </div>

    <div class="card">
        <div class="card-head">
            <div>
                <h2 class="card-title">Booked Dates</h2>
                <p class="card-sub">Only ACTIVE reservations appear here.</p>
            </div>
            <a class="btn" href="<%= ctx %>/dashboard.jsp">Back</a>
        </div>
        <div class="card-body">
            <table class="table">
                <thead>
                <tr>
                    <th>Room Type</th>
                    <th>Check-in</th>
                    <th>Check-out</th>
                    <th>Guest</th>
                    <th>Reservation</th>
                </tr>
                </thead>
                <tbody>
                <% if (av.isEmpty()) { %>
                <tr><td colspan="5" style="color:var(--muted);">No active bookings.</td></tr>
                <% } else {
                    for (String[] row : av) { %>
                <tr>
                    <td><%= row[0] %></td>
                    <td><%= row[1] %></td>
                    <td><%= row[2] %></td>
                    <td><%= row[3] %></td>
                    <td><a class="btn" href="<%= ctx %>/receipt?id=<%= row[4] %>">Receipt</a></td>
                </tr>
                <% } } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jsp" />
</body>
</html>