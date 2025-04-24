package ak;

import ak.loans.LoanDetails;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoanDetailsTest {

    @Test
    public void testLoanDetailsCreation() {
        LoanDetails loanDetails = new LoanDetails(36, 5.5);
        assertEquals(36, loanDetails.getDurationInMonths());
        assertEquals(5.5, loanDetails.getInterestRate());
    }
}