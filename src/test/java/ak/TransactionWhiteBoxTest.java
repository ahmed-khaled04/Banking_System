package ak;

import ak.accounts.Account;
import ak.accounts.SavingsAccount;
import ak.transactions.Transaction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionWhiteBoxTest {

    // Test to ensure generateTransactionId works correctly
    @Test
    public void testGenerateTransactionId() {
        // Creating a transaction instance, which will internally call
        // generateTransactionId()
        Account source = new SavingsAccount("123", "John Doe", 1000.0, 2.5);
        Account destination = new SavingsAccount("456", "Jane Doe", 500.0, 2.5);
        Transaction transaction = new Transaction(200.0, "Transfer", source.getAccountNumber(),
                destination.getAccountNumber(), null);

        // Ensure transactionId starts with "TXN-"
        assertTrue(transaction.getTransactionId().startsWith("TXN-"));
        // Ensure transactionId has the expected length (8 characters from UUID)
        assertEquals(13, transaction.getTransactionId().length()); // "TXN-" + 8 characters from UUID
    }

    // Test to ensure that the generateTimestamp method uses the correct pattern
    @Test
    public void testGenerateTimestamp() {
        // Creating a transaction instance, which will internally call
        // generateTimestamp()
        Account source = new SavingsAccount("123", "John Doe", 1000.0, 2.5);
        Account destination = new SavingsAccount("456", "Jane Doe", 500.0, 2.5);
        Transaction transaction = new Transaction(200.0, "Transfer", source.getAccountNumber(),
                destination.getAccountNumber(), null);

        String timestamp = transaction.getTimestamp();
        assertNotNull(timestamp);

        // Ensure the timestamp is in the correct format (yyyy-MM-dd HH:mm:ss)
        String regex = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
        assertTrue(timestamp.matches(regex));
    }

    // Test to ensure that the internal logic for invalid transaction types throws
    // an exception
    @Test
    public void testInvalidTransactionType() {
        Account source = new SavingsAccount("123", "John Doe", 1000.0, 2.5);
        Account destination = new SavingsAccount("456", "Jane Doe", 500.0, 2.5);

        // Using an invalid transaction type (empty string)
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(200.0, "", source.getAccountNumber(), destination.getAccountNumber(), null);
        });

        // Using a null transaction type
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(200.0, null, source.getAccountNumber(), destination.getAccountNumber(), null);
        });
    }

    // Test to check for invalid amount (less than or equal to zero)
    @Test
    public void testInvalidAmount() {
        Account source = new SavingsAccount("123", "John Doe", 1000.0, 2.5);
        Account destination = new SavingsAccount("456", "Jane Doe", 500.0, 2.5);

        // Testing with zero amount
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(0.0, "Transfer", source.getAccountNumber(), destination.getAccountNumber(), null);
        });

        // Testing with negative amount
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(-100.0, "Transfer", source.getAccountNumber(), destination.getAccountNumber(), null);
        });
    }

    // Test to check that null values for both source and destination accounts throw
    // an exception
    @Test
    public void testNullAccounts() {
        // Testing with both source and destination accounts being null
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(200.0, "Transfer", null, null, null);
        });

        // Testing with source account being null
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(200.0, "Transfer", null, "456", null);
        });

        // Testing with destination account being null
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(200.0, "Transfer", "123", null, null);
        });
    }
}
