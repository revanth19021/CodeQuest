
package app;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin-logout")
public class AdminLogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        String contextPath = request.getContextPath();

        // Retrieve the existing administrator session
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        // Expire the browser's session cookie
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setMaxAge(0);
        cookie.setPath(contextPath.isEmpty() ? "/" : contextPath);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        // Prevent cached authenticated pages from being reused
        response.setHeader(
                "Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        response.sendRedirect(contextPath + "/admin-login.html");
    }
}
