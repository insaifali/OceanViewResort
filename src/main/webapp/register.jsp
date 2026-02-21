<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String ctx = request.getContextPath();
    String msg = request.getParameter("msg");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Register • OceanViewResort</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jspf" />

<div class="container">
    <div class="hero">
        <h1>Create an account</h1>
        <p>Role-based access for staff. Keep credentials private.</p>
    </div>

    <% if (msg != null) { %>
    <div class="notice"><strong>Note:</strong> <%= msg %></div>
    <% } %>

    <div class="grid">
        <div class="card span-6">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Register</h2>
                    <p class="card-sub">This will be handled by a RegisterServlet in the next step.</p>
                </div>
            </div>
            <div class="card-body">
                <!-- Backend later: action="<%= ctx %>/register" -->
                <form class="form" method="post" action="<%= ctx %>/register">
                    <div class="field half">
                        <label>Full Name</label>
                        <input name="fullName" required placeholder="e.g., N. Perera">
                    </div>

                    <div class="field half">
                        <label>Username</label>
                        <input name="username" required placeholder="e.g., receptionist1">
                    </div>

                    <div class="field half">
                        <label>Password</label>
                        <input type="password" name="password" required placeholder="Create a strong password">
                        <div class="help">We will store a hashed password in DB (not plain text).</div>
                    </div>

                    <div class="field half">
                        <label>Role</label>
                        <select name="role" required>
                            <option value="">Select role</option>
                            <option value="RECEPTIONIST">Receptionist</option>
                            <option value="USER">User</option>
                            <option value="ADMIN">Admin</option>
                        </select>
                        <div class="help">Assumption: Admin can create other roles (we’ll enforce later).</div>
                    </div>

                    <div class="field">
                        <button class="btn btn-primary" type="submit">Create Account</button>
                        <a class="btn" href="<%= ctx %>/login.jsp">I already have an account</a>
                    </div>
                </form>
            </div>
        </div>

        <div class="card span-6">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Role permissions (simple)</h2>
                    <p class="card-sub">We’ll document these assumptions in your report.</p>
                </div>
            </div>
            <div class="card-body">
                <div class="notice">
                    <strong>ADMIN</strong> — manage users, view all bookings, cancel bookings.<br><br>
                    <strong>RECEPTIONIST</strong> — create bookings, search bookings, generate receipts.<br><br>
                    <strong>USER</strong> — create bookings and view/search own bookings (enforced later).
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jspf" />
</body>
</html>