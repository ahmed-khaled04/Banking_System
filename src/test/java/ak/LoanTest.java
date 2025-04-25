package ak;

import ak.customer.Customer;
import ak.loans.Loan;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoanTest {

    @Test
    public void testValidLoanCreation() {
        Customer customer = new Customer("123", "Nour", "nour@example.com", "1234567890");
        Loan loan = new Loan("LOAN123", customer.getCustomerId(), "ACC123", 10000.0, 0.05, 12);

        assertEquals(customer.getCustomerId(), loan.getCustomerId());
        assertEquals(10000.0, loan.getLoanAmount(), 0.01);
        assertEquals(0.05, loan.getInterestRate(), 0.01);
        assertEquals(12, loan.getDurationInMonths());
    }

    @Test
    public void testLoanWithNegativeAmountThrowsException() {
        Customer customer = new Customer("123", "Nour", "nour@example.com", "1234567890");

        assertThrows(IllegalArgumentException.class, () -> {
            new Loan("LOAN123", customer.getCustomerId(), "ACC123", -5000.0, 0.05, 12);
        });
    }

    @Test
    public void testLoanWithNullCustomerThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Loan("LOAN123", null, "ACC123", 5000.0, 0.05, 12);
        });
    }

}