
package app;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Message.RecipientType;

public class EmailService {

    public static void sendWelcomeEmail(
            String recipientEmail,
            String username) throws Exception {

        System.out.println("[EMAIL] Starting welcome email process.");

        String senderEmail = System.getenv("MAIL_USERNAME");
        String appPassword = System.getenv("MAIL_APP_PASSWORD");

        if (senderEmail == null || senderEmail.isBlank()
                || appPassword == null || appPassword.isBlank()) {
            throw new IllegalStateException(
                    "MAIL_USERNAME or MAIL_APP_PASSWORD is missing.");
        }

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");

        // Prevent SMTP connection attempts from hanging indefinitely.
        properties.put("mail.smtp.connectiontimeout", "10000");
        properties.put("mail.smtp.timeout", "10000");
        properties.put("mail.smtp.writetimeout", "10000");

        // Enable SMTP diagnostics in Render runtime logs.
        properties.put("mail.debug", "true");

        Session mailSession = Session.getInstance(
                properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication
                            getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                senderEmail, appPassword);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(mailSession);

            message.setFrom(new InternetAddress(
                    senderEmail, "CodeQuest Team", "UTF-8"));

            message.setRecipient(
                    RecipientType.TO,
                    new InternetAddress(recipientEmail));

            message.setSubject(
                    "Welcome to CodeQuest | Registration Successful",
                    "UTF-8");

            String safeUsername = escapeHtml(username);

            String html =
                "<!DOCTYPE html>"
              + "<html><body style='margin:0;padding:0;"
              + "background-color:#080d1d;font-family:Arial,Helvetica,sans-serif;'>"

              + "<table role='presentation' width='100%' cellspacing='0' "
              + "cellpadding='0' style='background:#080d1d;padding:30px 10px;'>"
              + "<tr><td align='center'>"

              + "<table role='presentation' width='600' cellspacing='0' "
              + "cellpadding='0' style='width:100%;max-width:600px;"
              + "background:#111a32;border:1px solid #29365b;"
              + "border-radius:16px;overflow:hidden;'>"

              // Header
              + "<tr><td align='center' style='padding:35px 24px;"
              + "background:#171c3d;'>"
              + "<div style='font-size:30px;font-weight:bold;"
              + "letter-spacing:2px;color:#ffffff;'>"
              + "CODE<span style='color:#a78bfa;'>QUEST</span></div>"
              + "<p style='margin:12px 0 0;color:#c5cbea;font-size:14px;'>"
              + "LEARN. CODE. LEVEL UP.</p>"
              + "</td></tr>"

              // Welcome content
              + "<tr><td style='padding:35px 28px;color:#f4f6ff;'>"
              + "<div style='text-align:center;font-size:42px;'>&#127881;</div>"
              + "<h1 style='text-align:center;font-size:27px;"
              + "line-height:1.3;margin:15px 0;color:#ffffff;'>"
              + "Welcome to CodeQuest!</h1>"

              + "<p style='font-size:16px;line-height:1.8;color:#d5dcf2;'>"
              + "Hi <strong style='color:#c4b5fd;'>" + safeUsername
              + "</strong>,</p>"

              + "<p style='font-size:15px;line-height:1.8;color:#d5dcf2;'>"
              + "Your registration has been completed successfully! "
              + "Your CodeQuest account is ready, and your programming "
              + "journey starts now.</p>"

              // Confirmation
              + "<table role='presentation' width='100%' cellspacing='0' "
              + "cellpadding='0' style='background:#142c30;"
              + "border:1px solid #22675d;border-radius:10px;'>"
              + "<tr><td style='padding:17px;'>"
              + "<p style='margin:0;color:#6ee7b7;font-size:15px;"
              + "font-weight:bold;'>&#10003; REGISTRATION SUCCESSFUL</p>"
              + "<p style='margin:8px 0 0;color:#d1fae5;font-size:13px;"
              + "line-height:1.6;'>Your account has been created successfully.</p>"
              + "</td></tr></table>"

              + "<h2 style='font-size:19px;color:#ffffff;margin-top:30px;'>"
              + "Your coding adventure awaits</h2>"

              // Features
              + "<table role='presentation' width='100%' cellspacing='0' "
              + "cellpadding='0'>"

              + "<tr><td style='padding:12px 0;border-bottom:1px solid #29365b;'>"
              + "<strong style='color:#c4b5fd;font-size:15px;'>"
              + "&#128187; Practice programming</strong>"
              + "<p style='margin:6px 0 0;color:#c5cbea;font-size:14px;"
              + "line-height:1.6;'>Improve your Java, SQL, and DSA skills "
              + "with practice missions and challenges.</p></td></tr>"

              + "<tr><td style='padding:12px 0;border-bottom:1px solid #29365b;'>"
              + "<strong style='color:#67e8f9;font-size:15px;'>"
              + "&#127942; Earn XP and level up</strong>"
              + "<p style='margin:6px 0 0;color:#c5cbea;font-size:14px;"
              + "line-height:1.6;'>Complete missions, earn experience points, "
              + "and progress through levels.</p></td></tr>"

              + "<tr><td style='padding:12px 0;'>"
              + "<strong style='color:#86efac;font-size:15px;'>"
              + "&#128640; Build your skills</strong>"
              + "<p style='margin:6px 0 0;color:#c5cbea;font-size:14px;"
              + "line-height:1.6;'>Practice problem-solving and build "
              + "confidence one challenge at a time.</p></td></tr>"

              + "</table>"

              + "<p style='font-size:15px;line-height:1.8;color:#d5dcf2;"
              + "margin-top:25px;'>Ready to begin? Log in to CodeQuest "
              + "and start exploring your missions!</p>"

              // Live Render URL
              + "<table role='presentation' align='center' cellspacing='0' "
              + "cellpadding='0' style='margin:25px auto;'>"
              + "<tr><td align='center' style='background:#7c3aed;"
              + "border-radius:8px;'>"
              + "<a href='https://codequest-2-ppmf.onrender.com/login.jsp' "
              + "style='display:inline-block;padding:15px 30px;"
              + "font-size:15px;font-weight:bold;color:#ffffff;"
              + "text-decoration:none;'>START YOUR JOURNEY &rarr;</a>"
              + "</td></tr></table>"

              + "<p style='font-size:14px;line-height:1.8;color:#c5cbea;'>"
              + "We're excited to have you in our learning community. "
              + "Keep practising, stay curious, and keep coding!</p>"

              + "<p style='font-size:15px;line-height:1.8;color:#ffffff;'>"
              + "Happy Coding!<br><strong>The CodeQuest Team</strong></p>"
              + "</td></tr>"

              // Footer
              + "<tr><td align='center' style='padding:22px 20px;"
              + "background:#0b1124;border-top:1px solid #29365b;'>"
              + "<p style='margin:0;color:#a5b0d0;font-size:12px;"
              + "line-height:1.8;'>"
              + "CodeQuest &mdash; Your programming learning adventure."
              + "<br>This email confirms the registration of your account."
              + "</p></td></tr>"

              + "</table></td></tr></table>"
              + "</body></html>";

            message.setContent(html, "text/html; charset=UTF-8");

            System.out.println(
                    "[EMAIL] Connecting to Gmail SMTP for recipient: "
                    + maskEmail(recipientEmail));

            Transport.send(message);

            System.out.println(
                    "[EMAIL] Gmail accepted the welcome email for "
                    + maskEmail(recipientEmail));

        } catch (Exception e) {
            System.err.println(
                    "[EMAIL ERROR] Welcome email failed: "
                    + e.getClass().getName() + ": " + e.getMessage());

            // Print the cause and stack trace to Render logs.
            e.printStackTrace();

            // Keep the exception available to RegisterServlet.
            throw e;
        }
    }

    private static String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }

    private static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "[invalid email]";
        }

        int at = email.indexOf('@');
        String local = email.substring(0, at);

        return (local.length() > 1
                ? local.substring(0, 2) + "***"
                : "*") + email.substring(at);
    }
}
