
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"
    import="java.util.List" %>
<%
    Integer adminId = (Integer) session.getAttribute("adminId");
    String adminUsername =
        (String) session.getAttribute("adminUsername");

    if (adminId == null || adminUsername == null) {
        response.sendRedirect(
            request.getContextPath() + "/admin-login.html");
        return;
    }

    List<String[]> questions =
        (List<String[]>) request.getAttribute("questions");

    if (questions == null) {
        response.sendRedirect(
            request.getContextPath() + "/view-questions");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Question Library | CodeQuest</title>
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">

    <style>
        .table-wrap {
            overflow-x: auto;
            margin-top: 25px;
            border: 1px solid #25345c;
            border-radius: 12px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 850px;
            background: #10162b;
        }

        th, td {
            padding: 15px;
            border-bottom: 1px solid #25345c;
            text-align: left;
            font-size: 13px;
            vertical-align: top;
        }

        th {
            color: #00d9ff;
            background: #0b1225;
        }

        td {
            color: #dce5ff;
        }

        .tag {
            display: inline-block;
            padding: 5px 9px;
            border-radius: 6px;
            background: #272047;
            color: #c8a6ff;
        }

        .empty {
            text-align: center;
            padding: 45px 20px;
        }

        .answer {
            color: #60e8a3;
            font-weight: bold;
        }
    </style>
</head>
<body>

<nav>
    <div class="logo">Code<span>Quest</span></div>
    <div class="links">
        <a href="<%= request.getContextPath() %>/admin-dashboard.jsp">
            Dashboard
        </a>
        <a href="<%= request.getContextPath() %>/add-question.jsp">
            Add Question
        </a>
        <a href="<%= request.getContextPath() %>/view-questions"
           class="active">View Questions</a>
        <a href="<%= request.getContextPath() %>/admin-logout">
            Logout
        </a>
    </div>
</nav>

<div class="container">

    <h1 class="section-title">Question Library</h1>

    <p class="muted">
        Welcome, <%= adminUsername %>.
        Questions below are retrieved directly from MySQL.
    </p>

    <br>

    <a href="<%= request.getContextPath() %>/add-question.jsp"
       class="btn">+ Add New Question</a>

    <% if (questions.isEmpty()) { %>

        <div class="card empty" style="margin-top:25px;">
            <h2>No Questions Yet</h2>
            <p class="muted">
                Create your first Java, SQL, or DSA challenge.
            </p>
            <br>
            <a href="<%= request.getContextPath() %>/add-question.jsp"
               class="btn">Create First Question</a>
        </div>

    <% } else { %>

        <p class="muted" style="margin-top:25px;">
            Total questions: <%= questions.size() %>
        </p>

        <div class="table-wrap">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Category</th>
                        <th>Difficulty</th>
                        <th>XP</th>
                        <th>Options</th>
                        <th>Correct Answer</th>
                    </tr>
                </thead>

                <tbody>
                <% for (String[] q : questions) { %>
                    <tr>
                        <td><%= q[0] %></td>
                        <td><%= q[1] %></td>
                        <td><span class="tag"><%= q[2] %></span></td>
                        <td><%= q[3] %></td>
                        <td><%= q[4] %> XP</td>
                        <td>
                            A. <%= q[5] %><br><br>
                            B. <%= q[6] %><br><br>
                            C. <%= q[7] %><br><br>
                            D. <%= q[8] %>
                        </td>
                        <td class="answer"><%= q[9] %></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>

    <% } %>

</div>

<footer>CodeQuest | Your question library.</footer>

</body>
</html>
