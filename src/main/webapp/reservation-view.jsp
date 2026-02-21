<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String ctx = request.getContextPath();
    String username = (String) session.getAttribute("username");
    if (username == null) { response.sendRedirect(ctx + "/login.jsp?msg=Please+login+to+continue"); return; }

    // Later: backend will set these attributes
    // request.setAttribute("results", list);
%>
<!DOCTYPE html>
<html>
<head>
    <title>Search / View • OceanViewResort</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jspf" />

<div class="container">
    <div class="hero">
        <h1>Search / View Reservations</h1>
        <p>Search by contact number or reservation ID. Backend will load results.</p>
    </div>

    <div class="grid">
        <div class="card span-6">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Search</h2>
                    <p class="card-sub">Next step: ReservationServlet returns matching rows from DB.</p>
                </div>
            </div>
            <div class="card-body">
                <form class="form" method="get" action="<%= ctx %>/reservation/search">
                    <div class="field half">
                        <label>Contact Number</label>
                        <input name="phone" placeholder="e.g., 07XXXXXXXX">
                    </div>
                    <div class="field half">
                        <label>Reservation ID</label>
                        <input name="reservationId" placeholder="e.g., 1024">
                    </div>
                    <div class="field">
                        <button class="btn btn-primary" type="submit">Search</button>
                        <a class="btn" href="<%= ctx %>/reservation/search">View All</a>
                    </div>
                </form>
            </div>
        </div>

        <div class="card span-6">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Actions</h2>
                    <p class="card-sub">Receipt printing & cancellation later.</p>
                </div>
            </div>
            <div class="card-body">
                <div class="notice">
                    After search, each row will have:<br><br>
                    ✅ View receipt<br>
                    ✅ Cancel reservation (admin/receptionist)<br>
                    ✅ Payment status (Paid/Partial/Unpaid)
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Results</h2>
                    <p class="card-sub">For now this is a UI template. Backend will populate it.</p>
                </div>
            </div>
            <div class="card-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Guest</th>
                        <th>Phone</th>
                        <th>Room</th>
                        <th>Dates</th>
                        <th>Total (LKR)</th>
                        <th>Payment</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td colspan="8" style="color: var(--muted);">No results yet. Run a search once backend is ready.</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jspf" />
</body>
</html>