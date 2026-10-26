
package app;

import java.awt.Color;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

@WebServlet("/download-result")
public class DownloadResultServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Color NAVY = new Color(16, 22, 43);
    private static final Color PURPLE = new Color(139, 53, 255);
    private static final Color CYAN = new Color(0, 160, 195);
    private static final Color LIGHT = new Color(242, 245, 252);
    private static final Color MUTED = new Color(90, 100, 125);
    private static final Color GREEN = new Color(30, 145, 95);
    private static final Color RED = new Color(190, 55, 65);

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(
                request.getContextPath() + "/login.jsp"
            );
            return;
        }

        Object sessionUserId = session.getAttribute("userId");

        if (!(sessionUserId instanceof Number)) {
            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Please log in again."
            );
            return;
        }

        int userId = ((Number) sessionUserId).intValue();

        String userSql =
            "SELECT username, total_xp, player_level " +
            "FROM users WHERE id = ?";

        String submissionSql =
            "SELECT m.title, m.category, m.difficulty, " +
            "ms.answer, ms.is_correct, ms.completed_at, " +
            "CASE WHEN ms.is_correct = 1 " +
            "THEN m.xp_reward ELSE 0 END AS earned_xp " +
            "FROM mission_submissions ms " +
            "JOIN missions m ON ms.mission_id = m.id " +
            "WHERE ms.user_id = ? " +
            "ORDER BY ms.completed_at DESC";

        String username;
        int totalXP;
        int playerLevel;

        response.setContentType("application/pdf");
        response.setHeader(
            "Content-Disposition",
            "attachment; filename=\"CodeQuest-Performance-Report.pdf\""
        );
        response.setHeader("Cache-Control", "no-store");

        Document document = new Document(PageSize.A4, 36, 36, 45, 45);

        try (Connection con = DBConnection.getConnection()) {

            // Get student details
            try (PreparedStatement ps = con.prepareStatement(userSql)) {
                ps.setInt(1, userId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        response.reset();
                        response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Student account not found."
                        );
                        return;
                    }

                    username = rs.getString("username");
                    totalXP = rs.getInt("total_xp");
                    playerLevel = rs.getInt("player_level");
                }
            }

            PdfWriter writer = PdfWriter.getInstance(
                document, response.getOutputStream()
            );

            writer.setPageEvent(new ReportPageEvent());

            document.open();

            Font titleFont = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD, 20,
                Color.WHITE
            );

            Font subtitleFont = FontFactory.getFont(
                FontFactory.HELVETICA, 10,
                Color.WHITE
            );

            Font headingFont = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD, 13,
                NAVY
            );

            Font normalFont = FontFactory.getFont(
                FontFactory.HELVETICA, 10,
                NAVY
            );

            Font smallFont = FontFactory.getFont(
                FontFactory.HELVETICA, 8,
                MUTED
            );

            // Report banner
            PdfPTable banner = new PdfPTable(1);
            banner.setWidthPercentage(100);

            PdfPCell bannerCell = new PdfPCell();
            bannerCell.setBackgroundColor(NAVY);
            bannerCell.setBorder(Rectangle.NO_BORDER);
            bannerCell.setPadding(18);

            Paragraph title = new Paragraph(
                "CODEQUEST", titleFont
            );
            title.setAlignment(Element.ALIGN_CENTER);
            bannerCell.addElement(title);

            Paragraph subtitle = new Paragraph(
                "STUDENT PERFORMANCE REPORT", subtitleFont
            );
            subtitle.setAlignment(Element.ALIGN_CENTER);
            bannerCell.addElement(subtitle);

            banner.addCell(bannerCell);
            document.add(banner);
            document.add(new Paragraph(" "));

            // Student profile
            document.add(new Paragraph("STUDENT PROFILE", headingFont));
            document.add(new Paragraph(" "));

            PdfPTable profile = new PdfPTable(2);
            profile.setWidthPercentage(100);
            profile.setWidths(new float[] { 1, 1 });

            addProfileCell(profile, "Student Username", safe(username));
            addProfileCell(profile, "Player Level",
                    String.valueOf(playerLevel));
            addProfileCell(profile, "Total XP", totalXP + " XP");
            addProfileCell(profile, "Report Generated",
                    new SimpleDateFormat(
                        "dd MMM yyyy, hh:mm a", Locale.ENGLISH
                    ).format(new Date()));

            document.add(profile);
            document.add(new Paragraph(" "));

            // Submission history
            document.add(new Paragraph(
                "MISSION SUBMISSION HISTORY", headingFont
            ));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(
                new float[] { 2.7f, 1.5f, 1.4f, 1.2f, 1.1f, 1.6f }
            );
            table.setWidthPercentage(100);
            table.setHeaderRows(1);
            table.setSplitRows(true);

            addTableHeader(table, "Mission");
            addTableHeader(table, "Category");
            addTableHeader(table, "Difficulty");
            addTableHeader(table, "Status");
            addTableHeader(table, "XP");
            addTableHeader(table, "Date");

            int attempted = 0;
            int correct = 0;
            int incorrect = 0;
            int calculatedXP = 0;

            try (PreparedStatement ps =
                    con.prepareStatement(submissionSql)) {

                ps.setInt(1, userId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        attempted++;

                        String missionTitle = safe(rs.getString("title"));
                        String category = safe(rs.getString("category"));
                        String difficulty = safe(rs.getString("difficulty"));
                        boolean isCorrect = rs.getBoolean("is_correct");
                        int earnedXP = rs.getInt("earned_xp");

                        if (isCorrect) {
                            correct++;
                            calculatedXP += earnedXP;
                        } else {
                            incorrect++;
                        }

                        Date completedAt = rs.getTimestamp("completed_at");
                        String dateText = completedAt == null
                            ? "-"
                            : new SimpleDateFormat(
                                "dd MMM yyyy", Locale.ENGLISH
                              ).format(completedAt);

                        addTableCell(table, missionTitle, false, NAVY);
                        addTableCell(table, category, false, NAVY);
                        addTableCell(table, difficulty, false, NAVY);
                        addTableCell(
                            table,
                            isCorrect ? "Correct" : "Incorrect",
                            true,
                            isCorrect ? GREEN : RED
                        );
                        addTableCell(
                            table, String.valueOf(earnedXP), false, NAVY
                        );
                        addTableCell(table, dateText, false, NAVY);
                    }
                }
            }

            if (attempted == 0) {
                PdfPCell emptyCell = new PdfPCell(
                    new Phrase(
                        "No mission submissions found.",
                        normalFont
                    )
                );
                emptyCell.setColspan(6);
                emptyCell.setPadding(12);
                emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                emptyCell.setBackgroundColor(LIGHT);
                table.addCell(emptyCell);
            }

            document.add(table);
            document.add(new Paragraph(" "));

            // Performance summary
            document.add(new Paragraph(
                "PERFORMANCE SUMMARY", headingFont
            ));
            document.add(new Paragraph(" "));

            double percentage = attempted == 0
                ? 0.0
                : correct * 100.0 / attempted;

            String grade;

            if (percentage >= 90) {
                grade = "A";
            } else if (percentage >= 75) {
                grade = "B";
            } else if (percentage >= 60) {
                grade = "C";
            } else {
                grade = "D";
            }

            PdfPTable summary = new PdfPTable(2);
            summary.setWidthPercentage(100);

            addSummaryCell(summary, "Total Submissions",
                    String.valueOf(attempted));
            addSummaryCell(summary, "Correct Submissions",
                    String.valueOf(correct));
            addSummaryCell(summary, "Incorrect Submissions",
                    String.valueOf(incorrect));
            addSummaryCell(summary, "Calculated XP from Correct Results",
                    calculatedXP + " XP");
            addSummaryCell(summary, "Accuracy",
                    String.format(Locale.US, "%.2f%%", percentage));
            addSummaryCell(summary, "Performance Grade", grade);

            document.add(summary);
            document.add(new Paragraph(" "));

            Paragraph note = new Paragraph(
                "Note: XP shown in the mission history is calculated " +
                "from each mission's current XP reward. It may differ " +
                "from the XP awarded at the time of submission if " +
                "mission rewards have changed.",
                smallFont
            );
            document.add(note);

        } catch (SQLException | DocumentException e) {
            throw new ServletException(
                "Unable to generate the performance report.", e
            );
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    private void addProfileCell(PdfPTable table,
                                String label,
                                String value) {

        Font labelFont = FontFactory.getFont(
            FontFactory.HELVETICA_BOLD, 9, MUTED
        );

        Font valueFont = FontFactory.getFont(
            FontFactory.HELVETICA_BOLD, 11, NAVY
        );

        PdfPCell cell = new PdfPCell();
        cell.setPadding(10);
        cell.setBackgroundColor(LIGHT);
        cell.setBorderColor(Color.WHITE);

        cell.addElement(new Paragraph(label, labelFont));
        cell.addElement(new Paragraph(value, valueFont));

        table.addCell(cell);
    }

    private void addTableHeader(PdfPTable table, String text) {
        Font font = FontFactory.getFont(
            FontFactory.HELVETICA_BOLD, 8, Color.WHITE
        );

        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(NAVY);
        cell.setPadding(7);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderColor(Color.WHITE);

        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table,
                              String text,
                              boolean centered,
                              Color textColor) {

        Font font = FontFactory.getFont(
            FontFactory.HELVETICA, 8, textColor
        );

        PdfPCell cell = new PdfPCell(
            new Phrase(safe(text), font)
        );

        cell.setPadding(6);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderColor(new Color(220, 225, 235));

        if (centered) {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        }

        table.addCell(cell);
    }

    private void addSummaryCell(PdfPTable table,
                                String label,
                                String value) {

        Font labelFont = FontFactory.getFont(
            FontFactory.HELVETICA, 9, MUTED
        );

        Font valueFont = FontFactory.getFont(
            FontFactory.HELVETICA_BOLD, 11, PURPLE
        );

        PdfPCell cell = new PdfPCell();
        cell.setPadding(10);
        cell.setBackgroundColor(LIGHT);
        cell.setBorderColor(Color.WHITE);

        cell.addElement(new Paragraph(label, labelFont));
        cell.addElement(new Paragraph(value, valueFont));

        table.addCell(cell);
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private static class ReportPageEvent extends PdfPageEventHelper {

        private final Font footerFont = FontFactory.getFont(
            FontFactory.HELVETICA, 8, MUTED
        );

        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            float left = document.left();
            float right = document.right();
            float y = document.bottom() - 20;

            writer.getDirectContent().setColorStroke(
                new Color(220, 225, 235)
            );

            writer.getDirectContent().moveTo(left, y + 12);
            writer.getDirectContent().lineTo(right, y + 12);
            writer.getDirectContent().stroke();

            Phrase footer = new Phrase(
                "CodeQuest | Student Performance Report     Page " +
                writer.getPageNumber(),
                footerFont
            );

            com.lowagie.text.pdf.ColumnText.showTextAligned(
                writer.getDirectContent(),
                Element.ALIGN_CENTER,
                footer,
                (left + right) / 2,
                y,
                0
            );
        }
    }
}
