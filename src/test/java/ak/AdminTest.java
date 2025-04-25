package ak;

import ak.admins.Admin;
import ak.customer.Customer;
import ak.accounts.Account;
import ak.accounts.AccountManager;
import ak.customer.CustomerManager;
import ak.database.DBconnection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class AdminTest {
    private Admin admin;
    private CustomerManager customerManager;
    private AccountManager accountManager;
    private Customer customer;
    private Account account;

    @BeforeEach
    public void setUp() {
        DBconnection.clearDatabase();


        admin = new Admin("adminId", "Admin Name", "adminUsername", "passwordHash");
        customerManager = new CustomerManager();
        accountManager = new AccountManager();
        admin.setCustomerManager(customerManager);
        admin.setAccountManager(accountManager);

        customer = customerManager.addCustomer("name","email" , "1234567890", "ahmed", "passwordHash");
        account = accountManager.createCheckingAccount(customer.getCustomerId(), "Holder Name", 1000.0, 10000);

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
    public void testDeleteAccount() {
        boolean result = admin.deleteAccount(account.getAccountNumber());
        assertTrue(result);
    }


    @Test
    public void testDeleteCustomer() {
        accountManager.deleteAccount(account.getAccountNumber());
        boolean result = admin.deleteCustomer(customer.getCustomerId());
        assertTrue(result);
    }



    @Test
    public void testAddCustomer() {
        Customer customer = admin.addCustomer("Name", "email@example.com", "1234567890", "username", "passwordHash");
        assertNotNull(customer);
    }

    @Test
    public void testAddSavingsAccount() {
        Account account = admin.addSavingsAccount(customer.getCustomerId(), "holderName", 1000.0, 0.05);
        assertNotNull(account);
    }

    @Test
    public void testAddCheckingAccount() {
        Account account = admin.addCheckingAccount(customer.getCustomerId() , "holderName", 500.0, 100.0);
        assertNotNull(account);
    }
}