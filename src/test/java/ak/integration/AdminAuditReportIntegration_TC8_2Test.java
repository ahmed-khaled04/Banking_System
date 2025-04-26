package ak.integration;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import ak.accounts.AccountManager;
import ak.admins.*;
import ak.customer.Customer;
import ak.customer.CustomerManager;
import ak.transactions.Transaction;
import ak.transactions.TransactionManager;
import ak.database.*;
import ak.Authentication.*;
import java.util.List;

public class AdminAuditReportIntegration_TC8_2Test {

    // @Test
    // @DisplayName("TC8.2 - Admin Audit Report → View All Customers + Transactions")
    void testAdminAuditReportGeneration() {
        // Initialize required modules

        PasswordUtils pu = new PasswordUtils();
        AdminManager adminManager = new AdminManager();
        AccountManager accountManager = new AccountManager();
        TransactionManager transactionManager = new TransactionManager(accountManager);
        Admin admin = adminManager.authenticateAdmin("admin", pu.hashPassword("admin123"));
        
        // Pre-test verification
        System.out.println("\n[1/4] Verifying preconditions...");

        assertNotNull(admin, "Admin should not be null");

        // Step 1: Request audit report
        System.out.println("\n[2/4] Generating audit report...");
        System.out.println("Customers: \n");
        System.out.println(admin.getAllCustomers());
        System.out.println("Accounts: \n");
        System.out.println(admin.getAllAccounts());

   
     }
}