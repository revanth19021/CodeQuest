
package app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
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

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        // Only authenticated administrators can add questions
        if (session == null
                || session.getAttribute("adminId") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/admin-login.html");
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String category = request.getParameter("category");
        String difficulty = request.getParameter("difficulty");

        String optionA = request.getParameter("option_a");
        String optionB = request.getParameter("option_b");
        String optionC = request.getParameter("option_c");
        String optionD = request.getParameter("option_d");

        String correctAnswer = request.getParameter("correct_answer");
        String xpValue = request.getParameter("xp_reward");

        // Validate required fields
        if (isBlank(title) || isBlank(description)
                || isBlank(category) || isBlank(difficulty)
                || isBlank(optionA) || isBlank(optionB)
                || isBlank(optionC) || isBlank(optionD)
                || isBlank(correctAnswer) || isBlank(xpValue)) {

            showMessage(request, response,
                    "Please fill in all the required fields.", "error");
            return;
        }

        // Validate category and difficulty on the server
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

        if (!("A".equals(correctAnswer)
                || "B".equals(correctAnswer)
                || "C".equals(correctAnswer)
                || "D".equals(correctAnswer))) {

            showMessage(request, response,
                    "Please select a valid correct answer.", "error");
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

        if (xpReward < 1 || xpReward > 1000
                || title.length() > 100
                || optionA.length() > 255
                || optionB.length() > 255
                || optionC.length() > 255
                || optionD.length() > 255) {

            showMessage(request, response,
                    "Check the title, options, and XP reward limits.",
                    "error");
            return;
        }

        String answerText;

        switch (correctAnswer) {
            case "A":
                answerText = optionA;
                break;
            case "B":
                answerText = optionB;
                break;
            case "C":
                answerText = optionC;
                break;
            default:
                answerText = optionD;
                break;
        }

        String sql =
                "INSERT INTO missions "
                + "(title, description, category, difficulty, "
                + "xp_reward, correct_answer, option_a, option_b, "
                + "option_c, option_d, created_by) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, title.trim());
            ps.setString(2, description.trim());
            ps.setString(3, category);
            ps.setString(4, difficulty);
            ps.setInt(5, xpReward);
            ps.setString(6, answerText);
            ps.setString(7, optionA.trim());
            ps.setString(8, optionB.trim());
            ps.setString(9, optionC.trim());
            ps.setString(10, optionD.trim());
            ps.setInt(11, (Integer) session.getAttribute("adminId"));

            int rows = ps.executeUpdate();

            if (rows > 0) {
                showMessage(request, response,
                        "Question added successfully! It is now saved in MySQL.",
                        "success");
            } else {
                showMessage(request, response,
                        "The question could not be saved. Please try again.",
                        "error");
            }

        } catch (SQLException e) {
            log("Unable to save CodeQuest question.", e);

            showMessage(request, response,
                    "Unable to save the question. Please check the database "
                    + "table structure and connection.", "error");
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

        session.setAttribute("questionMessage", message);
        session.setAttribute("questionMessageType", type);

        // Redirect avoids duplicate insertion on browser refresh
        response.sendRedirect(
                request.getContextPath() + "/add-question.jsp");
    }
}
