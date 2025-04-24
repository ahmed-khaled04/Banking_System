package ak.accounts;

import ak.database.DBconnection;
import ak.transactions.Transaction;

import java.sql.*;
import java.util.*;

public class AccountManager {
    private Connection connection;

    public AccountManager() {
        try {
            this.connection = DBconnection.getConnection();
            // initializeDatabase();
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw new RuntimeException("Failed to initialize AccountManager", e);
        }
    }

    private void initializeDatabase() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS accounts ("
                + "account_number VARCHAR(20) PRIMARY KEY, "
                + "account_holder_name VARCHAR(100) NOT NULL, "
                + "balance DECIMAL(15,2) NOT NULL, "
                + "account_type VARCHAR(20) NOT NULL, "
                + "interest_rate DECIMAL(5,2), " 
                + "overdraft_limit DECIMAL(15,2))"; 
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    public Account createSavingsAccount(String customerId, String holderName, double initialDeposit, double interestRate) {
        Account account = new SavingsAccount(customerId, holderName, initialDeposit, interestRate , true);
        createAccount(account);
        return account;
    }

    public Account createCheckingAccount(String customerId, String holderName, double initialDeposit, double overdraftLimit) {
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative");
        }
        Account account = new CheckingAccount(customerId, holderName, initialDeposit, overdraftLimit , true);
        createAccount(account);
        return account;
    }

    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts (account_number, customer_id, account_holder_name, balance, account_type, interest_rate, overdraft_limit) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, account.getAccountNumber());
            pstmt.setString(2, account.getCustomerId());
            pstmt.setString(3, account.getAccountHolderName());
            pstmt.setDouble(4, account.getBalance());
            pstmt.setString(5, account instanceof SavingsAccount ? "Savings" : "Checking");
            pstmt.setObject(6, account instanceof SavingsAccount ? ((SavingsAccount) account).getInterestRate() : null);
            pstmt.setObject(7, account instanceof CheckingAccount ? ((CheckingAccount) account).getOverdraftLimit() : null);
            pstmt.executeUpdate();
            System.out.println("Account created successfully for " + account.getAccountHolderName());
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating account: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAccount(String accountNumber) {
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account deleted.");
                return true;
            } else {
                System.out.println("Account not found.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting account: " + e.getMessage());
            return false;
        }
    }

    public boolean processTransaction(Transaction transaction) {
        try {
            connection.setAutoCommit(false);
    
            String fromAccountNumber = transaction.getFromAccount();
            String toAccountNumber = transaction.getToAccount();
            double amount = transaction.getAmount();
    

            //Deposit
            if (fromAccountNumber == null && toAccountNumber != null) {
                Account toAccount = getAccountByNumber(toAccountNumber);
                if (toAccount == null) {
                    System.out.println("To account not found: " + toAccountNumber);
                    return false;
                }
                toAccount.deposit(amount);
                updateAccountBalance(toAccount);
            } else if (fromAccountNumber != null && toAccountNumber == null) {
                Account fromAccount = getAccountByNumber(fromAccountNumber);
                if (fromAccount == null) {
                    System.out.println("From account not found: " + fromAccountNumber);
                    return false;
                }
                if (fromAccount.getBalance() < amount) {
                    System.out.println("Insufficient funds in account: " + fromAccountNumber);
                    return false;
                }
                fromAccount.withdraw(amount);
                updateAccountBalance(fromAccount);
            } else if (fromAccountNumber != null && toAccountNumber != null) {
                Account fromAccount = getAccountByNumber(fromAccountNumber);
                Account toAccount = getAccountByNumber(toAccountNumber);
    
                if (fromAccount == null || toAccount == null) {
                    System.out.println("One or both accounts not found.");
                    return false;
                }
                if (fromAccount.getBalance() < amount) {
                    System.out.println("Insufficient funds in account: " + fromAccountNumber);
                    return false;
                }
    
                fromAccount.withdraw(amount);
                toAccount.deposit(amount);
    
                updateAccountBalance(fromAccount);
                updateAccountBalance(toAccount);
            } else {
                System.out.println("Invalid transaction: both accounts are null.");
                return false;
            }
    
            connection.commit();
            System.out.println("Transaction processed successfully: " + transaction.getTransactionId());
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
            }
            System.err.println("Error processing transaction: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                System.err.println("Error resetting auto-commit: " + ex.getMessage());
            }
        }
    }

    public void applyInterestToSavingsAccounts() {
        String sql = "SELECT * FROM accounts WHERE account_type = 'Savings'";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String accountNumber = rs.getString("account_number");
                double balance = rs.getDouble("balance");
                double interestRate = rs.getDouble("interest_rate");
                String customerId = rs.getString("customer_id");
    
                SavingsAccount account = new SavingsAccount(customerId, accountNumber, balance, interestRate , rs.getBoolean("activated"));
                account.addInterest();
    
                updateAccountBalance(account);
            }
            System.out.println("Interest applied to all savings accounts.");
        } catch (SQLException e) {
            System.err.println("Error applying interest: " + e.getMessage());
        }
    }
    public void displayAllAccounts() {
        String sql = "SELECT * FROM accounts";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                System.out.println("Account Number: " + rs.getString("account_number"));
                System.out.println("Holder Name: " + rs.getString("account_holder_name"));
                System.out.println("Balance: $" + rs.getDouble("balance"));
                System.out.println("Account Type: " + rs.getString("account_type"));
                System.out.println("----------------------------");
            }
        } catch (SQLException e) {
            System.err.println("Error displaying accounts: " + e.getMessage());
        }
    }

