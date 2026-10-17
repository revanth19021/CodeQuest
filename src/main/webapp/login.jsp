
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%
    String message = (String) request.getAttribute("message");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | CodeQuest</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<nav>
    <div class="logo">Code<span>Quest</span></div>
    <div class="links">
        <a href="index.html">Home</a>
        <a href="login.jsp">Login</a>
        <a href="register.jsp" class="active">Register</a>
        <a href="admin-login.html">Admin</a>
    </div>
</nav>

<div class="container">
    <div class="form-card">
        <h2>Welcome Back!</h2>
        <p class="subtitle">Login to continue your coding adventure.</p>

        <form action="login" method="post">
            <label for="email">Email or Username</label>
            <input type="text" id="email" name="email"
                   placeholder="Enter email or username" required>

            <label for="password">Password</label>
            <input type="password" id="password" name="password"
                   placeholder="Enter your password" required>

            <button type="submit" class="btn">Enter CodeQuest</button>
        </form>

        <div class="form-footer">
            New player?
            <a href="register.jsp">Create an account</a>
            <br>
            Administrator?
            <a href="admin-login.html">Admin Login</a>
        </div>
    </div>
</div>

<footer>CodeQuest | Your next achievement awaits.</footer>

</body>
</html>
