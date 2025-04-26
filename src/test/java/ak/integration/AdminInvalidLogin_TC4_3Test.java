package ak.integration;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import ak.admins.*;

public class AdminInvalidLogin_TC4_3Test {

    // @Test
    // @DisplayName("TC4.3 - Invalid Login → Exception Handling")
    void testInvalidAdminLogin() {
        AdminManager adminManager = new AdminManager();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            adminManager.authenticateAdmin("admin", "wrongpass123"); // Wrong hashed password
        });

        String expectedMessage = "Invalid credentials";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println("Invalid login correctly handled with exception: " + actualMessage);
    }
}
