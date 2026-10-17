
package app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin-login")
public class AdminLoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ServletContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String appName = config.getInitParameter("appName");
        context = config.getServletContext();

        if (appName == null) {
            appName = "CodeQuest";
        }

        context.setAttribute("appName", appName);

        log(appName + " AdminLoginServlet initialized");
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null
                || username.trim().isEmpty()
                || password.isEmpty()) {

            request.setAttribute("message",
                    "Please enter your username and password.");

            RequestDispatcher rd =
                    request.getRequestDispatcher("/admin-login.html");

            rd.forward(request, response);
            return;
        }


String sql = "SELECT id, username FROM admins "
           + "WHERE username = ? AND password = ?";

try (Connection con = DBConnection.getConnection();
     PreparedStatement ps = con.prepareStatement(sql)) {

    ps.setString(1, username.trim());
    ps.setString(2, password);

    try (ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {

            HttpSession oldSession = request.getSession(false);

            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);

            session.setAttribute("adminId", rs.getInt("id"));
            session.setAttribute(
                    "adminUsername", rs.getString("username"));

            session.setMaxInactiveInterval(20 * 60);

            response.sendRedirect(
                    request.getContextPath() + "/admin-dashboard.jsp");
            return;
        }
    }

    request.setAttribute("message",
            "Invalid administrator username or password.");

    request.getRequestDispatcher("/admin-login.html")
           .forward(request, response);

} catch (Exception e) {
    log("Admin login failed", e);

    response.setContentType("text/html;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    response.getWriter().println(
            "Unable to complete login. Please check the database connection.");
}

            }
}
