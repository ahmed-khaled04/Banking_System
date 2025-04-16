package ak.transactions;

import ak.accounts.Account;
import ak.accounts.AccountManager;
import ak.database.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private Connection connection;
    private AccountManager accountManager;

    public TransactionManager(AccountManager accountManager) {
        this.accountManager = accountManager;
        try {
            this.connection = DBconnection.getConnection();
            //initializeDatabase();
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw new RuntimeException("Failed to initialize TransactionManager", e);
        }
    }

    private void initializeDatabase() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS transactions ("
                + "transaction_id VARCHAR(20) PRIMARY KEY, "
                + "amount DECIMAL(15,2) NOT NULL, "
                + "type VARCHAR(50) NOT NULL, "
                + "from_account VARCHAR(20), "
                + "to_account VARCHAR(20), "
                + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    public Transaction createTransaction(double amount, String type, String fromAccount, String toAccount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction type cannot be empty");
        }
        if (fromAccount == null && toAccount == null) {
            throw new IllegalArgumentException("At least one account (from or to) must be specified");
        }

        String sql = "INSERT INTO transactions (transaction_id, amount, type, from_account, to_account) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            Transaction newTransaction = new Transaction(amount, type, fromAccount, toAccount);

            pstmt.setString(1, newTransaction.getTransactionId());
            pstmt.setDouble(2, amount);
            pstmt.setString(3, type);
            pstmt.setString(4, fromAccount);
            pstmt.setString(5, toAccount);
            pstmt.executeUpdate();

            // Update account balances
            if (toAccount != null) {
                Account acc = accountManager.getAccountByNumber(toAccount);
                if (acc != null) {
                    System.out.println("Depositing " + amount + " to account: " + acc.getAccountNumber());
                    acc.deposit(amount);
                    accountManager.updateAccountBalance(acc);
                }
            }
            if (fromAccount != null) {
                Account acc = accountManager.getAccountByNumber(fromAccount);
                if (acc != null) {
                    System.out.println("Withdrawing " + amount + " from account: " + acc.getAccountNumber());
                    acc.withdraw(amount);
                    accountManager.updateAccountBalance(acc);
                }
            }

            System.out.println("Transaction recorded: " + newTransaction.getTransactionId());
            return newTransaction;
        } catch (SQLException e) {
            System.err.println("Error creating transaction: " + e.getMessage());
            throw new RuntimeException("Failed to create transaction", e);
        }
    }

    public Transaction getTransactionById(String transactionId) {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, transactionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToTransaction(rs);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error retrieving transaction: " + e.getMessage());
            throw new RuntimeException("Database error", e);
        }
    }

    public List<Transaction> getTransactionsByAccount(String accountId) {
        String sql = "SELECT * FROM transactions WHERE from_account = ? OR to_account = ?";
        List<Transaction> transactions = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, accountId);
            pstmt.setString(2, accountId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
            return transactions;
        } catch (SQLException e) {
            System.err.println("Error retrieving transactions: " + e.getMessage());
            throw new RuntimeException("Database error", e);
        }
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getDouble("amount"),
                rs.getString("type"),
                rs.getString("from_account"),
                rs.getString("to_account")
        );
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