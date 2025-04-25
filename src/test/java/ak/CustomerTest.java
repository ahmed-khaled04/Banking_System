package ak;

import ak.accounts.Account;
import ak.accounts.CheckingAccount;
import ak.accounts.SavingsAccount;
import ak.customer.Customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class CustomerTest {

    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer("123", "Jane Doe", "jane.doe@example.com", "1234567890");
    }

    // CUSTOMER CREATION
    // Test valid customer creation
    @Test
    public void testValidCustomerCreation() {
        assertEquals("123", customer.getCustomerId());
        assertEquals("Jane Doe", customer.getName());
        assertNotNull(customer.getAccounts());
    }

    // Test customer creation with null name
    @Test
    public void testCustomerCreationWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Customer("456", null, "jane.doe@example.com", "1234567890");
        });
    }

    // Test customer creation with empty ID
    @Test
    public void testCustomerCreationWithEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Customer("", "John Doe", "john.doe@example.com", "1234567890");
        });
    }

    // ACCOUNT ASSOCIATION
    // Test adding an account
    @Test
    public void testAddAccountToCustomer() {
        Account account = new SavingsAccount("123", "Jane Doe", 1000.0, 2.5, true);
        customer.addAccount(account);

        List<Account> accounts = customer.getAccounts();
        assertEquals(1, accounts.size());
        assertEquals(account, accounts.get(0));
    }

    // Test retrieving multiple accounts
    @Test
    public void testRetrieveMultipleAccounts() {
        Account acc1 = new CheckingAccount("123", "Jane Doe", 500.0, 500.0, "ACC124", true);
        Account acc2 = new SavingsAccount("123", "Jane Doe", 1500.0, 2.5 , true);

        customer.addAccount(acc1);
        customer.addAccount(acc2);

        List<Account> accounts = customer.getAccounts();
        assertTrue(accounts.contains(acc1));
        assertTrue(accounts.contains(acc2));
        assertEquals(2, accounts.size());
    }

    // Test adding null account
    @Test
    public void testAddNullAccount() {
        assertThrows(IllegalArgumentException.class, () -> {
            customer.addAccount(null);
        });
    }
}