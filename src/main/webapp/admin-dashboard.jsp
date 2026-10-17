
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true" %>
<%
    // Check whether an administrator is logged in
    Integer adminId = (Integer) session.getAttribute("adminId");
    String adminUsername =
        (String) session.getAttribute("adminUsername");

    if (adminId == null || adminUsername == null) {
        response.sendRedirect(
            request.getContextPath() + "/admin-login.html");
        return;
    }

    String appName = (String) application.getAttribute("appName");

    if (appName == null) {
        appName = "CodeQuest";
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard | CodeQuest</title>
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<nav>
    <div class="logo">Code<span>Quest</span></div>

    <div class="links">
        <a href="<%= request.getContextPath() %>/admin-dashboard.jsp"
           class="active">Dashboard</a>
        <a href="<%= request.getContextPath() %>/add-question.jsp">
            Add Question
        </a>
        <a href="<%= request.getContextPath() %>/view-questions">
            View Questions
        </a>
        <a href="<%= request.getContextPath() %>/admin-logout">
            Logout
        </a>
    </div>
</nav>

<div class="container">

    <section class="hero">
        <div class="hero-content">
            <p class="gradient">ADMIN CONTROL CENTER</p>

            <h1>
                Welcome back,<br>
                <span class="gradient"><%= adminUsername %></span>
            </h1>

            <p>
                Manage programming challenges, create questions,
                and help players level up their coding skills.
            </p>

            <a href="<%= request.getContextPath() %>/add-question.jsp"
               class="btn">+ Create Question</a>

            <a href="<%= request.getContextPath() %>/view-questions"
               class="btn btn-outline">Manage Questions</a>
        </div>

        <div class="hero-art">
            <div class="symbol">&#9881;</div>
            <h2>QUEST MASTER</h2>
            <p class="muted">Control the challenges. Shape the quest.</p>
        </div>
    </section>

    <h2 class="section-title">Administration</h2>
    <p class="muted">Manage your CodeQuest learning platform.</p>

    <div class="features">

        <div class="card">
            <div class="card-icon">&#9998;</div>
            <h3>Create Questions</h3>
            <p>
                Add Java, SQL, and DSA questions with options,
                correct answers, difficulty, and XP rewards.
            </p>
            <br>
            <a href="<%= request.getContextPath() %>/add-question.jsp"
               class="btn">Add Question</a>
        </div>

        <div class="card">
            <div class="card-icon">&#9776;</div>
            <h3>Question Library</h3>
            <p>
                Review the questions saved in the database.
                Newly created questions will be available to users
                when they retrieve the mission list.
            </p>
            <br>
            <a href="<%= request.getContextPath() %>/view-questions"
               class="btn">View Questions</a>
        </div>

        <div class="card">
            <div class="card-icon">&#9733;</div>
            <h3>Game Content</h3>
            <p>
                Build programming challenges across multiple
                categories and difficulty levels.
            </p>
            <br>
            <a href="<%= request.getContextPath() %>/add-question.jsp"
               class="btn btn-outline">Build a Quest</a>
        </div>

    </div>

    <div class="card" style="margin-top:25px;">
        <h3>Administrator Information</h3>
        <p class="muted">
            Application: <%= appName %>
        </p>
        <p class="muted">
            Signed in as: <%= adminUsername %>
        </p>
        <p class="muted">
            Your administrator session is active.
        </p>
    </div>

</div>

<footer>
    CodeQuest Admin Portal | Manage. Create. Inspire.
</footer>

</body>
</html>
