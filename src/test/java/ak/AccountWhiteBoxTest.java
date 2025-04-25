package ak;

import ak.accounts.Account;
import ak.accounts.CheckingAccount;
import ak.accounts.SavingsAccount;
import ak.transactions.Transaction;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class AccountWhiteBoxTest {

    /*
     * -------------------------------------------------
     * 1. Constructors & activation flag paths
     * -------------------------------------------------
     */
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

    /*
     * -------------------------------------------------
     * 2. Deposit guard clause (positive vs non‑positive)
     * -------------------------------------------------
     */
    @Test
    void depositRejectsZeroOrNegative() {
        Account acc = new SavingsAccount("C2", "Saver", 100, 3.0 , true);
        assertThrows(IllegalArgumentException.class, () -> acc.deposit(0) , 
        "Expected IllegalArgumentException for zero deposit.");
        assertEquals(100, acc.getBalance());
        
    
        assertThrows(IllegalArgumentException.class, () -> acc.deposit(-10) , 
        "Expected IllegalArgumentException for zero deposit.");
        assertEquals(100, acc.getBalance());
    }

    /*
     * -------------------------------------------------
     * 3. processTransaction – all internal branches
     * -------------------------------------------------
     */
    @Test
    void processTransaction_noMatchingAccount_noChange() {
        Account acc = new SavingsAccount("C3", "Uniq", 200, 2.0, true);
        Transaction t = new Transaction(50, "Irrelevant", "OTHER", "ELSE", null);

        acc.processTransaction(t);
        assertEquals(200, acc.getBalance());
    }

    /*
     * deposit path already covered previously; add withdrawal when both from & to
     * null
     */
    @Test
    void processTransaction_invalidBothNull() {
        Account acc = new SavingsAccount("C3", "Uniq", 200, 2.0, true);
        assertThrows(IllegalArgumentException.class, () -> {
            Transaction t = new Transaction(50, "Bad", null, null, null);
            acc.processTransaction(t);
        });
        // balance unchanged
        assertEquals(200, acc.getBalance());
    }

    /*
     * -------------------------------------------------
     * 4. SavingsAccount specific paths
     * -------------------------------------------------
     */
    @Test
    void savingsWithdraw_exactBalance() {
        SavingsAccount sa = new SavingsAccount("C4", "SaverX", 100, 1.0 , true);
        sa.withdraw(100);
        assertEquals(0, sa.getBalance(), 0.0001);
    }

    /*
     * -------------------------------------------------
     * 5. CheckingAccount overdraft – edge at exactly limit
     * -------------------------------------------------
     */
    @Test
    void overdraftExactLimitAllowed() {
        CheckingAccount ca = new CheckingAccount("C5", "Checker", 0, 300, "ACC‑OD", true);
        ca.withdraw(300); // exactly limit
        assertEquals(-300, ca.getBalance(), 0.0001);
    }

    /*
     * -------------------------------------------------
     * 6. Activation mutator
     * -------------------------------------------------
     */
    @Test
    void setActivatedTogglesFlag() {
        Account acc = new CheckingAccount("C6", "Flaggy", 10, 50, true);
        acc.setActivated(false);
        assertFalse(acc.isActivated());
    }

    /*
     * -------------------------------------------------
     * 7. Interest addition precision edge (very small balance)
     * -------------------------------------------------
     */
    @Test
    void addInterest_tinyBalanceRoundsUp() {
        SavingsAccount sa = new SavingsAccount("C7", "Tiny", 0.01, 10.0 , true);
        sa.addInterest(); // adds 0.001

        assertTrue(sa.getBalance() > 0.01); // balance increased
    }

    /*
     * -------------------------------------------------
     * 8. Overdraft branch when amount == balance + limit + ε
     * -------------------------------------------------
     */
    @Test
    void overdraftSlightlyOverLimitRejected() {
        CheckingAccount ca = new CheckingAccount("C8", "Edge", 0, 100, "ACC‑EDGE", true);
        assertThrows(IllegalArgumentException.class, () -> ca.withdraw(100.0001),
                "Expected IllegalArgumentException for overdraft slightly over limit.");

        assertEquals(0, ca.getBalance(), 0.0001); // unchanged
    }
}
