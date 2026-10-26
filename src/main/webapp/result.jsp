
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true" %>
<%
    Integer userId = (Integer) session.getAttribute("userId");

    if (userId == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String message = (String) request.getAttribute("resultMessage");
    Boolean correct = (Boolean) request.getAttribute("answerCorrect");
    Integer xp = (Integer) request.getAttribute("earnedXP");

    if (message == null) message = "Your answer has been processed.";
    if (correct == null) correct = false;
    if (xp == null) xp = 0;
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Mission Result | CodeQuest</title>
<link rel="stylesheet"
      href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<nav>
    <div class="logo">Code<span>Quest</span></div>
    <div class="links">
        <a href="<%= request.getContextPath() %>/dashboard">Dashboard</a>
        <a href="<%= request.getContextPath() %>/missions">Missions</a>
        <a href="<%= request.getContextPath() %>/leaderboard.jsp">Leaderboard</a>
    </div>
</nav>

<main class="container">
    <section class="result-card <%= correct ? "result-correct" : "result-incorrect" %>">
        <div class="result-icon"><%= correct ? "✓" : "↻" %></div>
        <h1><%= correct ? "Mission Completed!" : "Keep Practicing!" %></h1>
        <p class="result-message"><%= message %></p>

        <% if (correct && xp > 0) { %>
            <div class="xp-earned">+<%= xp %> XP</div>
        <% } %>

        <div class="result-actions">
            <a class="btn" href="<%= request.getContextPath() %>/missions">
                Continue Missions
            </a>
            <a class="btn btn-outline"
               href="<%= request.getContextPath() %>/dashboard">
                View Dashboard
            </a>
        </div>
    </section>
</main>

<footer>CodeQuest | Learn, play, and level up.</footer>
</body>
</html>
