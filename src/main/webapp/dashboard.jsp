
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true" %>
<%
    Integer userId = (Integer) session.getAttribute("userId");

    if (userId == null) {
        response.sendRedirect(
            request.getContextPath() + "/login.html");
        return;
    }

    String username = (String) request.getAttribute("username");
    Integer totalXP = (Integer) request.getAttribute("totalXP");
    Integer playerLevel = (Integer) request.getAttribute("playerLevel");
    Integer completedMissions =
        (Integer) request.getAttribute("completedMissions");

    // Direct URL access should retrieve fresh database values.
    if (username == null || totalXP == null
            || playerLevel == null || completedMissions == null) {
        response.sendRedirect(request.getContextPath() + "/dashboard");
        return;
    }

    int xpWithinLevel = totalXP % 500;
    int progress = (xpWithinLevel * 100) / 500;
    int xpRemaining = 500 - xpWithinLevel;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Player Dashboard | CodeQuest</title>
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">

    <style>
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(190px, 1fr));
            gap: 20px;
            margin: 30px 0;
        }

        .stat-number {
            font-size: 32px;
            color: #00d9ff;
            margin: 12px 0;
        }

        .progress-track {
            width: 100%;
            height: 12px;
            background: #25345c;
            border-radius: 20px;
            overflow: hidden;
            margin: 18px 0 10px;
        }

        .progress-fill {
            height: 100%;
            background: linear-gradient(90deg, #8b35ff, #00d9ff);
            border-radius: 20px;
        }

        .welcome-card {
            background: radial-gradient(circle at top right,
                #30205d, #10162b 65%);
        }
    </style>
</head>
<body>

<nav>
    <div class="logo">Code<span>Quest</span></div>
    <div class="links">
        <a href="<%= request.getContextPath() %>/dashboard"
           class="active">Dashboard</a>
        <a href="<%= request.getContextPath() %>/missions">Missions</a>
        <a href="<%= request.getContextPath() %>/leaderboard.jsp">
            Leaderboard
        </a>
        <a href="<%= request.getContextPath() %>/logout">Logout</a>
    </div>
</nav>

<div class="container">

    <div class="card welcome-card">
        <p class="gradient">YOUR CODING JOURNEY</p>

        <h1 style="margin:15px 0;">
            Welcome back, <%= username %>!
        </h1>

        <p class="muted">
            Every challenge brings you closer to becoming a coding champion.
        </p>

        <br>

        <a href="<%= request.getContextPath() %>/missions"
           class="btn">Continue Your Quest</a>
    </div>

    <div class="stats-grid">

        <div class="card">
            <div class="card-icon">&#9889;</div>
            <p class="muted">Total Experience</p>
            <h2 class="stat-number"><%= totalXP %> XP</h2>
        </div>

        <div class="card">
            <div class="card-icon">&#9733;</div>
            <p class="muted">Current Level</p>
            <h2 class="stat-number"><%= playerLevel %></h2>
        </div>

        <div class="card">
            <div class="card-icon">&#10003;</div>
            <p class="muted">Completed Missions</p>
            <h2 class="stat-number"><%= completedMissions %></h2>
        </div>

    </div>

    <div class="card">
        <h2>Level Progress</h2>

        <p class="muted">
            <%= xpWithinLevel %> / 500 XP toward your next level
        </p>

        <div class="progress-track">
            <div class="progress-fill"
                 style="width:<%= progress %>%;"></div>
        </div>

        <p class="muted">
            Earn <%= xpRemaining %> more XP to reach the next level.
        </p>
    </div>

    <h2 class="section-title" style="margin-top:35px;">
        Ready for Your Next Mission?
    </h2>

    <div class="features">

        <div class="card">
            <div class="card-icon">&lt;/&gt;</div>
            <h3>Java Missions</h3>
            <p>Strengthen your Java fundamentals and output prediction skills.</p>
            <br>
            <a href="<%= request.getContextPath() %>/missions"
               class="btn">Explore Missions</a>
        </div>

        <div class="card">
            <div class="card-icon">&#9638;</div>
            <h3>SQL Challenges</h3>
            <p>Practice database concepts through interactive questions.</p>
            <br>
            <a href="<%= request.getContextPath() %>/missions"
               class="btn">Start Learning</a>
        </div>

        <div class="card">
            <div class="card-icon">&#127942;</div>
            <h3>Compete</h3>
            <p>Keep earning XP and work your way up the leaderboard.</p>
            <br>
            <a href="<%= request.getContextPath() %>/leaderboard.jsp"
               class="btn btn-outline">View Rankings</a>
        </div>

    </div>

</div>

<footer>CodeQuest | Every line of code takes you forward.</footer>

</body>
</html>
