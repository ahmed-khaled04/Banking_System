package ak.integration;

import ak.customer.Customer;
import ak.customer.CustomerManager;
import ak.Authentication.AuthService;
import ak.Authentication.PasswordUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordSecurity_TC7_1Test {

    // @Test
    // @DisplayName("TC1 - Customer Password Encryption → Validate on Login")
    void testCustomerEncryptedPasswordLoginValidation() {
        CustomerManager customerManager = new CustomerManager();
        String plainPassword = "Secure@123";
        String encryptedPassword = PasswordUtils.hashPassword(plainPassword);

        // Add customer with encrypted password
        customerManager.addCustomer("customer name", "customer001@gmail.com", "01031674134", "username", encryptedPassword);

        // Attempt to authenticate customer with correct password
        Customer customer = customerManager.authenticateCustomer("username", encryptedPassword);
        assertNotNull(customer);
        assertEquals("username", customer.getUsername());
        System.out.println("Login with encrypted password succeeded.");

        // Attempt to authenticate customer with incorrect password
        Customer failedLogin = customerManager.authenticateCustomer("username", PasswordUtils.hashPassword("WrongPass"));
        assertNull(failedLogin);
        System.out.println("Login failed with incorrect password.");
    }
}
