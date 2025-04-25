package ak.loans;

import ak.database.DBconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LoanRequestManager {

    public boolean submitLoanRequest(String accountNumber, double loanAmount, String loanReason) {
        String sql = "INSERT INTO loan_requests (account_number, loan_amount, loan_reason, status) VALUES (?, ?, ?, 'Pending')";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            pstmt.setDouble(2, loanAmount);
            pstmt.setString(3, loanReason);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public List<LoanRequest> getAllLoanRequests() {
        List<LoanRequest> loanRequests = new ArrayList<>();
        String sql = "SELECT * FROM loan_requests";
        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                loanRequests.add(new LoanRequest(
                    rs.getString("id"),
                    rs.getString("account_number"),
                    rs.getDouble("loan_amount"),
                    rs.getString("loan_reason"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loanRequests;
    }

    public boolean updateLoanRequestStatus(String requestId, String status) {
        String sql = "UPDATE loan_requests SET status = ? WHERE id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, Integer.parseInt(requestId));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public LoanRequest submitRequest(String accountNumber, double loanAmount, String loanReason) {
        String sql = "INSERT INTO loan_requests (account_number, loan_amount, loan_reason, status) VALUES (?, ?, ?, 'Pending')";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, accountNumber);
            pstmt.setDouble(2, loanAmount);
            pstmt.setString(3, loanReason);
            int affectedRows = pstmt.executeUpdate();
    
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return new LoanRequest(
                            generatedKeys.getString(1), // Assuming the ID is auto-generated
                            accountNumber,
                            loanAmount,
                            loanReason,
                            "Pending"
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the loan request could not be created
    }
}