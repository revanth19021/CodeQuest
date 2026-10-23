
package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private static final String URL =
		    "jdbc:mysql://mysql-ba7768a-revanth200319-c3b9.i.aivencloud.com:13341/codequest_db?sslMode=REQUIRED";

    private static final String USERNAME = "avnadmin";

    private static final String PASSWORD =
        System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                "MySQL JDBC Driver not found. Check Connector/J.", e);
        }

        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                "DB_PASSWORD environment variable is not configured.");
        }

        return DriverManager.getConnection(
            URL, USERNAME, PASSWORD);
    }
}
