
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
    <title>Register | CodeQuest</title>
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
        <h2>Create Your Account</h2>
        <p class="subtitle">Join CodeQuest and begin your coding journey.</p>

        <form action="register" method="post">
            <label for="username">Username</label>
            <input type="text" id="username" name="username"
                   placeholder="Choose a username" required maxlength="50">

            <label for="email">Email Address</label>
            <input type="email" id="email" name="email"
                   placeholder="Enter your email" required maxlength="100">

            <label for="password">Password</label>
            <input type="password" id="password" name="password"
                   placeholder="Create a password" required minlength="8">

            <label for="confirmPassword">Confirm Password</label>
            <input type="password" id="confirmPassword"
                   name="confirmPassword"
                   placeholder="Re-enter your password"
                   required minlength="8">

            <button type="submit" class="btn">Create Account</button>
        </form>

        <div class="form-footer">
            Already have an account?
            <a href="login.html">Login here</a>
        </div>
    </div>
</div>

<footer>CodeQuest | Start your journey today.</footer>

</body>
</html>
