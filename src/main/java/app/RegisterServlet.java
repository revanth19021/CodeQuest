
package app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword =
                request.getParameter("confirmPassword");

        if (isBlank(username) || isBlank(email)
                || isBlank(password) || isBlank(confirmPassword)) {

            showMessage(request, response,
                    "All fields are required.");
            return;
        }

        username = username.trim();
        email = email.trim();

        if (username.length() > 50 || email.length() > 100
                || password.length() > 100) {

            showMessage(request, response,
                    "One or more fields exceed the allowed length.");
            return;
        }

        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            showMessage(request, response,
                    "Please enter a valid email address.");
            return;
        }

        if (password.length() < 8) {
            showMessage(request, response,
                    "Password must contain at least 8 characters.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMessage(request, response,
                    "Passwords do not match.");
            return;
        }

        String checkSQL =
                "SELECT id FROM users WHERE username = ? OR email = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement checkPS =
                     con.prepareStatement(checkSQL)) {

            checkPS.setString(1, username);
            checkPS.setString(2, email);

            try (ResultSet rs = checkPS.executeQuery()) {
                if (rs.next()) {
                    showMessage(request, response,
                            "Username or email already exists.");
                    return;
                }
            }

            String insertSQL =
                    "INSERT INTO users "
                    + "(username, email, password, total_xp, player_level) "
                    + "VALUES (?, ?, ?, 0, 1)";

            try (PreparedStatement insertPS =
                         con.prepareStatement(insertSQL)) {

                insertPS.setString(1, username);
                insertPS.setString(2, email);
                insertPS.setString(3, password);

                int rows = insertPS.executeUpdate();

                if (rows != 1) {
                    showMessage(request, response,
                            "Registration could not be completed.");
                    return;
                }
            }

        } catch (SQLException e) {
            log("Unable to register CodeQuest user.", e);

            showMessage(request, response,
                    "Registration could not be completed. "
                    + "Please check your details and try again.");
            return;
        }

        // The account has been saved successfully.
        // Email failure must not undo the registration.
        try {
            EmailService.sendWelcomeEmail(email, username);

            response.sendRedirect(
                    request.getContextPath()
                    + "/login.jsp?registered=1&email=sent");

        } catch (Exception e) {
            log("Account created, but welcome email could not be sent.", e);

            response.sendRedirect(
                    request.getContextPath()
                    + "/login.jsp?registered=1&email=failed");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void showMessage(HttpServletRequest request,
                             HttpServletResponse response,
                             String message)
            throws ServletException, IOException {

        request.setAttribute("message", message);

        RequestDispatcher rd =
                request.getRequestDispatcher("/register.jsp");

        rd.forward(request, response);
    }
}
