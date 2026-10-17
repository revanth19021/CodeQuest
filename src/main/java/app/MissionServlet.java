
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

@WebServlet("/missions")
public class MissionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);

        // Only logged-in users can access missions
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp");
            return;
        }

        List<String[]> missions = new ArrayList<>();

        String sql =
                "SELECT id, title, description, category, difficulty, "
                + "xp_reward, option_a, option_b, option_c, option_d "
                + "FROM missions ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String[] mission = new String[10];

                mission[0] = String.valueOf(rs.getInt("id"));
                mission[1] = rs.getString("title");
                mission[2] = rs.getString("description");
                mission[3] = rs.getString("category");
                mission[4] = rs.getString("difficulty");
                mission[5] = String.valueOf(rs.getInt("xp_reward"));
                mission[6] = rs.getString("option_a");
                mission[7] = rs.getString("option_b");
                mission[8] = rs.getString("option_c");
                mission[9] = rs.getString("option_d");

                missions.add(mission);
            }

            request.setAttribute("missions", missions);

            RequestDispatcher rd =
                    request.getRequestDispatcher("/missions.jsp");

            rd.forward(request, response);

        } catch (Exception e) {
            log("Unable to retrieve missions.", e);

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(
                    "<!DOCTYPE html><html><body "
                    + "style='background:#070b18;color:white;"
                    + "font-family:Arial;padding:30px'>"
                    + "<h2>Unable to load missions</h2>"
                    + "<p>Please check the database connection and try again.</p>"
                    + "<a style='color:#00d9ff' href='"
                    + request.getContextPath()
                    + "/dashboard'>Back to Dashboard</a>"
                    + "</body></html>");
        }
    }
}
