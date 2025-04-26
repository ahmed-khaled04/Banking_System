package ak.integration;

import ak.accounts.Account;
import ak.accounts.AccountManager;
import ak.transactions.TransactionManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SystemUtility_TC6_1Test {

    // @Test
    // @DisplayName("TC6.1 - Invalid Transaction → Exception Raised and Logged")
    void testInvalidTransactionHandling() {
        AccountManager accountManager = new AccountManager();
        Account account = accountManager.createCheckingAccount("Cust-1103", "user7", 0, 0);
        TransactionManager tm = new TransactionManager(accountManager);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tm.createTransaction(-500, "withdraw", account.getAccountNumber(), null); // Invalid negative amount
        });

        String expectedMessage = "Transaction amount must be positive";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println("Invalid transaction was caught and exception was logged.");
    }
}
