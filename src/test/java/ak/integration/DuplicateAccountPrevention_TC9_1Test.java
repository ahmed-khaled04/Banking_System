package ak.integration;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import ak.accounts.AccountManager;
import ak.customer.*;


public class DuplicateAccountPrevention_TC9_1Test {

    // Test data - EDIT THESE VALUES AS NEEDED
    private static final String name = "user";
    private static final String email = "user@gmail.com";
    private static final String username = "user";
    private static final String password_hash = "password";
    private static final String number = "01060758333";

    // @Test
    // @DisplayName("TC9.1 - Duplicate Account Creation Prevention")
    void testDuplicateAccountPrevention() {
        CustomerManager customerManager = new CustomerManager();

        // First creation (should succeed)
        Customer firstCustomer = customerManager.addCustomer(name, email, number, username, password_hash);
        assertNotNull(firstCustomer, "First customer creation should succeed");

        // Attempt duplicate creation
        Customer duplicateCustomer = customerManager.addCustomer(name, email, number, username, password_hash);
        
        // Verify duplicate prevention behavior
        if (duplicateCustomer == null) {
            // Option 1: Method returns null for duplicates
            System.out.println("Duplicate prevention working: Method returned null");
        } else if (duplicateCustomer.getCustomerId().equals(firstCustomer.getCustomerId())) {
            // Option 2: Method returns existing customer
            System.out.println("Duplicate prevention working: Returned existing customer");
        } else {
            // Option 3: Duplicate was actually created (test should fail)
            fail("Duplicate customer was created: " + duplicateCustomer.getCustomerId());
        }

        // Additional verification - should only be one customer with this username
        Customer retrieved = customerManager.getCustomerByUsername(username);
        assertNotNull(retrieved, "Original customer should exist");
        assertEquals(firstCustomer.getCustomerId(), retrieved.getCustomerId(), 
            "Should retrieve the original customer, not a duplicate");
    }
}

    