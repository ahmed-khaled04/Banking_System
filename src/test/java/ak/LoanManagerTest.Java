import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoanManagerTest {

    //LOAN APPROVAL
    @Test
    public void testLoanApprovalWithHighCreditScore() {
        Customer customer = new Customer("123", "Nour");
        Loan loan = new Loan(customer, 5000.0, 0.06, 12);
        LoanProcessor processor = new LoanProcessor();

        processor.processLoanApplication(loan, 750, 50000); // high credit score + income

        assertTrue(loan.isApproved());
    }

    @Test
    public void testLoanDenialWithLowCreditScore() {
        Customer customer = new Customer("124", "Ahmed");
        Loan loan = new Loan(customer, 8000.0, 0.09, 18);
        LoanProcessor processor = new LoanProcessor();

        processor.processLoanApplication(loan, 580, 40000); // low credit score

        assertFalse(loan.isApproved());
    }

    @Test
    public void testLoanDenialWithLowIncome() {
        Customer customer = new Customer("125", "Sarah");
        Loan loan = new Loan(customer, 10000.0, 0.1, 24);
        LoanProcessor processor = new LoanProcessor();

        processor.processLoanApplication(loan, 720, 8000); // good credit score, low income

        assertFalse(loan.isApproved());
    }

    @Test
    public void testLoanApprovalExactlyOnThreshold() {
        Customer customer = new Customer("126", "Omar");
        Loan loan = new Loan(customer, 4000.0, 0.07, 12);
        LoanProcessor processor = new LoanProcessor();

        processor.processLoanApplication(loan, 700, 20000); // exact threshold

        assertTrue(loan.isApproved());
    }

    //EDGE CASES AND INVALID INPUTS
    @Test
    public void testLoanWithNullCustomer() {
        assertThrows(NullPointerException.class, () -> {
            Loan loan = new Loan(null, 5000.0, 0.05, 12);
        }); 
    }

    @Test
    public void testLoanWithNegativeAmount() {
        Customer customer = new Customer("127", "Layla");

        assertThrows(IllegalArgumentException.class, () -> {
            Loan loan = new Loan(customer, -1000.0, 0.05, 12);
        });
    }

    @Test 
    public void testLoanWithZeroRepaymentTerm() {
        Customer customer = new Customer("128", "Ali");
        assertThrows(IllegalArgumentException.class, () -> {
            Loan loan = new Loan(customer, 2000.0, 0.05, 0); // zero term
        });
    }

    @Test
    public void testLoanWithNegativeInterestRate() {
        Customer customer = new Customer("129", "Fatima");

        assertThrows(IllegalArgumentException.class, () -> {
            Loan loan = new Loan(customer, 3000.0, -0.05, 12); // negative interest rate
        });
    }
}
