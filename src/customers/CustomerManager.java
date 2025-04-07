package customers;

import accounts.Account;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerManager {
    private List<Customer> customers;

    public CustomerManager() {
        this.customers = new ArrayList<>();
    }

    /**
     * Adds a new customer to the system
     * @param name Customer's full name
     * @param email Customer's email address
     * @param phoneNumber Customer's phone number
     * @return The newly created Customer object
     */
    public Customer addCustomer(String name, String email, String phoneNumber) {
        Customer newCustomer = new Customer(name, email, phoneNumber);
        customers.add(newCustomer);
        System.out.println("Customer added successfully with ID: " + newCustomer.getCustomerId());
        return newCustomer;
    }

    /**
     * Retrieves a customer by their ID
     * @param customerId The ID of the customer to find
     * @return The Customer object if found, null otherwise
     */
    public Customer getCustomerById(String customerId) {
        return customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all customers in the system
     * @return List of all customers
     */
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers); // Return a copy to prevent external modification
    }

    /**
     * Updates customer information
     * @param customerId ID of the customer to update
     * @param newName New name (null to keep existing)
     * @param newEmail New email (null to keep existing)
     * @param newPhoneNumber New phone number (null to keep existing)
     * @return true if update was successful, false otherwise
     */
    public boolean updateCustomer(String customerId, String newName, String newEmail, String newPhoneNumber) {
        Customer customer = getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found with ID: " + customerId);
            return false;
        }
    
        if (newName != null) {
            customer.setName(newName);
        }
        if (newEmail != null) {
            customer.setEmail(newEmail);
        }
        if (newPhoneNumber != null) {
            customer.setPhoneNumber(newPhoneNumber);
        }
    
        System.out.println("Customer updated successfully: " + customerId);
        return true;
    }

    /**
     * Removes a customer from the system
     * @param customerId ID of the customer to remove
     * @return true if removal was successful, false otherwise
     */
    public boolean removeCustomer(String customerId) {
        Customer customer = getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found with ID: " + customerId);
            return false;
        }

        customers.remove(customer);
        System.out.println("Customer removed successfully: " + customerId);
        return true;
    }

    /**
     * Adds an account to a customer
     * @param customerId ID of the customer
     * @param account Account to add
     * @return true if operation was successful, false otherwise
     */
    public boolean addAccountToCustomer(String customerId, Account account) {
        Customer customer = getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found with ID: " + customerId);
            return false;
        }

        customer.addAccount(account);
        return true;
    }

    /**
     * Lists all accounts for a specific customer
     * @param customerId ID of the customer
     */
    public void listCustomerAccounts(String customerId) {
        Customer customer = getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found with ID: " + customerId);
            return;
        }

        customer.listAccounts();
    }

    /**
     * Finds customers by name (case-insensitive partial match)
     * @param name Name or part of name to search for
     * @return List of matching customers
     */
    public List<Customer> findCustomersByName(String name) {
        String searchTerm = name.toLowerCase();
        return customers.stream()
                .filter(c -> c.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    /**
     * Finds a customer by email (exact match)
     * @param email Email to search for
     * @return The Customer object if found, null otherwise
     */
    public Customer findCustomerByEmail(String email) {
        return customers.stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst()
                .orElse(null);
    }
}