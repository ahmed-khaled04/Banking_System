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


public class AccountOverdraftIntegrationTest {

    // @Test
    // @DisplayName("TC1.3 - Open Account → Withdraw → Check Balance")
    public void testOpenAccountDepositAndCheckBalance() {
        // Test data
        //customerId, holderName, initialDeposit, overdraftLimit
        String customerId = "123444";
        String accountType = "checking";
        double withdrawAmount = 200.0;
        double depositAmount = 0;
        double initialDeposit = 100;
        double overdraft_limit = 50;
        String holderName = "user3";
        String transactionType ="Withdraw";
        String toAccount = "";
        double balance = 0;



        // Step 1: create account
        AccountManager accountManager = new AccountManager();
        Account account = accountManager.createCheckingAccount(customerId, holderName, initialDeposit, overdraft_limit);
        assertNotNull(account);
        assertEquals(customerId, account.getCustomerId());

        // Step 2: Deposit
        try{
        TransactionManager transactionManager = new TransactionManager(accountManager);
        transactionManager.createTransaction(withdrawAmount,transactionType,account.getAccountNumber(),toAccount);
        }catch(Exception e){
            if(e.getMessage().contains("overdraft limit")) System.out.println("withdraw failed, overdraft limit exceeded ");
        }

        // Step 3: Verify balance hasn't changed(transaction failed)
         
        account = accountManager.getAccountByNumber(account.getAccountNumber());
        balance = account.getBalance();

        
        assertEquals(100.0, balance, 0.01, "Balance should be unchanged");
    }

    
}