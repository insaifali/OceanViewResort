<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.oceanview.model.AvailabilityResult" %>
<%
    String ctx = request.getContextPath();
    String msg = request.getParameter("msg");
    String from = (String) request.getAttribute("from");
    String to = (String) request.getAttribute("to");
    List<AvailabilityResult> results = (List<AvailabilityResult>) request.getAttribute("results");
    if (results == null) results = new ArrayList<>();
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
        <h1>Room Availability</h1>
        <p>Select dates to check availability by room type and view overlapping bookings.</p>
    </div>

    <% if (msg != null) { %>
    <div class="notice"><strong>Message:</strong> <%= msg %></div>
    <% } %>

    <div class="grid">
        <div class="card span-6">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Check Availability</h2>
                    <p class="card-sub">Availability = capacity − overlapping active bookings.</p>
                </div>
            </div>
            <div class="card-body">
                <form class="form" method="get" action="<%= ctx %>/availability/check">
                    <div class="field half">
                        <label>From (Check-in)</label>
                        <input type="date" name="from" required value="<%= from != null ? from : "" %>">
                    </div>
                    <div class="field half">
                        <label>To (Check-out)</label>
                        <input type="date" name="to" required value="<%= to != null ? to : "" %>">
                    </div>
                    <div class="field">
                        <button class="btn btn-primary" type="submit">Check</button>
                        <a class="btn" href="<%= ctx %>/dashboard.jsp">Back</a>
                    </div>
                </form>
            </div>
        </div>

        <div class="card span-6">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Overlap Logic</h2>
                    <p class="card-sub">Used for both availability and booking prevention.</p>
                </div>
            </div>
            <div class="card-body">
                <div class="notice">
                    Overlap exists if:<br><br>
                    <strong>newFrom &lt; existingCheckOut</strong> AND <strong>newTo &gt; existingCheckIn</strong><br><br>
                    ✅ If overlaps count ≥ capacity → <b>Not Available</b>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Results</h2>
                    <p class="card-sub"><%= (from != null && to != null) ? ("For " + from + " → " + to) : "Select dates to view results." %></p>
                </div>
            </div>
            <div class="card-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Room Type</th>
                        <th>Capacity</th>
                        <th>Booked (Overlaps)</th>
                        <th>Available</th>
                        <th>Status</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (results.isEmpty()) { %>
                    <tr><td colspan="5" style="color:var(--muted);">No results yet.</td></tr>
                    <% } else {
                        for (AvailabilityResult r : results) { %>
                    <tr>
                        <td><b><%= r.roomType %></b></td>
                        <td><%= r.capacity %></td>
                        <td><%= r.bookedCount %></td>
                        <td><%= r.availableCount %></td>
                        <td>
                <span class="badge <%= r.available ? "ok" : "danger" %>">
                  <%= r.available ? "AVAILABLE" : "NOT AVAILABLE" %>
                </span>
                        </td>
                    </tr>

                    <% if (!r.bookings.isEmpty()) { %>
                    <tr>
                        <td colspan="5" style="background: rgba(255,255,255,.02);">
                            <div class="notice">
                                <strong>Overlapping bookings for <%= r.roomType %>:</strong><br><br>
                                <% for (String[] b : r.bookings) { %>
                                • ID <b><%= b[0] %></b> — <%= b[1] %> (<%= b[2] %> → <%= b[3] %>)
                                <a class="nav-chip" style="margin-left:10px;" href="<%= ctx %>/receipt?id=<%= b[0] %>">Receipt</a>
                                <br>
                                <% } %>
                            </div>
                        </td>
                    </tr>
                    <% } %>

                    <% } } %>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jsp" />
</body>
</html>