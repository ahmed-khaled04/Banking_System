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
        Account source = new SavingsAccount("123", "John Doe", 1000.0, 2.5 , true);
        Account destination = new SavingsAccount("456", "Jane Doe", 500.0, 2.5 , true);
        Transaction transaction = new Transaction(200.0, "Transfer", source.getAccountNumber(),
                destination.getAccountNumber(), null);

        // Ensure transactionId starts with "TXN-"
        assertTrue(transaction.getTransactionId().startsWith("TXN-"));
        // Ensure transactionId has the expected length (8 characters from UUID)
        assertEquals(12, transaction.getTransactionId().length()); 
    }

    @Test
    public void testInvalidTransactionType() {
        Account source = new SavingsAccount("123", "John Doe", 1000.0, 2.5 , true);
        Account destination = new SavingsAccount("456", "Jane Doe", 500.0, 2.5 , true);

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
        Account source = new SavingsAccount("123", "John Doe", 1000.0, 2.5 , true);
        Account destination = new SavingsAccount("456", "Jane Doe", 500.0, 2.5 , true);

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
