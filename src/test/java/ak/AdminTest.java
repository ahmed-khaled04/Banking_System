package ak;

import ak.admins.Admin;
import ak.customer.Customer;
import ak.accounts.Account;
import ak.accounts.AccountManager;
import ak.customer.CustomerManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class AdminTest {
    private Admin admin;
    private CustomerManager customerManager;
    private AccountManager accountManager;

    @BeforeEach
    public void setUp() {
        admin = new Admin("adminId", "Admin Name", "adminUsername", "passwordHash");
        customerManager = new CustomerManager();
        accountManager = new AccountManager();
        admin.setCustomerManager(customerManager);
        admin.setAccountManager(accountManager);
    }

    @Test
    public void testGetAdminId() {
        assertEquals("adminId", admin.getAdminId());
    }

    @Test
    public void testGetName() {
        assertEquals("Admin Name", admin.getName());
    }

    @Test
    public void testGetUsername() {
        assertEquals("adminUsername", admin.getUsername());
    }

    @Test
    public void testGetPasswordHash() {
        assertEquals("passwordHash", admin.getPasswordHash());
    }

    @Test
    public void testGetAllCustomers() {
        List<Customer> customers = admin.getAllCustomers();
        assertNotNull(customers);
    }

    @Test
    public void testGetAllAccounts() {
        List<Account> accounts = admin.getAllAccounts();
        assertNotNull(accounts);
    }

    @Test
    public void testDeleteCustomer() {
        boolean result = admin.deleteCustomer("customerId");
        assertTrue(result);
    }

    @Test
    public void testDeleteAccount() {
        boolean result = admin.deleteAccount("accountNumber");
        assertTrue(result);
    }

    @Test
    public void testAddCustomer() {
        Customer customer = admin.addCustomer("Name", "email@example.com", "1234567890", "username", "passwordHash");
        assertNotNull(customer);
    }

    @Test
    public void testAddSavingsAccount() {
        Account account = admin.addSavingsAccount("customerId", "holderName", 1000.0, 0.05);
        assertNotNull(account);
    }

    @Test
    public void testAddCheckingAccount() {
        Account account = admin.addCheckingAccount("customerId", "holderName", 500.0, 100.0);
        assertNotNull(account);
    }
}