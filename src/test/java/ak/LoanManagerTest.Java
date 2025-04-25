package ak;

import ak.loans.Loan;
import ak.loans.LoanManager;
import ak.accounts.Account;
import ak.accounts.AccountManager;
import ak.customer.Customer;
import ak.customer.CustomerManager;
import ak.database.DBconnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoanManagerTest {
    private LoanManager loanManager;
    private CustomerManager customerManager;
    private Customer customer;
    private Account account;
    private AccountManager accountManager;

    @BeforeEach
    public void setUp() throws SQLException {
        DBconnection.clearDatabase(); // Clear the database before each test
        loanManager = new LoanManager();
        customerManager = new CustomerManager();
        accountManager = new AccountManager();
        customer = customerManager.addCustomer("John Doe", "email.com", "1111", "username1", "passwordHash1");
        account = accountManager.createSavingsAccount(customer.getCustomerId(), "Savings", 1000.0, 2.5);
    }

    @Test
    public void testCreateLoan() {
        Loan loan = loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 5000.0, 5.0, 12);
        assertNotNull(loan, "Loan should be created successfully.");
        assertEquals(customer.getCustomerId(), loan.getCustomerId());
        assertEquals(account.getAccountNumber(), loan.getAccountNumber());
        assertEquals(5000.0, loan.getLoanAmount());
        assertEquals(5.0, loan.getInterestRate());
        assertEquals(12, loan.getDurationInMonths());
    }

    @Test
    public void testGetLoanById() {
        Loan createdLoan = loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 5000.0, 5.0, 12);
        Loan retrievedLoan = loanManager.getLoanById(createdLoan.getLoanId());
        assertNotNull(retrievedLoan, "Loan should be retrieved successfully.");
        assertEquals(createdLoan.getLoanId(), retrievedLoan.getLoanId());
    }

    @Test
    public void testValidateLoanParameters() {
        assertThrows(IllegalArgumentException.class, () -> loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), -5000.0, 5.0, 12));
        assertThrows(IllegalArgumentException.class, () -> loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 5000.0, -5.0, 12));
        assertThrows(IllegalArgumentException.class, () -> loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 5000.0, 5.0, 0));
    }

    @Test
    public void testRemoveLoan() {
        Loan loan = loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 5000.0, 5.0, 12);
        boolean result = loanManager.removeLoan(loan.getLoanId());
        assertTrue(result, "Loan should be removed successfully.");
        assertNull(loanManager.getLoanById(loan.getLoanId()), "Loan should no longer exist.");
    }

    @Test
    public void testGetAllLoans() {
        loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 5000.0, 5.0, 12);
        Customer customer2 = customerManager.addCustomer("Jane Doe", "email2.com", "2222", "username2", "passwordHash2");
        Account account2 = accountManager.createSavingsAccount(customer2.getCustomerId(), "Savings", 2000.0, 3.5);
        loanManager.createLoan(customer2.getCustomerId(), account2.getAccountNumber(), 10000.0, 6.0, 24);

        List<Loan> loans = loanManager.getAllLoans();
        assertNotNull(loans, "Loans list should not be null.");
        assertEquals(2, loans.size(), "There should be 2 loans in the database.");
    }

    @Test
    public void testCalculateCustomerMonthlyPayment() {
        Account account2 = accountManager.createSavingsAccount(customer.getCustomerId(), "Savings", 2000.0, 3.5);
        loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 5000.0, 5.0, 12);
        loanManager.createLoan(customer.getCustomerId(), account2.getAccountNumber(), 10000.0, 6.0, 24);

        double monthlyPayment = loanManager.calculateCustomerMonthlyPayment(customer.getCustomerId());
        assertTrue(monthlyPayment > 0, "Monthly payment should be greater than 0.");
    }

    @Test
    public void testCalculateCustomerTotalRemaining() {
        Account account2 = accountManager.createSavingsAccount(customer.getCustomerId(), "Savings", 2000.0, 3.5);
        loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 5000.0, 5.0, 12);
        loanManager.createLoan(customer.getCustomerId(), account2.getAccountNumber(), 10000.0, 6.0, 24);

        double totalRemaining = loanManager.calculateCustomerTotalRemaining(customer.getCustomerId());
        assertTrue(totalRemaining > 0, "Total remaining repayment should be greater than 0.");
    }
}