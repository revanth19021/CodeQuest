
package app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        String sql =
                "SELECT username, total_xp, player_level "
                + "FROM users WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    session.invalidate();
                    response.sendRedirect(
                            request.getContextPath() + "/login.jsp");
                    return;
                }

                String username = rs.getString("username");
                int totalXP = rs.getInt("total_xp");
                int playerLevel = rs.getInt("player_level");

                String countSQL =
                        "SELECT COUNT(*) FROM mission_submissions "
                        + "WHERE user_id = ? AND is_correct = TRUE";

                int completedMissions;

                try (PreparedStatement countPS =
                             con.prepareStatement(countSQL)) {

                    countPS.setInt(1, userId);

                    try (ResultSet countRS = countPS.executeQuery()) {
                        countRS.next();
                        completedMissions = countRS.getInt(1);
                    }
                }

                // Pass data to JSP without JSTL or EL
                request.setAttribute("username", username);
                request.setAttribute("totalXP", totalXP);
                request.setAttribute("playerLevel", playerLevel);
                request.setAttribute(
                        "completedMissions", completedMissions);

                RequestDispatcher rd =
                        request.getRequestDispatcher("/dashboard.jsp");

                rd.forward(request, response);
            }

        } catch (Exception e) {
            log("Unable to load player dashboard.", e);

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(
                    "<!DOCTYPE html><html><body "
                    + "style='background:#070b18;color:white;"
                    + "font-family:Arial;padding:30px'>"
                    + "<h2>Unable to load your dashboard</h2>"
                    + "<p>Please check the database connection and try again.</p>"
                    + "<a style='color:#00d9ff' href='"
                    + request.getContextPath()
                    + "/login.jsp'>Return to Login</a>"
                    + "</body></html>");
        }
    }
}
