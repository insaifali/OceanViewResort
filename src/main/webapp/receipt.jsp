<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String ctx = request.getContextPath();
    String username = (String) session.getAttribute("username");
    if (username == null) { response.sendRedirect(ctx + "/login.jsp?msg=Please+login+to+continue"); return; }

    // Backend later will set these attributes:
    String rid = (String) request.getAttribute("rid");
    String guest = (String) request.getAttribute("guest");
    String phone = (String) request.getAttribute("phone");
    String room = (String) request.getAttribute("roomType");
    String checkIn = (String) request.getAttribute("checkIn");
    String checkOut = (String) request.getAttribute("checkOut");
    String nights = (String) request.getAttribute("nights");
    String total = (String) request.getAttribute("total");
    String deposit = (String) request.getAttribute("deposit");
    String balance = (String) request.getAttribute("balance");
    String payMethod = (String) request.getAttribute("paymentMethod");

    // UI fallback placeholders
    if (rid == null) rid = "—";
    if (guest == null) guest = "—";
    if (phone == null) phone = "—";
    if (room == null) room = "—";
    if (checkIn == null) checkIn = "—";
    if (checkOut == null) checkOut = "—";
    if (nights == null) nights = "—";
    if (total == null) total = "—";
    if (deposit == null) deposit = "—";
    if (balance == null) balance = "—";
    if (payMethod == null) payMethod = "—";
%>
<!DOCTYPE html>
<html>
<head>
    <title>Receipt • OceanViewResort</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/style.css">
    <style>
        @media print {
            .nav, .footer, .no-print { display:none !important; }
            body{ background:#fff !important; color:#000 !important; }
            .card{ box-shadow:none !important; border:1px solid #ddd !important; }
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp" />

<div class="container">
    <div class="hero">
        <h1>Receipt</h1>
        <p>Printable confirmation for the guest. Generated after booking/payment.</p>
    </div>

    <div class="grid">
        <div class="card span-7">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Ocean View Resort • Booking Receipt</h2>
                    <p class="card-sub">Reservation ID: <b><%= rid %></b></p>
                </div>
                <span class="badge ok">CONFIRMED</span>
            </div>
            <div class="card-body">
                <table class="table">
                    <tbody>
                    <tr><th>Guest Name</th><td><%= guest %></td></tr>
                    <tr><th>Contact</th><td><%= phone %></td></tr>
                    <tr><th>Room Type</th><td><%= room %></td></tr>
                    <tr><th>Check-in</th><td><%= checkIn %></td></tr>
                    <tr><th>Check-out</th><td><%= checkOut %></td></tr>
                    <tr><th>Nights</th><td><%= nights %></td></tr>
                    <tr><th>Payment Method</th><td><%= payMethod %></td></tr>
                    </tbody>
                </table>

                <div style="height:14px"></div>

                <table class="table">
                    <tbody>
                    <tr><th>Total Amount (LKR)</th><td><b><%= total %></b></td></tr>
                    <tr><th>Deposit Paid (LKR)</th><td><%= deposit %></td></tr>
                    <tr><th>Balance (LKR)</th><td><b><%= balance %></b></td></tr>
                    </tbody>
                </table>

                <div style="height:14px"></div>

                <div class="notice">
                    <strong>Handled by:</strong> <%= username %><br>
                    <strong>Generated on:</strong> <%= java.time.LocalDateTime.now() %>
                </div>

                <div style="height:14px"></div>

                <div class="no-print">
                    <button class="btn btn-primary" onclick="window.print()">Print / Save as PDF</button>
                    <a class="btn" href="<%= ctx %>/reservation-view.jsp">Back</a>
                </div>
            </div>
        </div>

        <div class="card span-5">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Notes</h2>
                    <p class="card-sub">Important assumptions (keep it simple).</p>
                </div>
            </div>
            <div class="card-body">
                <div class="notice">
                    ✅ Final totals are calculated by backend service.<br>
                    ✅ Receipt uses booking details stored in DB.<br>
                    ✅ Payment status can be: Unpaid / Partial / Paid (next step).
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jsp" />
</body>
</html>