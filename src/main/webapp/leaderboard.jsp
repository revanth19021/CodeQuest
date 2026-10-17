```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="app.DBConnection" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>CodeQuest - Leaderboard</title>

<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    font-family: Arial, sans-serif;
    background: #0b1020;
    color: #ffffff;
}

.navbar {
    background: #151b30;
    padding: 20px 7%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #292f49;
}

.logo {
    color: #8b5cf6;
    font-size: 25px;
    font-weight: bold;
}

.navbar a {
    color: #cbd5e1;
    text-decoration: none;
    margin-left: 22px;
}

.navbar a:hover {
    color: #22d3ee;
}

.container {
    width: 90%;
    max-width: 1100px;
    margin: 45px auto;
}

h1 {
    text-align: center;
    font-size: 36px;
    margin-bottom: 12px;
}

.subtitle {
    text-align: center;
    color: #94a3b8;
    margin-bottom: 35px;
}

.leaderboard {
    background: #151b30;
    border: 1px solid #29324d;
    border-radius: 14px;
    padding: 25px;
    overflow-x: auto;
}

table {
    width: 100%;
    border-collapse: collapse;
    min-width: 550px;
}

th, td {
    padding: 17px 15px;
    text-align: left;
    border-bottom: 1px solid #29324d;
}

th {
    color: #22d3ee;
    font-size: 13px;
    text-transform: uppercase;
}

td {
    color: #e2e8f0;
}

tr:last-child td {
    border-bottom: none;
}

tr:hover {
    background: #1d2640;
}

.rank {
    font-weight: bold;
    color: #a78bfa;
}

.first {
    color: #facc15;
}

.second {
    color: #cbd5e1;
}

.third {
    color: #fb923c;
}

.xp {
    color: #22d3ee;
    font-weight: bold;
}

.empty {
    text-align: center;
    padding: 30px;
    color: #94a3b8;
}

.footer {
    text-align: center;
    padding: 25px;
    color: #64748b;
}
</style>
</head>

<body>

<div class="navbar">
    <div class="logo">CodeQuest</div>

    <div>
        <a href="index.html">Home</a>
        <a href="dashboard">Dashboard</a>
        <a href="missions">Missions</a>
        <a href="leaderboard.jsp">Leaderboard</a>
        <a href="logout">Logout</a>
    </div>
</div>

<div class="container">

    <h1>🏆 Global Leaderboard</h1>
    <p class="subtitle">
        Compete with fellow programmers and climb the rankings!
    </p>

    <div class="leaderboard">

        <table>
            <thead>
                <tr>
                    <th>Rank</th>
                    <th>Player</th>
                    <th>Level</th>
                    <th>Completed Missions</th>
                    <th>Total XP</th>
                </tr>
            </thead>

            <tbody>

            <%
                Connection con = null;
                PreparedStatement ps = null;
                ResultSet rs = null;

                try {
                    con = DBConnection.getConnection();

                    String sql =
                        "SELECT u.id, u.username, u.player_level, u.total_xp, " +
                        "COUNT(ms.id) AS completed_missions " +
                        "FROM users u " +
                        "LEFT JOIN mission_submissions ms " +
                        "ON u.id = ms.user_id AND ms.is_correct = TRUE " +
                        "GROUP BY u.id, u.username, u.player_level, u.total_xp " +
                        "ORDER BY u.total_xp DESC, completed_missions DESC, u.username ASC";

                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();

                    int rank = 1;
                    boolean hasPlayers = false;

                    while (rs.next()) {
                        hasPlayers = true;

                        String rankClass = "";
                        if (rank == 1) {
                            rankClass = "first";
                        } else if (rank == 2) {
                            rankClass = "second";
                        } else if (rank == 3) {
                            rankClass = "third";
                        }

                        String username = rs.getString("username");

                        // Escape user-controlled text before displaying it.
                        username = username.replace("&", "&amp;")
                                           .replace("<", "&lt;")
                                           .replace(">", "&gt;")
                                           .replace("\"", "&quot;")
                                           .replace("'", "&#39;");
            %>

                <tr>
                    <td class="rank <%= rankClass %>">
                        <% if (rank == 1) { %>
                            🥇
                        <% } else if (rank == 2) { %>
                            🥈
                        <% } else if (rank == 3) { %>
                            🥉
                        <% } else { %>
                            #<%= rank %>
                        <% } %>
                    </td>

                    <td><%= username %></td>

                    <td>Level <%= rs.getInt("player_level") %></td>

                    <td><%= rs.getInt("completed_missions") %></td>

                    <td class="xp">
                        <%= rs.getInt("total_xp") %> XP
                    </td>
                </tr>

            <%
                        rank++;
                    }

                    if (!hasPlayers) {
            %>

                <tr>
                    <td colspan="5" class="empty">
                        No players found. Register and start your first mission!
                    </td>
                </tr>

            <%
                    }

                } catch (Exception e) {
            %>

                <tr>
                    <td colspan="5" class="empty">
                        Unable to load the leaderboard. Please try again later.
                    </td>
                </tr>

            <%
                    e.printStackTrace();
                } finally {
                    try {
                        if (rs != null) rs.close();
                        if (ps != null) ps.close();
                        if (con != null) con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            %>

            </tbody>
        </table>

    </div>
</div>

<div class="footer">
    CodeQuest &copy; 2026 | Keep Coding. Keep Growing.
</div>

</body>
</html>
```