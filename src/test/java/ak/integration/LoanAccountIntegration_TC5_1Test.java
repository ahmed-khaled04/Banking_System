package ak.integration;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import ak.loans.*;
import ak.accounts.*;
import ak.database.*;

public class LoanAccountIntegration_TC5_1Test {

    // @Test
    // @DisplayName("TC5.1 - Approved Loan → Amount Deposited to Account")
    void testApprovedLoanDepositToAccount() {
        AccountManager accountManager = new AccountManager();
        LoanManager loanManager = new LoanManager();
        Account account = accountManager.createCheckingAccount("Cust-1103", "user6", 0, 0);

        // Create and approve a loan
        double loanAmount = 5000.0;
        double intrestRate = 10;
        int durationInMonths = 12;

        loanManager.createLoan(account.getCustomerId(),account.getAccountNumber(),loanAmount, intrestRate ,durationInMonths); // Assume this updates loan status to approved
        

        // Verify updated account balance
        account = accountManager.getAccountByNumber(account.getAccountNumber());
        double balance = account.getBalance();
        assertEquals(5000.0, balance, 0.01);
        System.out.println("Balance after loan deposit: $" + balance);
    }
}
