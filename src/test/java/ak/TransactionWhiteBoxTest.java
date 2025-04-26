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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

public class TransactionWhiteBoxTest {

    private TransactionManager transactionManager;
    private AccountManager accountManager; 
    private CustomerManager customerManager;

    @BeforeEach
    public void setUp() {

        DBconnection.clearDatabase();

        accountManager = new AccountManager();
        transactionManager = new TransactionManager(accountManager);
        customerManager = new CustomerManager();
    }


    // Test: generateTransactionId format
    @Test
    public void testGenerateTransactionId() {
        Transaction transaction = new Transaction(200.0, "Transfer", "ACC123", "ACC456", null);
        assertTrue(transaction.getTransactionId().startsWith("TXN-"));
        assertEquals(12, transaction.getTransactionId().length());
    }

    // Invalid transaction types
    @Test
    public void testInvalidTransactionType() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(200.0, "", "ACC123", "ACC456", null));

        assertThrows(IllegalArgumentException.class, () -> new Transaction(200.0, null, "ACC123", "ACC456", null));

        assertThrows(IllegalArgumentException.class,
                () -> new Transaction(200.0, "InvalidType", "ACC123", "ACC456", null));
    }

    // Invalid amounts (zero or negative)
    @Test
    public void testInvalidAmount() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(0.0, "Transfer", "ACC123", "ACC456", null));

        assertThrows(IllegalArgumentException.class,
                () -> new Transaction(-100.0, "Transfer", "ACC123", "ACC456", null));
    }

    // Null source/destination accounts
    @Test
    public void testNullAccounts() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(200.0, "Transfer", null, null, null));

        assertThrows(IllegalArgumentException.class, () -> new Transaction(200.0, "Transfer", null, "ACC456", null));

        assertThrows(IllegalArgumentException.class, () -> new Transaction(200.0, "Transfer", "ACC123", null, null));
    }

    // Valid transaction field assignment
    @Test
    public void testValidTransactionFields() {
        Transaction t = new Transaction(250.0, "Deposit", "ACC123", "ACC456", null);

        assertEquals(250.0, t.getAmount());
        assertEquals("Deposit", t.getType());
        assertEquals("ACC123", t.getFromAccount());
        assertEquals("ACC456", t.getToAccount());
        assertNull(t.getTimestamp());
    }

    // Timestamp format validation
    @Test
    public void testTimestampFormat() {
        Transaction t = new Transaction(100.0, "Transfer", "ACC001", "ACC002", null);
        String timestamp = t.generateTimestamp();

        assertTrue(timestamp.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    // Self-transfer scenario
    @Test
    public void testSelfTransferAllowed() {
        Transaction t = new Transaction(100.0, "Transfer", "ACC123", "ACC123", null);
        assertEquals("ACC123", t.getFromAccount());
        assertEquals("ACC123", t.getToAccount());
    }

    // Unsupported characters in transaction type
    @Test
    public void testUnsupportedCharactersInTransactionType() {
        assertThrows(IllegalArgumentException.class,
                () -> new Transaction(100.0, "@Withdraw!", "ACC001", "ACC002", null));
    }

    // Basic uniqueness check for transaction ID
    @Test
    public void testTransactionIdUniqueness() {
        Transaction t1 = new Transaction(50.0, "Deposit", "ACC001", "ACC002", null);
        Transaction t2 = new Transaction(75.0, "Deposit", "ACC003", "ACC004", null);

        assertNotEquals(t1.getTransactionId(), t2.getTransactionId());
    }

    // Test printTransactionDetails method
    @Test
    public void testPrintTransactionDetails() {
        Transaction t = new Transaction(250.0, "Deposit", "ACC123", "ACC456", null);
        t.printTransactionDetails();
        // This test is more about ensuring the method runs without errors.
    }

    // Test generateTimestamp method
    @Test
    public void testGenerateTimestamp() {
        Transaction t = new Transaction(100.0, "Transfer", "ACC001", "ACC002", null);
        String timestamp = t.generateTimestamp();
        assertNotNull(timestamp);
        assertTrue(timestamp.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    // Test getDate method
    @Test
    public void testGetDate() {
        Transaction t = new Transaction(100.0, "Transfer", "ACC001", "ACC002", "2023-04-24 12:00:00");
        assertEquals("2023-04-24 12:00:00", t.getDate());
    }

    // Test getTimestamp method
    @Test
    public void testGetTimestamp() {
        Transaction t = new Transaction(100.0, "Transfer", "ACC001", "ACC002", "2023-04-24 12:00:00");
        assertEquals("2023-04-24 12:00:00", t.getTimestamp());
    }

    // Test getTransactionId method
    @Test
    public void testGetTransactionId() {
        Transaction t = new Transaction(100.0, "Transfer", "ACC001", "ACC002", null);
        assertNotNull(t.getTransactionId());
        assertTrue(t.getTransactionId().startsWith("TXN-"));
    }

    // Test getAmount method
    @Test
    public void testGetAmount() {
        Transaction t = new Transaction(100.0, "Transfer", "ACC001", "ACC002", null);
        assertEquals(100.0, t.getAmount(), 0.01);
    }

    // Test getType method
    @Test
    public void testGetType() {
        Transaction t = new Transaction(100.0, "Transfer", "ACC001", "ACC002", null);
        assertEquals("Transfer", t.getType());
    }

    // Test getFromAccount method
    @Test
    public void testGetFromAccount() {
        Transaction t = new Transaction(100.0, "Transfer", "ACC001", "ACC002", null);
        assertEquals("ACC001", t.getFromAccount());
    }

    // Test getToAccount method
    @Test
    public void testGetToAccount() {
        Transaction t = new Transaction(100.0, "Transfer", "ACC001", "ACC002", null);
        assertEquals("ACC002", t.getToAccount());
    }

    @Test
    public void testEmptyTransactionIdGeneration() {
        Transaction transaction = new Transaction(200.0, "Transfer", "ACC123", "ACC456", null);
        assertNotNull(transaction.getTransactionId());
        assertFalse(transaction.getTransactionId().isEmpty());
    }

    @Test
    public void testTransactionWithCustomTimestamp() {
        String customTimestamp = "2023-01-01 12:00:00";
        Transaction t = new Transaction(100.0, "Transfer", "ACC001", "ACC002", customTimestamp);
        assertEquals(customTimestamp, t.getTimestamp());
    }

    @Test
    public void testTransactionWithEmptyTimestamp() {
        Transaction t = new Transaction(100.0, "Transfer", "ACC001", "ACC002" , null);
        assertNull(t.getTimestamp());
    }

    @Test
    public void testTransactionWithNullTimestamp() {
        Transaction t = new Transaction(100.0, "Transfer", "ACC001", "ACC002", null);
        assertNull(t.getTimestamp());
    }

    @Test
    public void testTransactionTypeCaseSensitivity() {
        Transaction t = new Transaction(100.0, "tRaNsFeR", "ACC001", "ACC002", null);
        assertEquals("tRaNsFeR", t.getType());
    }

    @Test
    public void testSmallAmountTransaction() {
        Transaction t = new Transaction(Double.MIN_VALUE, "Deposit", "ACC001", "ACC002", null);
        assertEquals(Double.MIN_VALUE, t.getAmount());
    }

    @Test
    public void testTransactionWithMaxLengthAccountNumbers() {
        String longAccountNumber = "ACC12345678901234567890"; // 22 characters
        Transaction t = new Transaction(100.0, "Transfer", longAccountNumber, longAccountNumber, null);
        assertEquals(longAccountNumber, t.getFromAccount());
        assertEquals(longAccountNumber, t.getToAccount());
    }

    @Test
    public void testTransactionWithSpecialCharactersInAccountNumbers() {
        String specialAccount = "ACC-123_456@789";
        Transaction t = new Transaction(100.0, "Transfer", specialAccount, specialAccount, null);
        assertEquals(specialAccount, t.getFromAccount());
        assertEquals(specialAccount, t.getToAccount());
    }

    @Test
    public void testTransactionWithDifferentPrecisionAmounts() {
        Transaction t1 = new Transaction(100.12345, "Deposit", "ACC001", "ACC002", null);
        Transaction t2 = new Transaction(100.12346, "Deposit", "ACC001", "ACC002", null);
        
        assertEquals(100.12345, t1.getAmount(), 0.00001);
        assertEquals(100.12346, t2.getAmount(), 0.00001);
        assertEquals(t1.getAmount(), t2.getAmount(), 0.001);
    }

    @Test
    public void testTransactionWithVeryLongType() {
        String longType = "VERY_LONG_TRANSACTION_TYPE_NAME_THAT_EXCEEDS_NORMAL_LENGTH";
        assertThrows(IllegalArgumentException.class, () -> new Transaction(100.0, longType, "ACC001", "ACC002", null));
    }

    @Test
    public void testTransactionEquality() {
        String fixedTimestamp = "2023-01-01 12:00:00";
        Transaction t1 = new Transaction(100.0, "Transfer", "ACC001", "ACC002", fixedTimestamp);
        Transaction t2 = new Transaction(100.0, "Transfer", "ACC001", "ACC002", fixedTimestamp);
        
        // While these have identical fields, they should not be considered equal due to different IDs
        assertNotEquals(t1, t2);
        assertNotEquals(t1.hashCode(), t2.hashCode());
    }
    

    @Test
    public void testGetTransactionById(){

        Customer customer = customerManager.addCustomer("Ahmed", "Email@email.com", "212125", "Ahmed", "1234");
        Account account1 = accountManager.createSavingsAccount(customer.getCustomerId(), "ahmed", 100, 0.5);

        Transaction transaction = transactionManager.createTransaction(100, "Deposit", account1.getAccountNumber(), null);



        assertEquals(transaction.getAmount(), transactionManager.getTransactionById(transaction.getTransactionId()).getAmount());
        assertEquals(transaction.getClass(), transactionManager.getTransactionById(transaction.getTransactionId()).getClass());
        assertEquals(transaction.getFromAccount(), transactionManager.getTransactionById(transaction.getTransactionId()).getFromAccount());
        assertEquals(transaction.getToAccount(), transactionManager.getTransactionById(transaction.getTransactionId()).getToAccount());
    }

    @Test
    public void testClose(){
        transactionManager.close();
        assertThrows(RuntimeException.class, () -> transactionManager.createTransaction(100, "Deposit", "ACC123", "ACC456"));
    }

    @Test
    public void testGetCreatedAt(){
        Customer customer = customerManager.addCustomer("Ahmed", "Email@email.com", "212125", "Ahmed", "1234");
        Account account1 = accountManager.createSavingsAccount(customer.getCustomerId(), "ahmed", 100, 0.5);

        Transaction transaction = transactionManager.createTransaction(100, "Deposit", account1.getAccountNumber(), null);
        // Get the current time
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedTime = currentTime.format(formatter);

        String createdAt = transactionManager.getCreatedAt(transaction.getTransactionId());

        assertTrue(createdAt.startsWith(formattedTime.substring(0, 10))); 
    }

    @Test
    public void testCreateTransactionWithInvalidData(){
        Customer customer = customerManager.addCustomer("Ahmed", "Email@email.com", "212125", "Ahmed", "1234");
        Account account1 = accountManager.createSavingsAccount(customer.getCustomerId(), "ahmed", 100, 0.5);

        assertThrows(IllegalArgumentException.class, () -> transactionManager.createTransaction(100, "", account1.getAccountNumber(), null));
        assertThrows(IllegalArgumentException.class, () -> transactionManager.createTransaction(100, null, account1.getAccountNumber(), null));
        assertThrows(IllegalArgumentException.class, () -> transactionManager.createTransaction(100, "deposit", null, null));
    }

    @Test
    public void testCreateTransactionManagerwithInvalidData(){
        assertThrows(IllegalArgumentException.class, () -> new TransactionManager(null));

    }
    

    @Test
    public void testGetTransactionByIdwithDBClosed(){
        transactionManager.close();
        assertThrows(RuntimeException.class, () -> transactionManager.getTransactionById("TXN-12345678"));
    }
    @Test
    public void testGetCreatedAtwithDBClosed(){
        transactionManager.close();
        assertThrows(RuntimeException.class, () -> transactionManager.getCreatedAt("TXN-12345678"));
    }
    @Test
    public void testGetTransactionByAccountwithDBClosed(){
        transactionManager.close();
        assertThrows(RuntimeException.class, () -> transactionManager.getTransactionsByAccount("ACC-123456"));
    }
    @Test
    public void testCloseDBwithDBClosed() {
        // Close the transaction manager
        transactionManager.close();

        // Assert that closing it again throws a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> transactionManager.close());
        assertEquals("Database connection is already closed.", exception.getMessage());
    }
}