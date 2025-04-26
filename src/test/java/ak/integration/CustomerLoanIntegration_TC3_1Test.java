
package ak.integration;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import ak.loans.LoanRequestManager;
import ak.loans.LoanRequest;
import ak.accounts.AccountManager;
import ak.accounts.CheckingAccount;
import ak.accounts.Account;
import ak.customer.Customer;
import ak.customer.CustomerManager;

public class CustomerLoanIntegration_TC3_1Test {

    // @Test
    // @DisplayName("TC3.1 - Create Customer → Submit Loan Request")
    void testCustomerCreatesAndSubmitsLoan() {

        String holderName = "Ali Test";
        double overdraft_limit = 1000;
        double initialDeposit = 0;



        // Create customer
        Customer customer = new Customer("Ali Test", "ali@example.com", "01012345678");
        AccountManager accountManager = new AccountManager();
        Account account = accountManager.createCheckingAccount(customer.getCustomerId(), holderName, initialDeposit, overdraft_limit);
       
        // Create loan request
        LoanRequest request = new LoanRequest(
            "LR2001",
            account.getAccountNumber(),
            10000.0,
            "Business Expansion",
            "Pending"
        );
        
        // Submit loan request

        LoanRequestManager requestManager = new LoanRequestManager();
        boolean submitted = requestManager.submitLoanRequest(request.getAccountNumber(),request.getLoanAmount(),request.getLoanReason() );
        if(submitted)System.out.println("Loan Created Successfully for Ali Test");

        // Validate
        assertTrue(submitted);
        assertEquals("LR2001", request.getRequestId());
        assertEquals(account.getAccountNumber(), request.getAccountNumber());
    }
}