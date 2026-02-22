<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String ctx = request.getContextPath();
    String msg = request.getParameter("msg");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Login • OceanViewResort</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp" />

<div class="container">
    <div class="hero">
        <h1>Sign in</h1>
        <p>Access the reservation dashboard based on your role.</p>
    </div>

    <% if (msg != null) { %>
    <div class="notice"><strong>Note:</strong> <%= msg %></div>
    <% } %>

    <div class="grid">
        <div class="card span-6">
            <div class="card-head">
                <div>
                    <h2 class="card-title">Login</h2>
                    <p class="card-sub">Next step: AuthServlet validates credentials and starts a session.</p>
                </div>
            </div>
            <div class="card-body">
                <!-- Backend later: action="<%= ctx %>/login" -->
                <form class="form" method="post" action="<%= ctx %>/login">
                    <div class="field half">
                        <label>Username</label>
                        <input name="username" required placeholder="e.g., admin1">
                    </div>

                    <div class="field half">
                        <label>Password</label>
                        <input type="password" name="password" required placeholder="Your password">
                    </div>

                    <div class="field half">
                        <label>Role</label>
                        <select name="role" required>
                            <option value="">Select role</option>
                            <option value="ADMIN">Admin</option>
                            <option value="RECEPTIONIST">Receptionist</option>
                            <option value="USER">User</option>
                        </select>
                        <div class="help">Role is used only for routing now; final role comes from DB.</div>
                    </div>

                    <div class="field">
                        <button class="btn btn-primary" type="submit">Sign In</button>
                        <a class="btn" href="<%= ctx %>/register.jsp">Create an account</a>
                    </div>
                </form>
            </div>
        </div>

        <div class="card span-6">
            <div class="card-head">
                <div>
                    <h2 class="card-title">After login</h2>
                    <p class="card-sub">Session fields we will use everywhere.</p>
                </div>
            </div>
            <div class="card-body">
                <div class="notice">
                    We will store these in session:<br><br>
                    <strong>username</strong>, <strong>role</strong><br><br>
                    Then every page checks session before showing data.
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jsp" />
</body>
</html>