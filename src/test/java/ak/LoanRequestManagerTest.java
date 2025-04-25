package ak;

import ak.loans.LoanRequestManager;
import ak.loans.LoanRequest;
import ak.accounts.Account;
import ak.accounts.AccountManager;
import ak.customer.Customer;
import ak.customer.CustomerManager;
import ak.database.DBconnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

public class LoanRequestManagerTest {
    private LoanRequestManager loanRequestManager;
    private AccountManager accountManager;
    private CustomerManager customerManager;
    private Customer customer;
    private Account account;

    @BeforeEach
    public void setUp() throws SQLException {
        DBconnection.clearDatabase(); // Clear the database before each test
        loanRequestManager = new LoanRequestManager();
        accountManager = new AccountManager();
        customerManager = new CustomerManager();
        customer = customerManager.addCustomer("John Doe","email.com" , "1111" , "username1" , "passwordHash1"); 
        account = accountManager.createSavingsAccount(customer.getCustomerId(), "Savings", 1000.0, 2.5);
    }

    @AfterEach
    public void tearDown() {
        try {
            DBconnection.closeConnection(DBconnection.getConnection()); // Close the database connection after each test
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception for debugging purposes
            fail("Failed to close the database connection: " + e.getMessage());
        }
    }

    @Test
    public void testSubmitLoanRequest() {
        boolean result = loanRequestManager.submitLoanRequest(account.getAccountNumber(), 5000.0, "Education");
        assertTrue(result);
        List<LoanRequest> requests = loanRequestManager.getAllLoanRequests();
        assertNotNull(requests);
        assertFalse(requests.isEmpty());
    }

    @Test
    public void testGetAllLoanRequests() {
        // Assuming testSubmitLoanRequest populates the database
        List<LoanRequest> requests = loanRequestManager.getAllLoanRequests();
        assertNotNull(requests);
    }

    @Test
    public void testUpdateLoanRequestStatus() {
        LoanRequest loanRequest = loanRequestManager.submitRequest(account.getAccountNumber(), 5000.0, "Education");
        assertNotNull(loanRequest);
        boolean updateResult = loanRequestManager.updateLoanRequestStatus(loanRequest.getRequestId(), "Accepted");
        assertTrue(updateResult);
        List<LoanRequest> requests = loanRequestManager.getAllLoanRequests();
        LoanRequest request = requests.get(0);
        assertEquals("Accepted", request.getStatus());
    }
}