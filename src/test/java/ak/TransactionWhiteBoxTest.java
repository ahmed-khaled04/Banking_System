package ak;

import ak.accounts.Account;
import ak.accounts.SavingsAccount;
import ak.transactions.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionWhiteBoxTest {

    // Test: generateTransactionId format
    @Test
    public void testGenerateTransactionId() {
        Account source = new SavingsAccount("123", "John Doe", 1000.0, 2.5, true);
        Account destination = new SavingsAccount("456", "Jane Doe", 500.0, 2.5, true);
        Transaction transaction = new Transaction(200.0, "Transfer", source.getAccountNumber(),
                destination.getAccountNumber(), null);

        assertTrue(transaction.getTransactionId().startsWith("TXN-"));
        assertEquals(12, transaction.getTransactionId().length());
    }

    // Invalid transaction types
    @Test
    public void testInvalidTransactionType() {
        Account source = new SavingsAccount("123", "John Doe", 1000.0, 2.5, true);
        Account destination = new SavingsAccount("456", "Jane Doe", 500.0, 2.5, true);

        assertThrows(IllegalArgumentException.class, () ->
            new Transaction(200.0, "", source.getAccountNumber(), destination.getAccountNumber(), null)
        );

        assertThrows(IllegalArgumentException.class, () ->
            new Transaction(200.0, null, source.getAccountNumber(), destination.getAccountNumber(), null)
        );
    }

    // Invalid amounts (zero or negative)
    @Test
    public void testInvalidAmount() {
        Account source = new SavingsAccount("123", "John Doe", 1000.0, 2.5, true);
        Account destination = new SavingsAccount("456", "Jane Doe", 500.0, 2.5, true);

        assertThrows(IllegalArgumentException.class, () ->
            new Transaction(0.0, "Transfer", source.getAccountNumber(), destination.getAccountNumber(), null)
        );

        assertThrows(IllegalArgumentException.class, () ->
            new Transaction(-100.0, "Transfer", source.getAccountNumber(), destination.getAccountNumber(), null)
        );
    }

    // Null source/destination accounts
    @Test
    public void testNullAccounts() {
        assertThrows(IllegalArgumentException.class, () ->
            new Transaction(200.0, "Transfer", null, null, null)
        );

        assertThrows(IllegalArgumentException.class, () ->
            new Transaction(200.0, "Transfer", null, "456", null)
        );

        assertThrows(IllegalArgumentException.class, () ->
            new Transaction(200.0, "Transfer", "123", null, null)
        );
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
        assertThrows(IllegalArgumentException.class, () ->
            new Transaction(100.0, "@Withdraw!", "ACC001", "ACC002", null)
        );
    }

    // Basic uniqueness check for transaction ID
    @Test
    public void testTransactionIdUniqueness() {
        Transaction t1 = new Transaction(50.0, "Deposit", "ACC001", "ACC002", null);
        Transaction t2 = new Transaction(75.0, "Deposit", "ACC003", "ACC004", null);

        assertNotEquals(t1.getTransactionId(), t2.getTransactionId());
    }
}