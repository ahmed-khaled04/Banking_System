package ak.admins;

import ak.App;
import ak.database.DBconnection;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminManager {

    // Authenticate an admin by username and password
    public Admin authenticateAdmin(String username, String passwordHash) {
        String sql = "SELECT * FROM admins WHERE username = ? AND password_hash = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, passwordHash);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Admin(
                    rs.getString("admin_id"),
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("password_hash")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add a new admin
    public boolean addAdmin(String name, String username, String passwordHash) {
        String sql = "INSERT INTO admins (admin_id, name, username, password_hash) VALUES (?, ?, ?, ?)";
        String adminId = "ADMIN-" + java.util.UUID.randomUUID().toString().substring(0, 8);
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, adminId);
            pstmt.setString(2, name);
            pstmt.setString(3, username);
            pstmt.setString(4, passwordHash);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Retrieve an admin by username
    public Admin getAdminByUsername(String username) {
        String sql = "SELECT * FROM admins WHERE username = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Admin(
                    rs.getString("admin_id"),
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("password_hash")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    
}