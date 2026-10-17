
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"
    import="java.util.List" %>
<%
    Integer userId = (Integer) session.getAttribute("userId");

    if (userId == null) {
        response.sendRedirect(
            request.getContextPath() + "/login.html");
        return;
    }

    String username = (String) session.getAttribute("username");

    List<String[]> missions =
        (List<String[]>) request.getAttribute("missions");

    if (missions == null) {
        response.sendRedirect(request.getContextPath() + "/missions");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Coding Missions | CodeQuest</title>
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">

    <style>
        .mission-card {
            margin-top: 22px;
        }

        .mission-meta {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            margin: 15px 0;
        }

        .tag {
            background: #272047;
            color: #c8a6ff;
            border: 1px solid #493475;
            border-radius: 6px;
            padding: 6px 10px;
            font-size: 12px;
        }

        .option {
            display: block;
            padding: 12px;
            border: 1px solid #25345c;
            background: #080f22;
            border-radius: 8px;
            margin: 10px 0;
            cursor: pointer;
        }

        .option input {
            width: auto;
            margin-right: 10px;
        }

        .option:hover {
            border-color: #00d9ff;
        }

        .empty {
            margin-top: 25px;
            text-align: center;
        }
    </style>
</head>
<body>

<nav>
    <div class="logo">Code<span>Quest</span></div>
    <div class="links">
        <a href="<%= request.getContextPath() %>/dashboard">
            Dashboard
        </a>
        <a href="<%= request.getContextPath() %>/missions"
           class="active">Missions</a>
        <a href="<%= request.getContextPath() %>/leaderboard.jsp">
            Leaderboard
        </a>
        <a href="<%= request.getContextPath() %>/logout">Logout</a>
    </div>
</nav>

<div class="container">

    <h1 class="section-title">Coding Missions</h1>
    <p class="muted">
        Welcome, <%= username == null ? "Player" : username %>!
        Choose a challenge and earn XP.
    </p>

    <% if (missions.isEmpty()) { %>

        <div class="card empty">
            <h2>No Missions Available</h2>
            <p class="muted">
                New challenges will appear here when the admin adds them.
            </p>
        </div>

    <% } else { %>

        <% for (String[] m : missions) { %>

            <div class="card mission-card">
                <h2><%= m[1] %></h2>

                <div class="mission-meta">
                    <span class="tag"><%= m[3] %></span>
                    <span class="tag"><%= m[4] %></span>
                    <span class="tag"><%= m[5] %> XP</span>
                </div>

                <p class="muted"><%= m[2] %></p>

                <form action="<%= request.getContextPath() %>/submit-answer"
                      method="post">

                    <input type="hidden" name="missionId"
                           value="<%= m[0] %>">

                    <label class="option">
                        <input type="radio" name="answer" value="A" required>
                        A. <%= m[6] %>
                    </label>

                    <label class="option">
                        <input type="radio" name="answer" value="B">
                        B. <%= m[7] %>
                    </label>

                    <label class="option">
                        <input type="radio" name="answer" value="C">
                        C. <%= m[8] %>
                    </label>

                    <label class="option">
                        <input type="radio" name="answer" value="D">
                        D. <%= m[9] %>
                    </label>

                    <button type="submit" class="btn">
                        Submit Answer
                    </button>
                </form>
            </div>

        <% } %>

    <% } %>

</div>

<footer>CodeQuest | Learn, play, and level up.</footer>

</body>
</html>
