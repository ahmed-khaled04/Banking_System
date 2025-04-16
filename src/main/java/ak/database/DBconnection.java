package ak.database;

import java.sql.*;

public class DBconnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/banking_system";
    private static final String USER = "postgres"; // Your PostgreSQL username
    private static final String PASSWORD = "332004"; // Your PostgreSQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void clearDatabase() {
        String[] tables = {"transactions", "accounts", "customers", "loans"}; // Add all table names here
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {
            // Disable foreign key checks
            stmt.execute("SET session_replication_role = 'replica';");

            // Truncate all tables
            for (String table : tables) {
                stmt.execute("TRUNCATE TABLE " + table + " CASCADE;");
            }

            // Re-enable foreign key checks
            stmt.execute("SET session_replication_role = 'origin';");

            System.out.println("All data in the database has been erased.");
        } catch (SQLException e) {
            System.err.println("Error clearing database: " + e.getMessage());
        }
    }
}
