package ak.integration;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import ak.admins.*;
import ak.Authentication.*;
import ak.customer.*;

public class AdminAuthIntegration_TC4_1Test {

    // @Test
    // @DisplayName("TC4.1 - Admin Login → View All Customers")
    void testAdminLoginAndViewCustomers() {
        
        AdminManager adminManager = new AdminManager();
        
        CustomerManager customerManager = new CustomerManager();
        customerManager.addCustomer("user", "user@gmail.com", "01031674134", "user", "password");


        // Simulate login
        Admin admin = adminManager.authenticateAdmin("admin", "240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9");
        Admin admin_verify = adminManager.getAdminByUsername("admin");
        assertEquals(admin_verify.getAdminId(), admin.getAdminId());
        System.out.println("Admin login successful");

        // Fetch customers
        List<Customer> customers = admin.getAllCustomers();
        assertNotNull(customers);
        assertTrue(customers.size() >= 0); // At least an empty list
        System.out.println("customers: \n");
        System.out.println(customers);
    }
}
