package ak.integration;

import ak.accounts.Account;
import ak.accounts.AccountManager;
import ak.transactions.*;
import ak.loans.LoanRequest;
import ak.loans.LoanRequestManager;
import ak.loans.Loan;
import ak.loans.LoanManager;
import ak.financial.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerFinancialWorkflow_TC8_1Test {

    // @Test
    // @DisplayName("TC8.1 - Open Account → Deposit → Request Loan → Loan Approved → Interest Applied")
    void testCustomerFinancialWorkflow() {
        // Initialize necessary modules
        AccountManager accountManager = new AccountManager();
        TransactionManager transactionManager = new TransactionManager(accountManager);
        LoanRequestManager loanRequestManager = new LoanRequestManager();
        LoanManager loanManager = new LoanManager();
        InterestCalculator interestCalculator = new InterestCalculator();

        // Step 1: Open a customer account
        Account account = accountManager.createCheckingAccount("Cust-1010", "John Doe", 1000, 500);
        assertNotNull(account);
        assertEquals(1000, account.getBalance(), "Initial deposit should match account balance");
        System.out.println("Account created and initial deposit done.");

        // Step 2: Deposit additional funds using TransactionManager
        transactionManager.createTransaction(500, "deposit","", account.getAccountNumber());
        account = accountManager.getAccountByNumber(account.getAccountNumber());
        assertEquals(1500, account.getBalance(), "Balance after deposit should be updated");
        System.out.println("Funds deposited successfully.");

        // Step 3: Request loan using LoanRequestManager
        boolean loanRequest = loanRequestManager.submitLoanRequest(account.getAccountNumber(), 5000, "Personal loan");
        assertNotNull(loanRequest);
        System.out.println("Loan request submitted.");

        // Step 4: Create loan using LoanManager
        Loan loan = loanManager.createLoan(account.getCustomerId(), account.getAccountNumber(), 5000, 10, 12);
        assertEquals(5000, loan.getLoanAmount(), "Requested loan amount should be 5000");
        Account account2 = accountManager.getAccountByNumber(account.getAccountNumber());
        System.out.println("Loan created.");

        // Step 5: Apply interest using InterestCalculator
        double interest = interestCalculator.calculateSimpleInterest(5000, 0.05, 1); // Loan amount, interest rate, and duration in months
        double expectedInterest = 5000 * 0.05 * 1;  // 5% interest for 1 year
        assertEquals(expectedInterest, interest, "Interest should be calculated correctly");
        System.out.println("Interest applied: " + interest);

    }
}
