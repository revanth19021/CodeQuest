
package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/codequest_db";

    private static final String USERNAME = "root";
    private static final String PASSWORD = "Nagasai@20032104";

    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MySQL JDBC Driver not found. Check Connector/J.", e);
        }

        return DriverManager.getConnection(
                URL, USERNAME, PASSWORD);
    }
}
