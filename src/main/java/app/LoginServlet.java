
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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String identifier = request.getParameter("email");
        String password = request.getParameter("password");

        if (identifier == null || identifier.trim().isEmpty()
                || password == null || password.isEmpty()) {

            showMessage(request, response,
                    "Please enter your username or email and password.");
            return;
        }

        String sql =
                "SELECT id, username FROM users "
                + "WHERE (username = ? OR email = ?) AND password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, identifier.trim());
            ps.setString(2, identifier.trim());
            ps.setString(3, password);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    int userId = rs.getInt("id");
                    String username = rs.getString("username");

                    // Prevent session fixation
                    HttpSession oldSession = request.getSession(false);

                    if (oldSession != null) {
                        oldSession.invalidate();
                    }

                    HttpSession session = request.getSession(true);

                    session.setAttribute("userId", userId);
                    session.setAttribute("username", username);
                    session.setMaxInactiveInterval(20 * 60);

                    response.sendRedirect(
                            request.getContextPath() + "/dashboard");
                    return;
                }
            }

            showMessage(request, response,
                    "Invalid username/email or password.");

        } catch (Exception e) {
            log("Unable to authenticate CodeQuest user.", e);

            showMessage(request, response,
                    "Login could not be completed. Please try again.");
        }
    }

    private void showMessage(HttpServletRequest request,
                             HttpServletResponse response,
                             String message)
            throws ServletException, IOException {

        request.setAttribute("message", message);

        RequestDispatcher rd =
                request.getRequestDispatcher("/login.jsp");

        rd.forward(request, response);
    }
}
