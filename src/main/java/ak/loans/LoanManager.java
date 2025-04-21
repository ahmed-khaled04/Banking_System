package ak.loans;

import ak.database.DBconnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanManager {

    private Connection connection;
    public LoanManager() {
        try {
            this.connection = DBconnection.getConnection();
            initializeDatabase();
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw new RuntimeException("Failed to initialize LoanManager", e);
        }
    }

    private void initializeDatabase() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS loans ("
                + "loan_id VARCHAR(20) PRIMARY KEY, "
                + "customer_id VARCHAR(20) NOT NULL, "
                + "account_number VARCHAR(20) NOT NULL, " // Added account_number column
                + "loan_amount DECIMAL(15,2) NOT NULL, "
                + "interest_rate DECIMAL(5,2) NOT NULL, "
                + "duration_months INTEGER NOT NULL, "
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY (customer_id) REFERENCES customers(customer_id))";
        

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    public Loan createLoan(String customerId, String accountNumber, double loanAmount,
                           double interestRate, int durationInMonths) {
        validateLoanParameters(loanAmount, interestRate, durationInMonths);

        String sql = "INSERT INTO loans (loan_id, customer_id, account_number, loan_amount, interest_rate, duration_months) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            Loan newLoan = new Loan(customerId, accountNumber, loanAmount, interestRate, durationInMonths);

            pstmt.setString(1, newLoan.getLoanId());
            pstmt.setString(2, customerId);
            pstmt.setString(3, accountNumber); // Set account number
            pstmt.setDouble(4, loanAmount);
            pstmt.setDouble(5, interestRate);
            pstmt.setInt(6, durationInMonths);
            pstmt.executeUpdate();

            System.out.println("Loan created successfully: " + newLoan.getLoanId());
            return newLoan;
        } catch (SQLException e) {
            System.err.println("Error creating loan: " + e.getMessage());
            throw new RuntimeException("Failed to create loan", e);
        }
    }

    private void validateLoanParameters(double amount, double rate, int duration) {
        if (amount <= 0) throw new IllegalArgumentException("Loan amount must be positive");
        if (rate <= 0) throw new IllegalArgumentException("Interest rate must be positive");
        if (duration <= 0) throw new IllegalArgumentException("Loan duration must be positive");
    }

    public Loan getLoanById(String loanId) {
        String sql = "SELECT * FROM loans WHERE loan_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, loanId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToLoan(rs);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error retrieving loan: " + e.getMessage());
            throw new RuntimeException("Database error", e);
        }
    }

    public List<Loan> getLoansByCustomer(String customerId) {
        String sql = "SELECT * FROM loans WHERE customer_id = ?";
        List<Loan> loans = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
            return loans;
        } catch (SQLException e) {
            System.err.println("Error retrieving customer loans: " + e.getMessage());
            throw new RuntimeException("Database error", e);
        }
    }

    public List<Loan> getAllLoans() {
        String sql = "SELECT * FROM loans";
        List<Loan> loans = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
            return loans;
        } catch (SQLException e) {
            System.err.println("Error retrieving all loans: " + e.getMessage());
            throw new RuntimeException("Database error", e);
        }
    }

    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        return new Loan(
            rs.getString("loan_id"),
            rs.getString("customer_id"),
            rs.getString("account_number"), // Map account number
            rs.getDouble("loan_amount"),
            rs.getDouble("interest_rate"),
            rs.getInt("duration_months")
        );
    }

    public boolean removeLoan(String loanId) {
        String sql = "DELETE FROM loans WHERE loan_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, loanId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error removing loan: " + e.getMessage());
            throw new RuntimeException("Database error", e);
        }
    }

    // Financial calculation methods remain unchanged from original
    public double calculateCustomerMonthlyPayment(String customerId) {
        return getLoansByCustomer(customerId).stream()
                .mapToDouble(Loan::calculateMonthlyPayment)
                .sum();
    }

    public double calculateCustomerTotalRemaining(String customerId) {
        return getLoansByCustomer(customerId).stream()
                .mapToDouble(Loan::calculateTotalRepayment)
                .sum();
    }

    public void printCustomerLoans(String customerId) {
        List<Loan> loans = getLoansByCustomer(customerId);
        if (loans.isEmpty()) {
            System.out.println("No loans found for customer: " + customerId);
            return;
        }
    
        System.out.println("\nLoans for customer " + customerId + ":");
        System.out.println("--------------------------------");
        loans.forEach(Loan::printLoanDetails);
        System.out.println("--------------------------------");
        System.out.printf("Total Monthly Payments: $%.2f%n", calculateCustomerMonthlyPayment(customerId));
        System.out.printf("Total Remaining Repayment: $%.2f%n", calculateCustomerTotalRemaining(customerId));
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}