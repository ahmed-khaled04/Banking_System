package ak.customer;

import ak.accounts.Account;
import ak.accounts.CheckingAccount;
import ak.accounts.SavingsAccount;
import ak.database.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CustomerManager {
    
    public Customer addCustomer(String name, String email, String phoneNumber , String username, String passwordHash) {
        String customerId = "CUST-" + UUID.randomUUID().toString().substring(0, 8);
        String sql = "INSERT INTO customers (customer_id, name, email, phone_number , username , password_hash) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, phoneNumber);
            pstmt.setString(5, username);
            pstmt.setString(6, passwordHash);
            pstmt.executeUpdate();
            
            Customer newCustomer = new Customer(customerId, name, email, phoneNumber , username, passwordHash);
            System.out.println("Customer added successfully with ID: " + customerId);
            return newCustomer;
            
        } catch (SQLException e) {
            System.out.println("Error adding customer: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves a customer by their ID
     * @param customerId The ID of the customer to find
     * @return The Customer object if found, null otherwise
     */
    public Customer getCustomerById(String customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Customer(
                    rs.getString("customer_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getString("username"),
                    rs.getString("password_hash")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving customer: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all customers in the system
     * @return List of all customers
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        
        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                customers.add(new Customer(
                    rs.getString("customer_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getString("username"),
                    rs.getString("password_hash")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving customers: " + e.getMessage());
        }
        return customers;
    }

    /**
     * Updates customer information
     * @param customerId ID of the customer to update
     * @param newName New name (null to keep existing)
     * @param newEmail New email (null to keep existing)
     * @param newPhoneNumber New phone number (null to keep existing)
     * @return true if update was successful, false otherwise
     */
    public boolean updateCustomer(String customerId, String newName, String newEmail, String newPhoneNumber , String newUsername, String newPasswordHash) {
        // First get the current customer data
        Customer customer = getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found with ID: " + customerId);
            return false;
        }
        
        // Use existing values if new ones are null
        String name = newName != null ? newName : customer.getName();
        String email = newEmail != null ? newEmail : customer.getEmail();
        String phoneNumber = newPhoneNumber != null ? newPhoneNumber : customer.getPhoneNumber();
        String username = newUsername != null ? newUsername : customer.getUsername();
        String passwordHash = newPasswordHash != null ? newPasswordHash : customer.getPasswordHash();
        
        String sql = "UPDATE customers SET name = ?, email = ?, phone_number = ?, username = ?, password_hash = ? WHERE customer_id = ?";
        
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phoneNumber);
            pstmt.setString(4, customerId);
            pstmt.setString(5, username);
            pstmt.setString(6, passwordHash);
            
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Customer updated successfully: " + customerId);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e.getMessage());
        }
        return false;
    }

    /**
     * Removes a customer from the system
     * @param customerId ID of the customer to remove
     * @return true if removal was successful, false otherwise
     */
    public boolean removeCustomer(String customerId) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            int rowsDeleted = pstmt.executeUpdate();
            
            if (rowsDeleted > 0) {
                System.out.println("Customer removed successfully: " + customerId);
                return true;
            } else {
                System.out.println("Customer not found with ID: " + customerId);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error removing customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adds an account to a customer
     * @param customerId ID of the customer
     * @param account Account to add
     * @return true if operation was successful, false otherwise
     */
    public boolean addAccountToCustomer(String customerId, Account account) {
        // First verify customer exists
        if (getCustomerById(customerId) == null) {
            System.out.println("Customer not found with ID: " + customerId);
            return false;
        }
        
        // The actual account creation should be handled by AccountManager
        // This method would just link the account to customer in database
        String sql = "UPDATE accounts SET customer_id = ? WHERE account_number = ?";
        
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            pstmt.setString(2, account.getAccountNumber());
            
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
            
        } catch (SQLException e) {
            System.out.println("Error adding account to customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lists all accounts for a specific customer
     * @param customerId ID of the customer
     */
    public void listCustomerAccounts(String customerId) {
        // This would be better implemented in AccountManager
        // Here we'll just demonstrate a database approach
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";
        
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            System.out.println("Accounts for customer ID: " + customerId);
            System.out.println("----------------------------");
            
            while (rs.next()) {
                System.out.println("Account Number: " + rs.getString("account_number"));
                System.out.println("Type: " + rs.getString("account_type"));
                System.out.println("Balance: $" + rs.getDouble("balance"));
                System.out.println("----------------------------");
            }
            
        } catch (SQLException e) {
            System.out.println("Error listing customer accounts: " + e.getMessage());
        }
    }

    /**
     * Finds customers by name (case-insensitive partial match)
     * @param name Name or part of name to search for
     * @return List of matching customers
     */
    public List<Customer> findCustomersByName(String name) {
        List<Customer> matchingCustomers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE LOWER(name) LIKE LOWER(?)";
        
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                matchingCustomers.add(new Customer(
                    rs.getString("customer_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getString("username"),
                    rs.getString("password_hash")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error finding customers by name: " + e.getMessage());
        }
        return matchingCustomers;
    }

    /**
     * Finds a customer by email (exact match)
     * @param email Email to search for
     * @return The Customer object if found, null otherwise
     */
    public Customer findCustomerByEmail(String email) {
        String sql = "SELECT * FROM customers WHERE email = ?";
        
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email.trim());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Customer(
                    rs.getString("customer_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getString("username"),
                    rs.getString("password_hash")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error finding customer by email: " + e.getMessage());
        }
        return null;
    }

    public Customer authenticateCustomer(String username , String password){
        String sql = "SELECT * FROM customers WHERE username = ? AND password_hash = ?";
        
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username.trim());
            pstmt.setString(2, password.trim());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Customer(
                    rs.getString("customer_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getString("username"),
                    rs.getString("password_hash")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error authenticating customer: " + e.getMessage());
        }
        return null;
    }

    public Customer getCustomerByUsername(String username) {
        String sql = "SELECT * FROM customers WHERE username = ?";
        
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username.trim());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Customer(
                    rs.getString("customer_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone_number"),
                    rs.getString("username"),
                    rs.getString("password_hash")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error authenticating customer: " + e.getMessage());
        }
        return null;
    }

}