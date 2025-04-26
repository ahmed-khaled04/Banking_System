package ak.integration;

import ak.accounts.AccountManager;
import ak.database.DBconnection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SystemUtility_TC6_2Test {

    // @Test
    // @DisplayName("TC6.2 - DBconnection Failure Simulation")
    void testDBConnectionFailureSimulation() {
        // Simulate DB failure by setting an invalid test connection
        Connection mockConnection = null;
        DBconnection.setTestConnection(mockConnection);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            AccountManager accountManager = new AccountManager();
            accountManager.createCheckingAccount("Cust-DBFail", "failAccount", 1000, 0);
        });

        String expected = "Failed";
        String actual = exception.getMessage();
        assertTrue(actual.contains(expected));
        System.out.println("Database failure correctly simulated and handled.");
    }
}
