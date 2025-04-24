package ak;

import ak.loans.LoanRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoanRequestTest {
    private LoanRequest loanRequest;

    @BeforeEach
    public void setUp() {
        loanRequest = new LoanRequest("REQ001", "ACC123", 5000.0, "Education", "Pending");
    }

    @Test
    public void testGetRequestId() {
        assertEquals("REQ001", loanRequest.getRequestId());
    }

    @Test
    public void testGetAccountNumber() {
        assertEquals("ACC123", loanRequest.getAccountNumber());
    }

    @Test
    public void testGetLoanAmount() {
        assertEquals(5000.0, loanRequest.getLoanAmount());
    }

    @Test
    public void testGetLoanReason() {
        assertEquals("Education", loanRequest.getLoanReason());
    }

    @Test
    public void testGetStatus() {
        assertEquals("Pending", loanRequest.getStatus());
    }
}