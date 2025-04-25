package ak;

import ak.admins.Admin;
import ak.admins.AdminManager;
import ak.database.DBconnection;

// import ak.customer.Customer;
// import ak.accounts.Account;
// import ak.accounts.AccountManager;
// import ak.customer.CustomerManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

public class AdminManagerTest {
    private AdminManager adminManager;
    private Admin admin;

    @BeforeEach
    public void setUp() throws SQLException {

        DBconnection.clearDatabase(); 

        adminManager = new AdminManager();
        
        // Assuming you have a method to add an admin to the database for testing
        // testing purposes. You need to implement this based on your DB schema.
        adminManager.addAdmin("Test Admin", "testAdminUsername", "testPasswordHash");
        
        admin = adminManager.getAdminByUsername("testAdminUsername");
    }

    @Test
    public void testAuthenticateAdmin() {
        Admin authenticatedAdmin = adminManager.authenticateAdmin("testAdminUsername", "testPasswordHash");
        assertNotNull(authenticatedAdmin);
    }

    @Test
    public void testAddAdmin() {
        boolean result = adminManager.addAdmin("New Admin", "newAdminUsername", "newPasswordHash");
        assertTrue(result);
    }

    @Test
    public void testGetAdminByUsername() {
        Admin retrievedAdmin = adminManager.getAdminByUsername("testAdminUsername");
        assertNotNull(retrievedAdmin);
    }
}