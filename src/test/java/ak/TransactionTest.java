package ak;

import ak.accounts.Account;
import ak.accounts.SavingsAccount;
import ak.transactions.Transaction;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionTest {

    // TRANSACTION CREATION
    @Test
    public void testValidTransactionCreation() {
        Account source = new SavingsAccount("123", "John Doe", 1000.0, 2.5);
        Account destination = new SavingsAccount("456", "Jane Doe", 500.0, 2.5);
        Transaction transaction = new Transaction(200.0, "Transfer", source.getAccountNumber(), destination.getAccountNumber(), null);

        assertEquals(200.0, transaction.getAmount());
        assertEquals("Transfer", transaction.getType());
        assertEquals(source.getAccountNumber(), transaction.getFromAccount());
        assertEquals(destination.getAccountNumber(), transaction.getToAccount());
        assertNotNull(transaction.getTimestamp());

        // Parse the timestamp string to LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime transactionTime = LocalDateTime.parse(transaction.getTimestamp(), formatter);

        // Check if the transaction timestamp is before the current time plus one second
        assertTrue(transactionTime.isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    public void testNegativeAmountThrowsException() {
        Account source = new SavingsAccount("123", "John Doe", 1000.0, 2.5);
        Account destination = new SavingsAccount("456", "Jane Doe", 500.0, 2.5);

        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(-50.0, "Transfer", source.getAccountNumber(), destination.getAccountNumber(), null);
        });
    }

    @Test
    public void testNullAccountsThrowException() {
        Account acc = new SavingsAccount("789", "Alice Smith", 300.0, 2.5);

        assertThrows(NullPointerException.class, () -> new Transaction(100.0, "Transfer", null, acc.getAccountNumber(), null));
        assertThrows(NullPointerException.class, () -> new Transaction(100.0, "Transfer", acc.getAccountNumber(), null, null));
    }
}