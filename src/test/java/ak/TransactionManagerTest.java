package ak;

import ak.accounts.Account;
import ak.accounts.AccountManager;
import ak.accounts.SavingsAccount;
import ak.customer.Customer;
import ak.customer.CustomerManager;
import ak.database.DBconnection;
import ak.transactions.Transaction;
import ak.transactions.TransactionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TransactionManagerTest {
    private TransactionManager transactionManager;
    private AccountManager accountManager;
    private Customer customer;
    private Account source;
    private Account destination;

    @BeforeEach
    public void setUp() {
        DBconnection.clearDatabase(); // Clear the database before each test
        accountManager = new AccountManager();
        transactionManager = new TransactionManager(accountManager);

        customer = new CustomerManager().addCustomer("John Doe", "john.doe@example.com", "1234567890", "john123", "password123");
        source = accountManager.createSavingsAccount(customer.getCustomerId(), "John Doe", 1000.0, 2.5);
        destination = accountManager.createSavingsAccount(customer.getCustomerId(), "Jane Doe", 500.0, 2.5);
    }

    @Test
    public void testValidTransactionCreation() {
        Transaction transaction = transactionManager.createTransaction(200.0, "transfer", source.getAccountNumber(), destination.getAccountNumber());

        assertNotNull(transaction);
        assertEquals(200.0, transaction.getAmount());
        assertEquals("transfer", transaction.getType());
        assertEquals(source.getAccountNumber(), transaction.getFromAccount());
        assertEquals(destination.getAccountNumber(), transaction.getToAccount());

        List<Transaction> history = transactionManager.getTransactionsByAccount(source.getAccountNumber());
        assertEquals(1, history.size());
        //assertEquals(transaction, history.get(0));
    }

    @Test
    public void testTransferWithInsufficientFunds() {
        assertThrows(RuntimeException.class, () -> {
            transactionManager.createTransaction(1500.0, "transfer", source.getAccountNumber(), destination.getAccountNumber());
        });
    }

    @Test
    public void testTransferNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            transactionManager.createTransaction(-100.0, "transfer", source.getAccountNumber(), destination.getAccountNumber());
        });
    }

    @Test
    public void testTransactionHistoryMultiple() {
        transactionManager.createTransaction(100.0, "transfer", source.getAccountNumber(), destination.getAccountNumber());
        transactionManager.createTransaction(50.0, "transfer", source.getAccountNumber(), destination.getAccountNumber());

        List<Transaction> history = transactionManager.getTransactionsByAccount(source.getAccountNumber());
        assertEquals(2, history.size());
    }

    @Test
    public void testHistoryReturnsEmptyListForAccountWithNoTransactions() {
        Account newAccount = new SavingsAccount(customer.getCustomerId(), "New Account", 300.0, 2.5 , true);
        accountManager.createAccount(newAccount);
        List<Transaction> history = transactionManager.getTransactionsByAccount(newAccount.getAccountNumber());
        assertTrue(history.isEmpty());
    }

    @Test
    public void testNullSourceAccount() {
        assertThrows(IllegalArgumentException.class, () -> {
            transactionManager.createTransaction(100.0, "transfer", null, destination.getAccountNumber());
        });
    }

    @Test
    public void testNullDestinationAccount() {
        assertThrows(IllegalArgumentException.class, () -> {
            transactionManager.createTransaction(100.0, "transfer", source.getAccountNumber(), null);
        });
    }

    @Test
    public void testTransferZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            transactionManager.createTransaction(0.0, "transfer", source.getAccountNumber(), destination.getAccountNumber());
        });
    }
}