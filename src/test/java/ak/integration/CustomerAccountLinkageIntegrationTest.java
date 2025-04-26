
package ak.integration;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import ak.customer.Customer;
import ak.customer.CustomerManager;
import ak.accounts.Account;
import ak.accounts.AccountManager;
import java.util.List;

public class CustomerAccountLinkageIntegrationTest {

    // @Test
    // @DisplayName("TC2.1 - Create Customer → Link Account")
    public void testCustomerAccountLinkage() {
        // Test data
        String name = "Alex";
        String email = "alex@example.com";
        String phoneNumber = "1234567890";
        String username = "alex_user";
        String passwordHash = "hashed_password_123";
        double initialDeposit = 0;
        double overdraftLimit = 1000;

        // Step 1: Create customer
        CustomerManager customerManager = new CustomerManager();
        Customer customer = customerManager.addCustomer(name, email, phoneNumber, username, passwordHash);
        
        // Re-fetch customer to ensure persistence
        customer = customerManager.getCustomerById(customer.getCustomerId());
        assertNotNull(customer, "Customer should exist in database");
        assertEquals(name, customer.getName());
        assertEquals(email, customer.getEmail());

        // Step 2: Create and link account
        AccountManager accountManager = new AccountManager();
        Account account = accountManager.createCheckingAccount(customer.getCustomerId(), name, initialDeposit, overdraftLimit);
        assertNotNull(account, "Account creation failed");
        assertEquals(customer.getCustomerId(), account.getCustomerId());

        // Step 3: Verify linkage
        List<Account> customerAccounts = accountManager.getAccountsByCustomerId(customer.getCustomerId());
        assertFalse(customerAccounts.isEmpty(), "Customer should have at least one account");
        assertEquals(account.getAccountNumber(), customerAccounts.get(0).getAccountNumber(), 
            "Created account should be linked to customer");
    }
}