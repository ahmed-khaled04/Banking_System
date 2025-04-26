package ak;

import ak.accounts.Account;
import ak.accounts.AccountManager;
import ak.customer.Customer;
import ak.customer.CustomerManager;
import ak.database.DBconnection;
import ak.loans.Loan;
import ak.loans.LoanDetails;
import ak.loans.LoanManager;
import ak.loans.LoanRequest;
import ak.loans.LoanRequestManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

public class LoanWhiteBoxTest {

    private LoanManager loanManager;
    private LoanRequestManager loanRequestManager;
    private CustomerManager customerManager;
    private AccountManager accountManager;
    private Account account;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        DBconnection.clearDatabase();


        loanManager = new LoanManager();
        loanRequestManager = new LoanRequestManager();
        customerManager = new CustomerManager();
        accountManager = new AccountManager();
        customer = customerManager.addCustomer("Jane", "email@email.com", "1000", "Hamada", "1234");
        account = accountManager.createSavingsAccount(customer.getCustomerId(), "Hamada", 1000.0, 2.5);
    }

    // LoanManager Tests
    @Test
    public void testCreateLoan() {
        Loan loan = loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 10000, 5.5, 60);
        assertNotNull(loan, "Loan should be created.");
        assertEquals(customer.getCustomerId(), loan.getCustomerId());
        assertEquals(account.getAccountNumber(), loan.getAccountNumber());
        assertEquals(10000, loan.getLoanAmount(), 0.01);
        assertEquals(5.5, loan.getInterestRate(), 0.01);
        assertEquals(60, loan.getDurationInMonths());
    }

    @Test
    public void testCreateLoanWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> loanManager.createLoan("C123", "ACC123", -10000, 5.5, 60));

        assertThrows(IllegalArgumentException.class, () -> loanManager.createLoan("C123", "ACC123", 10000, -5.5, 60));

        assertThrows(IllegalArgumentException.class, () -> loanManager.createLoan("C123", "ACC123", 10000, 5.5, -60));
    }

    @Test
    public void testGetLoanById() {
        Loan loan = loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 10000, 5.5, 60);
        Loan retrievedLoan = loanManager.getLoanById(loan.getLoanId());
        assertNotNull(retrievedLoan, "Loan should be retrieved.");
        assertEquals(loan.getLoanId(), retrievedLoan.getLoanId());
    }

    @Test
    public void testGetLoansByCustomer() {
        loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 10000, 5.5, 60);
        List<Loan> loans = loanManager.getLoansByCustomer(customer.getCustomerId());
        assertNotNull(loans, "Loans list should not be null.");
        assertFalse(loans.isEmpty(), "Loans list should not be empty.");
    }

    @Test
    public void testGetAllLoans() {
        loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 10000, 5.5, 60);
        List<Loan> loans = loanManager.getAllLoans();
        assertNotNull(loans, "Loans list should not be null.");
        assertFalse(loans.isEmpty(), "Loans list should not be empty.");
    }

    @Test
    public void testRemoveLoan() {
        Loan loan = loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 10000, 5.5, 60);
        assertTrue(loanManager.removeLoan(loan.getLoanId()), "Loan should be removed.");
    }

    // LoanRequestManager Tests
    @Test
    public void testSubmitLoanRequest() {
        assertTrue(loanRequestManager.submitLoanRequest("ACC123", 10000, "Home Improvement"));
    }

    @Test
    public void testGetAllLoanRequests() {
        loanRequestManager.submitLoanRequest("ACC123", 10000, "Home Improvement");
        List<LoanRequest> requests = loanRequestManager.getAllLoanRequests();
        assertNotNull(requests, "Loan requests list should not be null.");
        assertFalse(requests.isEmpty(), "Loan requests list should not be empty.");
    }

    @Test
    public void testUpdateLoanRequestStatus() {
        loanRequestManager.submitLoanRequest("ACC123", 10000, "Home Improvement");
        List<LoanRequest> requests = loanRequestManager.getAllLoanRequests();
        LoanRequest request = requests.get(0);
        assertTrue(loanRequestManager.updateLoanRequestStatus(request.getRequestId(), "Approved"));
    }

    // Loan Tests
    @Test
    public void testLoanCreation() {
        Loan loan = new Loan("C123", "ACC123", 10000, 5.5, 60);
        assertNotNull(loan, "Loan should be created.");
        assertEquals("C123", loan.getCustomerId());
        assertEquals("ACC123", loan.getAccountNumber());
        assertEquals(10000, loan.getLoanAmount(), 0.01);
        assertEquals(5.5, loan.getInterestRate(), 0.01);
        assertEquals(60, loan.getDurationInMonths());
    }

    @Test
    public void testLoanCreationWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> new Loan("C123", "ACC123", -10000, 5.5, 60));

        assertThrows(IllegalArgumentException.class, () -> new Loan("C123", "ACC123", 10000, -5.5, 60));

        assertThrows(IllegalArgumentException.class, () -> new Loan("C123", "ACC123", 10000, 5.5, -60));
    }

    @Test
    public void testLoanMonthlyPaymentCalculation() {
        Loan loan = new Loan("C123", "ACC123", 10000, 5.5, 60);
        double monthlyPayment = loan.calculateMonthlyPayment();
        assertTrue(monthlyPayment > 0, "Monthly payment should be positive.");
    }

    @Test
    public void testLoanTotalRepaymentCalculation() {
        Loan loan = new Loan("C123", "ACC123", 10000, 5.5, 60);
        double totalRepayment = loan.calculateTotalRepayment();
        assertTrue(totalRepayment > 0, "Total repayment should be positive.");
    }

    @Test
    public void testLoanPrintDetails() {
        Loan loan = new Loan("C123", "ACC123", 10000, 5.5, 60);
        loan.printLoanDetails();
        // This test is more about ensuring the method runs without errors.
    }

    // LoanRequest Tests
    @Test
    public void testLoanRequestCreation() {
        LoanRequest request = new LoanRequest("R123", "ACC123", 10000, "Home Improvement", "Pending");
        assertNotNull(request, "Loan request should be created.");
        assertEquals("R123", request.getRequestId());
        assertEquals("ACC123", request.getAccountNumber());
        assertEquals(10000, request.getLoanAmount(), 0.01);
        assertEquals("Home Improvement", request.getLoanReason());
        assertEquals("Pending", request.getStatus());
    }

    // LoanDetails Tests
    @Test
    public void testLoanDetailsCreation() {
        LoanDetails details = new LoanDetails(60, 5.5);
        assertNotNull(details, "Loan details should be created.");
        assertEquals(60, details.getDurationInMonths());
        assertEquals(5.5, details.getInterestRate(), 0.01);
    }

    // Test LoanManager with edge cases
    @Test
    public void testCreateLoanWithEdgeCases() {
        // Test with very high interest rate
        assertThrows(RuntimeException.class, () -> loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 10000, 1000.0, 60));
        assertThrows(RuntimeException.class, () -> loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 10000, 1, 1200));

        
    }

    // Test LoanRequestManager with edge cases
    @Test
    public void testSubmitLoanRequestWithEdgeCases() {
        // Test with very high loan amount
        assertTrue(loanRequestManager.submitLoanRequest("ACC123", 10000000, "Business Expansion"));

        // Test with very long loan reason
        String longReason = "A very long reason that exceeds the typical length limits for a loan reason.";
        assertTrue(loanRequestManager.submitLoanRequest("ACC123", 10000, longReason));
    }

    // Test Loan financial calculations with edge cases
    @Test
    public void testLoanFinancialCalculationsWithEdgeCases() {
        Loan loan = new Loan("C123", "ACC123", 10000, 99.9, 60);
        double monthlyPayment = loan.calculateMonthlyPayment();
        assertTrue(monthlyPayment > 0, "Monthly payment should be positive.");

        double totalRepayment = loan.calculateTotalRepayment();
        assertTrue(totalRepayment > 0, "Total repayment should be positive.");
    }

    @Test
    public void testCreateLoanWithInvalidData(){
        assertThrows(IllegalArgumentException.class, () -> new Loan(null, customer.getCustomerId(), account.getAccountNumber(), 100, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new Loan("Loan1234", customer.getCustomerId(), null, 100, 1, 1));
    }

    @Test
    public void testPrintCustomerLoans(){

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        
        loanManager.printCustomerLoans(customer.getCustomerId());
        String output = outputStream.toString();
        assertTrue(output.contains("No loans found for customer:"), "Output should contain the header.");
        
        outputStream.reset();
        loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 10000, 5.5, 60);
        loanManager.printCustomerLoans(customer.getCustomerId());
        output = outputStream.toString();
        assertTrue(output.contains("Loans for customer"), "Output should contain the header.");


        System.setOut(originalOut);
   }

   @Test
   public void testCloseDB(){
        loanManager.close();
        assertThrows(RuntimeException.class, () -> loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 10000, 5.5, 60));
    }
    
    @Test
    public void testMethodsWithDBClosed(){
        loanManager.close();
        assertThrows(RuntimeException.class, () -> loanManager.createLoan(customer.getCustomerId(), account.getAccountNumber(), 10000, 5.5, 60));
        assertThrows(RuntimeException.class, () -> loanManager.getLoanById("loanId"));
        assertThrows(RuntimeException.class, () -> loanManager.getLoansByCustomer("null"));
        assertThrows(RuntimeException.class, () -> loanManager.getAllLoans());
        assertThrows(RuntimeException.class, () -> loanManager.removeLoan("1234"));
   }
}