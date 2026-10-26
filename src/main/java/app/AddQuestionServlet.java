
package app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/add-question")
public class AddQuestionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("adminId") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/admin-login.html");
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String category = request.getParameter("category");
        String difficulty = request.getParameter("difficulty");
        String questionType = request.getParameter("question_type");
        String xpValue = request.getParameter("xp_reward");

        String optionA = request.getParameter("option_a");
        String optionB = request.getParameter("option_b");
        String optionC = request.getParameter("option_c");
        String optionD = request.getParameter("option_d");

        String correctAnswer = request.getParameter("correct_answer");
        String expectedAnswer = request.getParameter("expected_answer");
        String starterCode = request.getParameter("starter_code");
        String testCode = request.getParameter("test_code");

        if (isBlank(title) || isBlank(description)
                || isBlank(category) || isBlank(difficulty)
                || isBlank(questionType) || isBlank(xpValue)) {
            showMessage(request, response,
                    "Please fill in all common required fields.", "error");
            return;
        }

        if (!("Java".equals(category)
                || "SQL".equals(category)
                || "DSA".equals(category))) {
            showMessage(request, response,
                    "Please select a valid category.", "error");
            return;
        }

        if (!("Easy".equals(difficulty)
                || "Medium".equals(difficulty)
                || "Hard".equals(difficulty))) {
            showMessage(request, response,
                    "Please select a valid difficulty.", "error");
            return;
        }

        if (!("MCQ".equals(questionType)
                || "FILL_BLANK".equals(questionType)
                || "CODING".equals(questionType))) {
            showMessage(request, response,
                    "Please select a valid question type.", "error");
            return;
        }

        int xpReward;

        try {
            xpReward = Integer.parseInt(xpValue);
        } catch (NumberFormatException e) {
            showMessage(request, response,
                    "XP reward must be a valid number.", "error");
            return;
        }

        if (xpReward < 1 || xpReward > 1000 || title.length() > 100) {
            showMessage(request, response,
                    "Title or XP reward is outside the allowed limits.",
                    "error");
            return;
        }

        // MCQ-specific validation
        if ("MCQ".equals(questionType)) {
            if (isBlank(optionA) || isBlank(optionB)
                    || isBlank(optionC) || isBlank(optionD)
                    || isBlank(correctAnswer)) {
                showMessage(request, response,
                        "MCQ questions require four options and a correct answer.",
                        "error");
                return;
            }

            if (!("A".equals(correctAnswer)
                    || "B".equals(correctAnswer)
                    || "C".equals(correctAnswer)
                    || "D".equals(correctAnswer))) {
                showMessage(request, response,
                        "Correct answer must be A, B, C, or D.", "error");
                return;
            }

            if (optionA.length() > 255 || optionB.length() > 255
                    || optionC.length() > 255 || optionD.length() > 255) {
                showMessage(request, response,
                        "Each MCQ option must be 255 characters or fewer.",
                        "error");
                return;
            }
        }

        // Fill-in-the-blanks validation
        if ("FILL_BLANK".equals(questionType) && isBlank(expectedAnswer)) {
            showMessage(request, response,
                    "Enter the expected answer for the blank.", "error");
            return;
        }

        // Coding-question validation
        if ("CODING".equals(questionType)
                && isBlank(expectedAnswer)) {
            showMessage(request, response,
                    "Enter the expected answer or solution for this coding question.",
                    "error");
            return;
        }

        String correctAnswerText = null;

        if ("MCQ".equals(questionType)) {
            switch (correctAnswer) {
                case "A":
                    correctAnswerText = optionA.trim();
                    break;
                case "B":
                    correctAnswerText = optionB.trim();
                    break;
                case "C":
                    correctAnswerText = optionC.trim();
                    break;
                case "D":
                    correctAnswerText = optionD.trim();
                    break;
                default:
                    showMessage(request, response,
                            "Invalid correct answer.", "error");
                    return;
            }
        } else {
            correctAnswerText = expectedAnswer.trim();
        }

        String sql =
                "INSERT INTO missions "
                + "(title, description, category, difficulty, xp_reward, "
                + "correct_answer, option_a, option_b, option_c, option_d, "
                + "created_by, question_type, expected_answer, starter_code, "
                + "test_code) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, title.trim());
            ps.setString(2, description.trim());
            ps.setString(3, category);
            ps.setString(4, difficulty);
            ps.setInt(5, xpReward);
            ps.setString(6, correctAnswerText);

            // These fields can be NULL for non-MCQ questions.
            ps.setString(7, "MCQ".equals(questionType) ? optionA.trim() : null);
            ps.setString(8, "MCQ".equals(questionType) ? optionB.trim() : null);
            ps.setString(9, "MCQ".equals(questionType) ? optionC.trim() : null);
            ps.setString(10, "MCQ".equals(questionType) ? optionD.trim() : null);

            ps.setInt(11, (Integer) session.getAttribute("adminId"));
            ps.setString(12, questionType);
            ps.setString(13, "MCQ".equals(questionType)
                    ? null : expectedAnswer.trim());
            ps.setString(14, "CODING".equals(questionType)
                    ? starterCode : null);
            ps.setString(15, "CODING".equals(questionType)
                    ? testCode : null);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                showMessage(request, response,
                        "Question added successfully!", "success");
            } else {
                showMessage(request, response,
                        "Question could not be saved.", "error");
            }

        } catch (SQLException e) {
            log("Unable to save CodeQuest question.", e);
            showMessage(request, response,
                    "Unable to save the question. Check the database connection and table structure.",
                    "error");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void showMessage(HttpServletRequest request,
                             HttpServletResponse response,
                             String message,
                             String type)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.setAttribute("questionMessage", message);
            session.setAttribute("questionMessageType", type);
        }

        response.sendRedirect(
                request.getContextPath() + "/add-question.jsp");
    }
}
