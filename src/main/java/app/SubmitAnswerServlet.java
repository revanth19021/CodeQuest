
package app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/submit-answer")
public class SubmitAnswerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        String missionIdValue = request.getParameter("missionId");
        String selectedOption = request.getParameter("answer");

        if (missionIdValue == null || selectedOption == null
                || !selectedOption.matches("[ABCD]")) {

            showResult(request, response,
                    "Please select a valid answer.", false, 0);
            return;
        }

        int missionId;

        try {
            missionId = Integer.parseInt(missionIdValue);
            if (missionId <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showResult(request, response,
                    "Invalid mission selected.", false, 0);
            return;
        }

        String sql =
                "SELECT correct_answer, xp_reward "
                + "FROM missions WHERE id = ?";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                String correctAnswer;
                int xpReward;

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, missionId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            showResult(request, response,
                                    "This mission no longer exists.",
                                    false, 0);
                            return;
                        }

                        correctAnswer = rs.getString("correct_answer");
                        xpReward = rs.getInt("xp_reward");
                    }
                }

                String selectedAnswer = getOptionText(
                        con, missionId, selectedOption);

                if (selectedAnswer == null) {
                    con.rollback();
                    showResult(request, response,
                            "The selected option is invalid.",
                            false, 0);
                    return;
                }

                boolean correct = selectedAnswer.equals(correctAnswer);

                String insert =
                        "INSERT INTO mission_submissions "
                        + "(user_id, mission_id, answer, is_correct) "
                        + "VALUES (?, ?, ?, ?)";

                try (PreparedStatement ps =
                        con.prepareStatement(insert)) {

                    ps.setInt(1, userId);
                    ps.setInt(2, missionId);
                    ps.setString(3, selectedOption);
                    ps.setBoolean(4, correct);
                    ps.executeUpdate();
                }

                if (correct) {
                    String update =
                            "UPDATE users "
                            + "SET total_xp = total_xp + ?, "
                            + "player_level = FLOOR((total_xp + ?) / 500) + 1 "
                            + "WHERE id = ?";

                    try (PreparedStatement ps =
                            con.prepareStatement(update)) {

                        ps.setInt(1, xpReward);
                        ps.setInt(2, xpReward);
                        ps.setInt(3, userId);

                        if (ps.executeUpdate() != 1) {
                            throw new SQLException(
                                    "Unable to update player XP.");
                        }
                    }
                }

                con.commit();

                if (correct) {
                    showResult(request, response,
                            "Correct answer! You earned " + xpReward
                                    + " XP.",
                            true, xpReward);
                } else {
                    showResult(request, response,
                            "Not quite! Keep practicing and try another mission.",
                            false, 0);
                }

            } catch (SQLException e) {
                con.rollback();

                // MySQL error 1062: duplicate unique-key entry.
                if (e.getErrorCode() == 1062) {
                    showResult(request, response,
                            "You have already attempted this mission.",
                            false, 0);
                } else {
                    throw e;
                }
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            log("Unable to process mission answer.", e);

            showResult(request, response,
                    "Unable to submit your answer. Please try again.",
                    false, 0);
        }
    }

    private String getOptionText(Connection con, int missionId,
                                 String option)
            throws SQLException {

        String column;

        switch (option) {
            case "A": column = "option_a"; break;
            case "B": column = "option_b"; break;
            case "C": column = "option_c"; break;
            case "D": column = "option_d"; break;
            default: return null;
        }

        String sql = "SELECT " + column
                + " FROM missions WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, missionId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }

        return null;
    }

    private void showResult(HttpServletRequest request,
                            HttpServletResponse response,
                            String message,
                            boolean correct,
                            int xp)
            throws ServletException, IOException {

        request.setAttribute("resultMessage", message);
        request.setAttribute("answerCorrect", correct);
        request.setAttribute("earnedXP", xp);

        request.getRequestDispatcher("/mission-result.jsp")
               .forward(request, response);
    }
}
