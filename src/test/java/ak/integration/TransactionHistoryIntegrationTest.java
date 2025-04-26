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


public class TransactionHistoryIntegrationTest {

    // @Test
    // @DisplayName("TC1.4 - Open Account → Deposit → Withdraw → Transfer → Fretch history → Check Balance")
    public void testOpenAccountDepositAndCheckBalance() {
        // Test data
        //customerId, holderName, initialDeposit, overdraftLimit
        String customerId = "1456";
        String accountType = "checking";
        double withdrawAmount = 30;
        double depositAmount = 50;
        double transferAmount = 20;
        double initialDeposit = 100;
        double overdraft_limit = 50;
        String holderName = "user4";
        String toAccount = "";
        double balance_1 = 0;
        double balance_2 = 0;



        // Step 1: create account
        AccountManager accountManager = new AccountManager();
        Account account_1 = accountManager.createCheckingAccount(customerId, holderName, initialDeposit, overdraft_limit);
        Account account_2 = accountManager.createCheckingAccount(customerId, "user5", initialDeposit, overdraft_limit);
        assertNotNull(account_1);
        assertNotNull(account_2);
        assertEquals(customerId, account_1.getCustomerId());
        assertEquals(customerId, account_2.getCustomerId());

        // Step 2: Deposit
        TransactionManager transactionManager = new TransactionManager(accountManager);
        transactionManager.createTransaction(depositAmount,"Deposit","",account_1.getAccountNumber());
        transactionManager.createTransaction(withdrawAmount,"Withdraw",account_1.getAccountNumber(),toAccount);
        transactionManager.createTransaction(transferAmount,"Transfer",account_1.getAccountNumber(),account_2.getAccountNumber());
        


        List<Transaction> transactions = transactionManager.getTransactionsByAccount(account_1.getAccountNumber());

        

        // Step 3: Verify balance && print out transactions for account_1
         
        account_1 = accountManager.getAccountByNumber(account_1.getAccountNumber());
        balance_1 = account_1.getBalance();
        assertEquals(100.0, balance_1, 0.01, "Balance should reflect deposited amount");
        account_2 = accountManager.getAccountByNumber(account_2.getAccountNumber());
        balance_2 = account_2.getBalance();
        assertEquals(120.0, balance_2, 0.01, "Balance should reflect deposited amount");

        System.out.println(transactions);
        
    }

    
}
