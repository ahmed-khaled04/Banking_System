package ak.integration;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import ak.accounts.AccountManager;
import ak.accounts.CheckingAccount;
import ak.accounts.Account;
import ak.transactions.Transaction;
import ak.transactions.TransactionManager;
import ak.database.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public class AccountDepositIntegrationTest { 

    // @Test
    // @DisplayName("TC1.1 - Open Account → Deposit → Check Balance")
    public void testOpenAccountDepositAndCheckBalance() {
        // Test data
        //customerId, holderName, initialDeposit, overdraftLimit
        String customerId = "12345";
        String accountType = "checking";
        double depositAmount = 100.0;
        double initialDeposit = 0;
        double overdraft_limit = 1000;
        String holderName = "user1";
        String transactionType ="Deposit";
        String fromAccount = "";
        double balance = 0;



        // Step 1: create account
        AccountManager accountManager = new AccountManager();
        Account account = accountManager.createCheckingAccount(customerId, holderName, initialDeposit, overdraft_limit);
        assertNotNull(account);
        assertEquals(customerId, account.getCustomerId());

        // Step 2: Deposit
        TransactionManager transactionManager = new TransactionManager(accountManager);
        transactionManager.createTransaction(depositAmount,transactionType,fromAccount,account.getAccountNumber());
        

        // Step 3: Verify balance(After the deposit, refresh the account object)
         
        account = accountManager.getAccountByNumber(account.getAccountNumber());
        balance = account.getBalance();
        assertEquals(100.0, balance, 0.01, "Balance should reflect deposited amount");
    }

    
}