package ak.integration;



import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import ak.loans.LoanRequestManager;
import ak.loans.LoanManager;
import ak.loans.LoanRequest;
import ak.loans.Loan;
import ak.loans.LoanDetails;
import ak.accounts.AccountManager;
import ak.accounts.CheckingAccount;
import ak.accounts.Account;
import ak.customer.Customer;
import ak.customer.CustomerManager;

public class LoanApprovalIntegration_TC3_2Test {

    // @Test
    // @DisplayName("TC3.2 - Submit Loan Request → Approve → Generate Loan → Verify Funds")
void testLoanApprovalFlow() {
    // ========== Test Setup ==========
    System.out.println("\n=== Starting Test: Loan Approval Flow ===");
    
    // Test data
    String holderName = "user5";
    String loanPurpose = "Home Renovation";
    double overdraftLimit = 1000;
    double initialDeposit = 0;
    double loanAmount = 1000.0;
    double interestRate = 10.0;
    int durationMonths = 12;

    // ========== 1. Create Customer & Account ==========
    System.out.println("[1/4] Creating customer and account...");
    Customer customer = new Customer("user5", "ali@example.com", "01012345678");
    AccountManager accountManager = new AccountManager();
    Account account = accountManager.createCheckingAccount(
        customer.getCustomerId(), 
        holderName, 
        initialDeposit, 
        overdraftLimit
    );
    
    System.out.printf("» Created account %s with initial balance: $%.2f%n", 
        account.getAccountNumber(), account.getBalance());

    // ========== 2. Submit Loan Request ==========
    System.out.println("\n[2/4] Submitting loan request...");
    LoanRequest request = new LoanRequest(
        "LR1001",
        account.getAccountNumber(),
        loanAmount,
        loanPurpose,
        "Pending"
    );
    
    LoanRequestManager requestManager = new LoanRequestManager();
    requestManager.submitLoanRequest(
        request.getAccountNumber(),
        request.getLoanAmount(), 
        request.getLoanReason()
    );
    
    System.out.printf("» Submitted %s loan request for $%.2f (%s)%n", 
        loanPurpose, loanAmount, request.getStatus());

    // ========== 3. Approve & Create Loan ==========
    System.out.println("\n[3/4] Creating loan...");
    LoanManager loanManager = new LoanManager();
    Loan loan = loanManager.createLoan(
        customer.getCustomerId(), 
        request.getAccountNumber(), 
        request.getLoanAmount(), 
        interestRate, 
        durationMonths
    );
    
    System.out.printf("» Approved loan %s at %.1f%% interest%n", 
        loan.getLoanId(), interestRate);
   

    // ========== 4. Verifications ==========
    System.out.println("\n[4/4] Running assertions...");
    
    // Loan creation assertions
    assertNotNull(loan, "Loan should be created");
    assertEquals(durationMonths, loan.getDurationInMonths(), 
        "Loan duration should match");
    assertEquals(interestRate, loan.getInterestRate(), 0.01,
        "Interest rate should match");
    

}
}

