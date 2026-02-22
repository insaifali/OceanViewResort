<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Help • OceanViewResort</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp" />

<div class="container">
    <div class="hero">
        <h1>Help</h1>
        <p>Quick guide for staff to use the system.</p>
    </div>

    <div class="grid">
        <div class="card span-6">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Workflow</h2>
                    <p class="card-sub">Simple and correct steps.</p>
                </div>
            </div>
            <div class="card-body">
                <div class="notice">
                    1) Login<br>
                    2) Create reservation<br>
                    3) Select payment method + deposit<br>
                    4) Generate receipt<br>
                    5) Search reservations anytime by phone/ID
                </div>
            </div>
        </div>

        <div class="card span-6">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Rules (logic)</h2>
                    <p class="card-sub">Enforced by backend in next step.</p>
                </div>
            </div>
            <div class="card-body">
                <div class="notice">
                    ✅ Check-out must be after check-in<br>
                    ✅ Prevent overlapping bookings for the same room/room type<br>
                    ✅ Total = nights × rate<br>
                    ✅ Balance = total − deposit
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jsp" />
</body>
</html>