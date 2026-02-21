<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String ctx = request.getContextPath();
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");
    if (username == null) { response.sendRedirect(ctx + "/login.jsp?msg=Please+login+to+continue"); return; }
%>
<!DOCTYPE html>
<html>
<head>
    <title>New Reservation • OceanViewResort</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jspf" />

<div class="container">
    <div class="hero">
        <h1>New Reservation</h1>
        <p>Create a booking, select payment method, and generate a receipt.</p>
    </div>

    <div class="grid">
        <div class="card span-7">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Guest & Booking Details</h2>
                    <p class="card-sub">Backend will validate dates, availability, and calculate final amount.</p>
                </div>
            </div>
            <div class="card-body">

                <!-- Backend later: POST to /reservation/create -->
                <form class="form" method="post" action="<%= ctx %>/reservation/create" id="resForm">

                    <div class="field half">
                        <label>Guest Full Name</label>
                        <input name="guestName" required placeholder="e.g., Amal Silva">
                    </div>

                    <div class="field half">
                        <label>Contact Number</label>
                        <input name="phone" required placeholder="e.g., 07XXXXXXXX" maxlength="15">
                        <div class="help">Use digits only. We'll validate in backend too.</div>
                    </div>

                    <div class="field">
                        <label>Address</label>
                        <input name="address" placeholder="Optional">
                    </div>

                    <div class="field third">
                        <label>Room Type</label>
                        <select name="roomType" id="roomType" required>
                            <option value="">Select</option>
                            <option value="SINGLE">Single</option>
                            <option value="DOUBLE">Double</option>
                            <option value="SUITE">Suite</option>
                        </select>
                        <div class="help">Rates are assumed fixed (can be changed later).</div>
                    </div>

                    <div class="field third">
                        <label>Check-in</label>
                        <input type="date" name="checkIn" id="checkIn" required>
                    </div>

                    <div class="field third">
                        <label>Check-out</label>
                        <input type="date" name="checkOut" id="checkOut" required>
                    </div>

                    <div class="field third">
                        <label>No. of Guests</label>
                        <input type="number" name="guests" min="1" max="10" value="1" required>
                    </div>

                    <div class="field">
                        <label>Special Requests</label>
                        <textarea name="notes" placeholder="Optional (e.g., late check-in, baby cot)"></textarea>
                    </div>

                    <div class="field">
                        <h3 style="margin:4px 0 10px 0;">Payment</h3>
                    </div>

                    <div class="field half">
                        <label>Payment Method</label>
                        <select name="paymentMethod" id="paymentMethod" required>
                            <option value="">Select</option>
                            <option value="CASH">Cash</option>
                            <option value="CARD">Card</option>
                            <option value="BANK_TRANSFER">Bank Transfer</option>
                        </select>
                    </div>

                    <div class="field half">
                        <label>Deposit Paid (LKR)</label>
                        <input type="number" name="deposit" id="deposit" min="0" step="0.01" value="0">
                        <div class="help">Final balance will be calculated after final amount.</div>
                    </div>

                    <div class="field half">
                        <label>Receipt Required?</label>
                        <select name="receiptRequired" required>
                            <option value="YES">Yes</option>
                            <option value="NO">No</option>
                        </select>
                    </div>

                    <div class="field half">
                        <label>Receipt Email (optional)</label>
                        <input type="email" name="receiptEmail" placeholder="e.g., guest@email.com">
                    </div>

                    <div class="field">
                        <button class="btn btn-primary" type="submit">Create Reservation</button>
                        <a class="btn" href="<%= ctx %>/dashboard.jsp">Back to Dashboard</a>
                    </div>

                </form>
            </div>
        </div>

        <div class="card span-5">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Estimate Preview</h2>
                    <p class="card-sub">This is only a preview. Backend is the source of truth.</p>
                </div>
            </div>
            <div class="card-body">
                <div class="notice" id="estimateBox">
                    Select room type + dates to see an estimate.
                </div>

                <div style="height:12px"></div>

                <div class="notice">
                    <strong>Assumed Nightly Rates</strong><br><br>
                    Single: <b>LKR 8,000</b><br>
                    Double: <b>LKR 12,000</b><br>
                    Suite: <b>LKR 20,000</b><br>
                </div>

                <div style="height:12px"></div>

                <div class="notice">
                    <strong>Logic we will enforce in backend</strong><br><br>
                    ✅ check-out must be after check-in<br>
                    ✅ prevent double booking by date overlap<br>
                    ✅ total = nights × rate<br>
                    ✅ balance = total − deposit
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    // Safe estimate only (not authoritative)
    const rates = { SINGLE: 8000, DOUBLE: 12000, SUITE: 20000 };

    function nightsBetween(a, b){
        const d1 = new Date(a), d2 = new Date(b);
        const ms = d2 - d1;
        const n = Math.ceil(ms / (1000*60*60*24));
        return (isFinite(n) && n > 0) ? n : 0;
    }

    function updateEstimate(){
        const rt = document.getElementById('roomType').value;
        const ci = document.getElementById('checkIn').value;
        const co = document.getElementById('checkOut').value;
        const dep = parseFloat(document.getElementById('deposit').value || "0");

        const box = document.getElementById('estimateBox');

        if(!rt || !ci || !co){
            box.innerHTML = "Select room type + dates to see an estimate.";
            return;
        }
        const nights = nightsBetween(ci, co);
        if(nights === 0){
            box.innerHTML = "<span class='badge danger'>Invalid dates</span><br><br>Check-out must be after check-in.";
            return;
        }
        const total = nights * rates[rt];
        const balance = Math.max(0, total - dep);

        box.innerHTML =
            "<span class='badge ok'>Estimate</span><br><br>" +
            "Nights: <b>" + nights + "</b><br>" +
            "Rate per night: <b>LKR " + rates[rt].toLocaleString() + "</b><br>" +
            "Estimated total: <b>LKR " + total.toLocaleString() + "</b><br>" +
            "Deposit: <b>LKR " + dep.toLocaleString() + "</b><br>" +
            "Estimated balance: <b>LKR " + balance.toLocaleString() + "</b>";
    }

    ["roomType","checkIn","checkOut","deposit"].forEach(id=>{
        const el = document.getElementById(id);
        if(el) el.addEventListener("input", updateEstimate);
    });
</script>

<jsp:include page="/WEB-INF/jsp/footer.jspf" />
</body>
</html>