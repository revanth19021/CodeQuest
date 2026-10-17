
package app;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        String contextPath = request.getContextPath();

        // Get the existing session without creating a new one
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        // Expire the session cookie in the browser
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setMaxAge(0);
        cookie.setPath(contextPath.isEmpty() ? "/" : contextPath);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        // Prevent the browser from caching authenticated pages
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        response.sendRedirect(contextPath + "/login.html");
    }
}
