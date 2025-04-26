package ak.integration;

import ak.loans.Loan;
import ak.loans.LoanManager;
import ak.financial.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SystemUtility_TC6_3Test {

    // @Test
    // @DisplayName("TC6.3 - Interest Calculation on Invalid Data")
    void testInterestCalculationWithInvalidData() {
        // Create a Loan object with invalid data
        LoanManager loanmanager = new LoanManager();
        
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Loan invalidLoan = loanmanager.createLoan("L9999", "A9999", 1000, 10, 12);
            InterestCalculator.calculateSimpleInterest(10,10,-10);
        });

        String expectedMessage = "cannot be negative";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        System.out.println("Interest calculation failed on invalid loan data.");
    }
}
