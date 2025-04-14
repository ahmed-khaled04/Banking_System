package ak;


import ak.customer.Customer;
import ak.customer.CustomerManager;
import ak.database.DBconnection;

import java.sql.*;

public class BankingApp {
    public static void main(String[] args) {
        // Initialize CustomerManager
        CustomerManager customerManager = new CustomerManager();
        
        System.out.println("=== Testing CustomerManager ===");
        
        // Test adding customers
        System.out.println("\nAdding customers...");
        Customer customer1 = customerManager.addCustomer("John Doe", "john.doe@example.com", "555-0101");
        Customer customer2 = customerManager.addCustomer("Jane Smith", "jane.smith@example.com", "555-0102");
        
        // Display added customers
        System.out.println("\nAdded customers:");
        displayCustomer(customer1);
        displayCustomer(customer2);
        
        // Test getCustomerById
        System.out.println("\nRetrieving customer by ID...");
        Customer retrievedCustomer = customerManager.getCustomerById(customer1.getCustomerId());
        System.out.println("Retrieved customer:");
        displayCustomer(retrievedCustomer);
        
        // Test getAllCustomers
        System.out.println("\nAll customers in system:");
        for (Customer c : customerManager.getAllCustomers()) {
            displayCustomer(c);
        }
        
        // Test updateCustomer
        System.out.println("\nUpdating customer...");
        boolean updateSuccess = customerManager.updateCustomer(
            customer1.getCustomerId(), 
            "Johnathan Doe",  // new name
            null,             // keep same email
            "555-0103"        // new phone
        );
        System.out.println("Update " + (updateSuccess ? "successful" : "failed"));
        
        // Verify update
        Customer updatedCustomer = customerManager.getCustomerById(customer1.getCustomerId());
        System.out.println("Updated customer details:");
        displayCustomer(updatedCustomer);
        
        // Test find by email
        System.out.println("\nFinding customer by email...");
        Customer foundByEmail = customerManager.findCustomerByEmail("jane.smith@example.com");
        System.out.println("Found customer:");
        displayCustomer(foundByEmail);
        
        // Test find by name
        System.out.println("\nFinding customers by name (partial match)...");
        System.out.println("Customers with 'Smith' in name:");
        for (Customer c : customerManager.findCustomersByName("Smith")) {
            displayCustomer(c);
        }
        
        // Test removeCustomer
        // System.out.println("\nRemoving customer...");
        // boolean removeSuccess = customerManager.removeCustomer(customer2.getCustomerId());
        // System.out.println("Removal " + (removeSuccess ? "successful" : "failed"));
        
        // Verify removal
        System.out.println("\nRemaining customers:");
        for (Customer c : customerManager.getAllCustomers()) {
            displayCustomer(c);
        }
    }
    
    private static void displayCustomer(Customer customer) {
        if (customer == null) {
            System.out.println("Customer is null");
            return;
        }
        System.out.println("ID: " + customer.getCustomerId());
        System.out.println("Name: " + customer.getName());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Phone: " + customer.getPhoneNumber());
        System.out.println("----------------------");
    }
    
}