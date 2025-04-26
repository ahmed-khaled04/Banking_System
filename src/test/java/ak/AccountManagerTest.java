package ak;

import ak.accounts.Account;
import ak.accounts.AccountManager;
import ak.accounts.CheckingAccount;
import ak.accounts.SavingsAccount;
import ak.customer.Customer;
import ak.customer.CustomerManager;
import ak.database.DBconnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountManagerTest {
    private AccountManager accountManager;
    private CustomerManager customerManager;
    private Customer testCustomer;

    @BeforeEach
    public void setUp() throws SQLException {
        DBconnection.clearDatabase(); // Clear the database before each test
        accountManager = new AccountManager();
        customerManager = new CustomerManager();
        testCustomer = customerManager.addCustomer("John Doe", "email.com", "1111", "username1", "passwordHash1");
    }

    @Test
    public void testCreateSavingsAccount() {
        Account account = accountManager.createSavingsAccount(testCustomer.getCustomerId(), "John Doe", 1000.0, 2.5);
        assertNotNull(account, "Savings account should be created successfully.");
        assertEquals(testCustomer.getCustomerId(), account.getCustomerId());
        assertEquals("John Doe", account.getAccountHolderName());
        assertEquals(1000.0, account.getBalance());
        assertTrue(account.isActivated());
    }

    @Test
    public void testCreateCheckingAccount() {
        Account account = accountManager.createCheckingAccount(testCustomer.getCustomerId(), "Jane Doe", 500.0, 1000.0);
        assertNotNull(account, "Checking account should be created successfully.");
        assertEquals(testCustomer.getCustomerId(), account.getCustomerId());
        assertEquals("Jane Doe", account.getAccountHolderName());
        assertEquals(500.0, account.getBalance());
        assertTrue(account.isActivated());
    }

    @Test
    public void testDeleteAccount() {
        Account account = accountManager.createSavingsAccount(testCustomer.getCustomerId(), "John Doe", 1000.0, 2.5);
        boolean result = accountManager.deleteAccount(account.getAccountNumber());
        assertTrue(result, "Account should be deleted successfully.");
        assertNull(accountManager.getAccountByNumber(account.getAccountNumber()), "Account should no longer exist.");
    }

    @Test
    public void testGetAccountByNumber() {
        Account account = accountManager.createSavingsAccount(testCustomer.getCustomerId(), "John Doe", 1000.0, 2.5);
        Account retrievedAccount = accountManager.getAccountByNumber(account.getAccountNumber());
        assertNotNull(retrievedAccount, "Account should be retrieved successfully.");
        assertEquals(account.getAccountNumber(), retrievedAccount.getAccountNumber());
    }

    @Test
    public void testGetAllAccounts() {
        Customer customer2 = customerManager.addCustomer("Jane Doe", "email2.com", "2222", "username2", "passwordHash2");
        accountManager.createSavingsAccount(testCustomer.getCustomerId(), "John Doe", 1000.0, 2.5);
        accountManager.createCheckingAccount(customer2.getCustomerId(), "Jane Doe", 500.0, 1000.0);

        List<Account> accounts = accountManager.getAllAccounts();
        assertNotNull(accounts, "Accounts list should not be null.");
        assertEquals(2, accounts.size(), "There should be 2 accounts in the database.");
    }

    @Test
    public void testUpdateAccountActivation() {
        Account account = accountManager.createSavingsAccount(testCustomer.getCustomerId(), "John Doe", 1000.0, 2.5);
        boolean result = accountManager.updateAccountActivation(account.getAccountNumber(), false);
        assertTrue(result, "Account activation status should be updated successfully.");
        assertFalse(accountManager.getAccountByNumber(account.getAccountNumber()).isActivated());
    }

    @Test
    public void testProcessTransaction() {
        Customer customer2 = customerManager.addCustomer("Jane Doe", "email2.com", "2222", "username2", "passwordHash2");

        Account source = accountManager.createSavingsAccount(testCustomer.getCustomerId(), "John Doe", 1000.0, 2.5);
        Account destination = accountManager.createSavingsAccount(customer2.getCustomerId(), "Jane Doe", 500.0, 2.5);

        boolean result = accountManager.processTransaction(new ak.transactions.Transaction(200.0, "Transfer", source.getAccountNumber(), destination.getAccountNumber(), null));
        assertTrue(result, "Transaction should be processed successfully.");
    }
}