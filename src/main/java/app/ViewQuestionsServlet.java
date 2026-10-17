
package app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/view-questions")
public class ViewQuestionsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);

        // Only logged-in admins can view the question library
        if (session == null
                || session.getAttribute("adminId") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/admin-login.html");
            return;
        }

        List<String[]> questions = new ArrayList<>();

        String sql =
                "SELECT id, title, category, difficulty, xp_reward, "
                + "option_a, option_b, option_c, option_d, correct_answer "
                + "FROM missions ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String[] question = new String[10];

                question[0] = String.valueOf(rs.getInt("id"));
                question[1] = rs.getString("title");
                question[2] = rs.getString("category");
                question[3] = rs.getString("difficulty");
                question[4] = String.valueOf(rs.getInt("xp_reward"));
                question[5] = rs.getString("option_a");
                question[6] = rs.getString("option_b");
                question[7] = rs.getString("option_c");
                question[8] = rs.getString("option_d");
                question[9] = rs.getString("correct_answer");

                questions.add(question);
            }

            request.setAttribute("questions", questions);

            RequestDispatcher rd =
                    request.getRequestDispatcher("/view-questions.jsp");

            rd.forward(request, response);

        } catch (Exception e) {
            log("Unable to retrieve CodeQuest questions.", e);

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(
                    "<!DOCTYPE html><html><body "
                    + "style='background:#070b18;color:white;"
                    + "font-family:Arial;padding:30px'>"
                    + "<h2>Unable to load questions</h2>"
                    + "<p>Check the database connection and missions table.</p>"
                    + "<a style='color:#00d9ff' href='"
                    + request.getContextPath()
                    + "/admin-dashboard.jsp'>Back to Dashboard</a>"
                    + "</body></html>");
        }
    }
}
