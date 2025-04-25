package ak;

import ak.accounts.Account;
import ak.accounts.CheckingAccount;
import ak.accounts.SavingsAccount;
import ak.transactions.Transaction;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class AccountWhiteBoxTest {

    // 1. Constructors & activation flag paths
    @Test
    void constructorWithExplicitNumber() {
        CheckingAccount ca = new CheckingAccount("C0", "Holder", 50, 100, "ACC‑EX", false);
        assertEquals("ACC‑EX", ca.getAccountNumber());
        assertFalse(ca.isActivated());
    }

    @Test
    void constructorAutoGeneratesNumber() {
        Account acc = new SavingsAccount("C1", "Auto", 0, 2.0, false);
        assertTrue(acc.getAccountNumber().startsWith("ACC-"));
    }

    // 2. Deposit guard clause (positive vs non‑positive)
    @Test
    void depositRejectsZeroOrNegative() {
        Account acc = new SavingsAccount("C2", "Saver", 100, 3.0 , true);
        assertThrows(IllegalArgumentException.class, () -> acc.deposit(0));
        assertEquals(100, acc.getBalance());

        assertThrows(IllegalArgumentException.class, () -> acc.deposit(-10));
        assertEquals(100, acc.getBalance());
    }

    // 3. processTransaction – all internal branches
    @Test
    void processTransaction_noMatchingAccount_noChange() {
        Account acc = new SavingsAccount("C3", "Uniq", 200, 2.0, true);
        Transaction t = new Transaction(50, "transfer", "OTHER", "ELSE", null);
        acc.processTransaction(t);
        assertEquals(200, acc.getBalance());
    }

    @Test
    void processTransaction_invalidBothNull() {
        Account acc = new SavingsAccount("C3", "Uniq", 200, 2.0, true);
        assertThrows(IllegalArgumentException.class, () -> {
            Transaction t = new Transaction(50, "Bad", null, null, null);
            acc.processTransaction(t);
        });
        assertEquals(200, acc.getBalance());
    }

    // 4. SavingsAccount specific paths
    @Test
    void savingsWithdraw_exactBalance() {
        SavingsAccount sa = new SavingsAccount("C4", "SaverX", 100, 1.0 , true);
        sa.withdraw(100);
        assertEquals(0, sa.getBalance(), 0.0001);
    }

    // 5. CheckingAccount overdraft – edge at exactly limit
    @Test
    void overdraftExactLimitAllowed() {
        CheckingAccount ca = new CheckingAccount("C5", "Checker", 0, 300, "ACC‑OD", true);
        ca.withdraw(300);
        assertEquals(-300, ca.getBalance(), 0.0001);
    }

    // 6. Activation mutator
    @Test
    void setActivatedTogglesFlag() {
        Account acc = new CheckingAccount("C6", "Flaggy", 10, 50, true);
        acc.setActivated(false);
        assertFalse(acc.isActivated());
    }

    // 7. Interest addition precision edge
    @Test
    void addInterest_tinyBalanceRoundsUp() {
        SavingsAccount sa = new SavingsAccount("C7", "Tiny", 0.01, 10.0 , true);
        sa.addInterest();
        assertTrue(sa.getBalance() > 0.01);
    }

    // 8. Overdraft slightly over the limit
    @Test
    void overdraftSlightlyOverLimitRejected() {
        CheckingAccount ca = new CheckingAccount("C8", "Edge", 0, 100, "ACC‑EDGE", true);
        assertThrows(IllegalArgumentException.class, () -> ca.withdraw(100.0001));
        assertEquals(0, ca.getBalance(), 0.0001);
    }

    // Account-Transaction interaction
    @Test
    void withdrawFromInactiveAccountThrows() {
        Account acc = new SavingsAccount("C9", "Inactive", 500, 1.5, false);
        assertThrows(IllegalStateException.class, () -> acc.withdraw(100));
        assertEquals(acc.getBalance(), 500);
    }

    @Test
    void depositToInactiveAccountThrows() {
        Account acc = new SavingsAccount("C10", "Inactive", 100, 2.0, false);
        assertThrows(IllegalStateException.class, () -> acc.deposit(50));
        assertEquals(acc.getBalance(), 100);
    }

    @Test
    void savingsWithdrawLeavesZeroBalance() {
        SavingsAccount sa = new SavingsAccount("C11", "EmptyOut", 250, 1.0, true);
        sa.withdraw(250);
        assertEquals(0, sa.getBalance(), 0.0001);
    }

    @Test
    void toggleActivationTwiceRestoresOriginalState() {
        Account acc = new CheckingAccount("C12", "FlipFlop", 100, 50, true);
        acc.setActivated(false);
        acc.setActivated(true);
        assertTrue(acc.isActivated());
    }

    @Test
    void addInterestWhenDeactivatedThrows() {
        SavingsAccount acc = new SavingsAccount("C13", "Dormant", 1000, 5.0, false);
        assertThrows(IllegalStateException.class, acc::addInterest);
    }
}