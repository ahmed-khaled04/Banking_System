
package ak.integration;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import ak.customer.Customer;
import ak.customer.CustomerManager;
import ak.accounts.Account;
import ak.accounts.AccountManager;
import java.util.List;

public class CustomerAccountViewIntegrationTest {

    // @Test
    // @DisplayName("TC2.2 - Customer → View Account Details")
    public void testViewCustomerAccountDetails() {
        // Test data
        String name = "Aly";
        String email = "aly@example.com";
        String phoneNumber = "1234567811";
        String username = "aly_user";
        String passwordHash = "hashed_password_123";
        double initialDeposit = 500.0;
        double overdraftLimit = 1000.0;

        // 1. Create and verify customer
        CustomerManager customerManager = new CustomerManager();
        Customer customer = customerManager.addCustomer(name, email, phoneNumber, username, passwordHash);
        
        // Re-fetch customer to ensure persistence
        // customer = customerManager.getCustomerById(customer.getCustomerId());
        assertNotNull(customer, "Customer should exist in database");
        
        // 2. Create and verify account
        AccountManager accountManager = new AccountManager();
        Account account = accountManager.createCheckingAccount(
            customer.getCustomerId(), 
            name, 
            initialDeposit, 
            overdraftLimit
        );
        assertNotNull(account, "Account creation failed");

        // 3. Retrieve and verify account details
        List<Account> customerAccounts = accountManager.getAccountsByCustomerId(customer.getCustomerId());
        System.out.println(customerAccounts);
        assertFalse(customerAccounts.isEmpty(), "No accounts found for customer");
        System.out.println("account number: " + account.getAccountNumber() + "\nbalance: " + account.getBalance() + "\naccount type: " + account.getAccountType());

        
        Account retrievedAccount = customerAccounts.get(0);
        assertAll("Account details verification",
            () -> assertEquals(account.getAccountNumber(), retrievedAccount.getAccountNumber()),
            () -> assertEquals(initialDeposit, retrievedAccount.getBalance(), 0.01),
            () -> assertEquals("CheckingAccount", retrievedAccount.getAccountType()),
            () -> assertTrue(retrievedAccount.isActivated()),
            () -> assertEquals(customer.getCustomerId(), retrievedAccount.getCustomerId())
        );
    }
}