private final Map<String, Account> accountCache = new HashMap<>();

public Account getAccountByNumber(String accountNumber) {
    // Check if the account is already in the cache
    if (accountCache.containsKey(accountNumber)) {
        return accountCache.get(accountNumber);
    }

    String sql = "SELECT * FROM accounts WHERE account_number = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setString(1, accountNumber);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            String customerId = rs.getString("customer_id");
            String accountType = rs.getString("account_type");
            String accountHolderName = rs.getString("account_holder_name");
            double balance = rs.getDouble("balance");
            Account account;
            if ("Savings".equalsIgnoreCase(accountType)) {
                double interestRate = rs.getDouble("interest_rate");
                account = new SavingsAccount(customerId, accountHolderName, balance, interestRate , accountNumber , rs.getBoolean("activated"));
            } else if ("Checking".equalsIgnoreCase(accountType)) {
                double overdraftLimit = rs.getDouble("overdraft_limit");
                account = new CheckingAccount(customerId, accountHolderName, balance, overdraftLimit , accountNumber , rs.getBoolean("activated"));
            } else {
                return null;
            }
            // Cache the account for future use
            accountCache.put(accountNumber, account);
            return account;
        }
    } catch (SQLException e) {
        System.err.println("Error retrieving account: " + e.getMessage());
    }
    return null;
}

    public void updateAccountBalance(Account account) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, account.getBalance());
            pstmt.setString(2, account.getAccountNumber());
            pstmt.executeUpdate();
        }
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

    public List<Account> getAccountsByCustomerId(String customerId) {
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";
        List<Account> accounts = new ArrayList<>();
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String accountType = rs.getString("account_type");
                if ("Savings".equalsIgnoreCase(accountType)) {
                    accounts.add(new SavingsAccount(
                        rs.getString("customer_id"),
                        rs.getString("account_holder_name"),
                        rs.getDouble("balance"),
                        rs.getDouble("interest_rate"),
                        rs.getString("account_number"),
                        rs.getBoolean("activated")
                    ));
                } else if ("Checking".equalsIgnoreCase(accountType)) {
                    accounts.add(new CheckingAccount(
                        rs.getString("customer_id"),
                        rs.getString("account_holder_name"),
                        rs.getDouble("balance"),
                        rs.getDouble("overdraft_limit"),
                        rs.getString("account_number") ,
                        rs.getBoolean("activated")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public List<Account> getAllAccounts() {
        String sql = "SELECT * FROM accounts";
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String accountType = rs.getString("account_type");
                if ("Savings".equalsIgnoreCase(accountType)) {
                    accounts.add(new SavingsAccount(
                        rs.getString("customer_id"),
                        rs.getString("account_holder_name"),
                        rs.getDouble("balance"),
                        rs.getDouble("interest_rate"),
                        rs.getString("account_number"),
                        rs.getBoolean("activated")
                    ));
                } else if ("Checking".equalsIgnoreCase(accountType)) {
                    accounts.add(new CheckingAccount(
                        rs.getString("customer_id"),
                        rs.getString("account_holder_name"),
                        rs.getDouble("balance"),
                        rs.getDouble("overdraft_limit"),
                        rs.getString("account_number"),
                        rs.getBoolean("activated")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }


    public boolean updateAccountActivation(String accountNumber, boolean activated) {
        String query = "UPDATE accounts SET activated = ? WHERE account_number = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setBoolean(1, activated);
            stmt.setString(2, accountNumber);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean isAccountOwnedByCustomer(String accountNumber, String customerId) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE account_number = ? AND customer_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, customerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Return true if the count is greater than 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if an error occurs or the account does not belong to the customer
    }





}