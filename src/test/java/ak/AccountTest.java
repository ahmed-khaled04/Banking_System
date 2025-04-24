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
        account = new CheckingAccount("C123", "Yousef Sameh", 1000.0, 500.0, "ACC001", true);
    }

    // ACCOUNT CREATION
    @Test
    public void testAccountCreation() {
        assertEquals("ACC001", account.getAccountNumber());
        assertEquals(1000.0, account.getBalance(), 0.01); // Use delta for double comparison
    }

    @Test
    public void testAccountCreationWithNegativeBalance() {
        assertThrows(IllegalArgumentException.class, () -> 
            new CheckingAccount("C124", "Someone", -500.0, 500.0, "ACC002", true)
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
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(1500.0));
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