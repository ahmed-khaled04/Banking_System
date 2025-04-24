package ak;

import ak.loans.LoanRequestManager;
import ak.loans.LoanRequest;
import ak.database.DBconnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

public class LoanRequestManagerTest {
    private LoanRequestManager loanRequestManager;

    @BeforeEach
    public void setUp() throws SQLException {
        DBconnection.clearDatabase(); // Clear the database before each test
        loanRequestManager = new LoanRequestManager();
    }

    @AfterEach
    public void tearDown() {
        DBconnection.closeConnection(); // Close the database connection after each test
    }

    @Test
    public void testSubmitLoanRequest() {
        boolean result = loanRequestManager.submitLoanRequest("ACC123", 5000.0, "Education");
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
        boolean result = loanRequestManager.submitLoanRequest("ACC123", 5000.0, "Education");
        assertTrue(result);
        boolean updateResult = loanRequestManager.updateLoanRequestStatus("1", "Approved");
        assertTrue(updateResult);
        List<LoanRequest> requests = loanRequestManager.getAllLoanRequests();
        LoanRequest request = requests.get(0);
        assertEquals("Approved", request.getStatus());
    }
}