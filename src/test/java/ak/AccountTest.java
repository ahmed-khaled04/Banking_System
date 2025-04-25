package ak;

import ak.accounts.Account;
import ak.accounts.CheckingAccount;
//import ak.accounts.SavingsAccount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    private Account account;

    @BeforeEach
    public void setUp() {
        account = new CheckingAccount("C123", "Yousef Sameh", 1000.0, 500.0, true);
    }

    // ACCOUNT CREATION
    @Test
    public void testAccountCreation() {
        assertNotNull(account, "Account should be created successfully.");
        assertEquals(1000.0, account.getBalance(), 0.01); // Use delta for double comparison
    }

    @Test
    public void testAccountNumberFormat() {
        String accountNumber = account.getAccountNumber();
        assertTrue(accountNumber.matches("ACC-\\d{6}"), "Account number should start with 'ACC-' followed by 6 digits.");
    }

    @Test
    public void testAccountCreationWithNegativeBalance() {
        assertThrows(IllegalArgumentException.class, () -> 
            new CheckingAccount("C124", "Someone", -500.0, 500.0, true),
            "Expected IllegalArgumentException for negative initial balance."
        );
    }

    // DEPOSIT FUNCTIONALITY
    @Test
    public void testDepositPositiveAmount() {
        account.deposit(500.0);
        assertEquals(1500.0, account.getBalance(), 0.01); // Use delta for double comparison
    }

    @Test
    public void testDepositNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-100.0));
    }

    // WITHDRAWAL FUNCTIONALITY
    @Test
    public void testWithdrawValidAmount() {
        account.withdraw(200.0);
        assertEquals(800.0, account.getBalance(), 0.01); // Use delta for double comparison
    }

    @Test
    public void testWithdrawAmountGreaterThanBalance() {
        assertThrows(IllegalArgumentException.class, () -> 
            account.withdraw(1600.0),
            "Expected IllegalArgumentException for withdrawal amount greater than balance."
        );
    }

    @Test
    public void testWithdrawNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-100.0));
    }

    // BALANCE INQUIRY
    @Test
    public void testGetBalance() {
        assertEquals(1000.0, account.getBalance(), 0.01); // Use delta for double comparison
    }
}