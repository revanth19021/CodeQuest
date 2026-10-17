
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true" %>
<%
    Integer adminId = (Integer) session.getAttribute("adminId");
    String adminUsername =
        (String) session.getAttribute("adminUsername");

    if (adminId == null || adminUsername == null) {
        response.sendRedirect(
            request.getContextPath() + "/admin-login.html");
        return;
    }

    String message = (String) session.getAttribute("questionMessage");
    String messageType =
        (String) session.getAttribute("questionMessageType");

    // Display each feedback message only once
    session.removeAttribute("questionMessage");
    session.removeAttribute("questionMessageType");

    if (messageType == null) {
        messageType = "success";
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Question | CodeQuest</title>
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">

    <style>
        .question-form {
            max-width: 760px;
            margin: 35px auto;
        }

        .form-grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 0 20px;
        }

        .full-width {
            grid-column: 1 / -1;
        }

        .notice {
            padding: 14px;
            border: 1px solid #00d9ff;
            border-radius: 8px;
            margin-bottom: 20px;
            color: #dffaff;
            background: #10253a;
        }

        .notice.error {
            border-color: #ff5470;
            color: #ffdce2;
            background: #351522;
        }

        textarea {
            resize: vertical;
            min-height: 100px;
        }

        @media (max-width: 550px) {
            .form-grid {
                grid-template-columns: 1fr;
            }

            .full-width {
                grid-column: auto;
            }
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
        <a href="<%= request.getContextPath() %>/add-question.jsp"
           class="active">Add Question</a>
        <a href="<%= request.getContextPath() %>/view-questions">
            View Questions
        </a>
        <a href="<%= request.getContextPath() %>/admin-logout">
            Logout
        </a>
    </div>
</nav>

<div class="container">

    <div class="question-form card">

        <h2 class="section-title">Create a New Mission</h2>
        <p class="muted">
            Add a question to the database for CodeQuest players.
        </p>

        <% if (message != null) { %>
            <div class="notice <%= "error".equals(messageType)
                ? "error" : "" %>">
                <%= message %>
            </div>
        <% } %>

        <form action="<%= request.getContextPath() %>/add-question"
              method="post">

            <div class="form-grid">

                <div class="full-width">
                    <label for="title">Question Title</label>
                    <input type="text" id="title" name="title"
                           placeholder="Example: Java Output Challenge"
                           maxlength="100" required>
                </div>

                <div class="full-width">
                    <label for="description">Question Description</label>
                    <textarea id="description" name="description"
                              placeholder="Enter the question or code snippet..."
                              required></textarea>
                </div>

                <div>
                    <label for="category">Category</label>
                    <select id="category" name="category" required>
                        <option value="">Select category</option>
                        <option value="Java">Java</option>
                        <option value="SQL">SQL</option>
                        <option value="DSA">DSA</option>
                    </select>
                </div>

                <div>
                    <label for="difficulty">Difficulty</label>
                    <select id="difficulty" name="difficulty" required>
                        <option value="">Select difficulty</option>
                        <option value="Easy">Easy</option>
                        <option value="Medium">Medium</option>
                        <option value="Hard">Hard</option>
                    </select>
                </div>

                <div>
                    <label for="option_a">Option A</label>
                    <input type="text" id="option_a" name="option_a"
                           maxlength="255" required>
                </div>

                <div>
                    <label for="option_b">Option B</label>
                    <input type="text" id="option_b" name="option_b"
                           maxlength="255" required>
                </div>

                <div>
                    <label for="option_c">Option C</label>
                    <input type="text" id="option_c" name="option_c"
                           maxlength="255" required>
                </div>

                <div>
                    <label for="option_d">Option D</label>
                    <input type="text" id="option_d" name="option_d"
                           maxlength="255" required>
                </div>

                <div class="full-width">
                    <label for="correct_answer">Correct Answer</label>
                    <select id="correct_answer" name="correct_answer"
                            required>
                        <option value="">Select correct option</option>
                        <option value="A">Option A</option>
                        <option value="B">Option B</option>
                        <option value="C">Option C</option>
                        <option value="D">Option D</option>
                    </select>
                </div>

                <div class="full-width">
                    <label for="xp_reward">XP Reward</label>
                    <input type="number" id="xp_reward" name="xp_reward"
                           min="1" max="1000" value="50" required>
                </div>

            </div>

            <button type="submit" class="btn">Save Question</button>

            <a href="<%= request.getContextPath() %>/admin-dashboard.jsp"
               class="btn btn-outline">Cancel</a>

        </form>
    </div>

</div>

<footer>CodeQuest | Every question creates a new challenge.</footer>

</body>
</html>
