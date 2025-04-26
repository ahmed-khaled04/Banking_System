package ak;

import ak.accounts.Account;
import ak.accounts.AccountManager;
import ak.accounts.CheckingAccount;
import ak.accounts.SavingsAccount;
import ak.customer.Customer;
import ak.customer.CustomerManager;
import ak.database.DBconnection;
import ak.transactions.Transaction;
import ak.transactions.TransactionManager;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountWhiteBoxTest {

    private Account account;
    private AccountManager accountManager;
    private CustomerManager customerManager;
    private TransactionManager transactionmanager;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        DBconnection.clearDatabase(); 


        accountManager = new AccountManager();
        customerManager = new CustomerManager();
        transactionmanager = new TransactionManager(accountManager);
        customer = customerManager.addCustomer("John Doe", "email.com", "1111", "username1", "passwordHash1");
        account = new CheckingAccount(customer.getCustomerId(), "Yousef Sameh", 1000.0, 500.0, true);
    }

    // ---- Account Creation Tests ----
    @Test
    void testAccountCreation() {
        assertNotNull(account, "Account should be created successfully.");
        assertEquals(1000.0, account.getBalance(), 0.01);
    }

    @Test
    void testAccountNumberFormat() {
        String accountNumber = account.getAccountNumber();
        assertTrue(accountNumber.matches("ACC-\\d{6}"), "Account number format should match.");
    }

    @Test
    void testAccountCreationWithNegativeBalance() {
        assertThrows(IllegalArgumentException.class, () -> new CheckingAccount("C124", "Someone", -500.0, 500.0, true));
    }

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

    // ---- Deposit Tests ----
    @Test
    void testDepositPositiveAmount() {
        account.deposit(500.0);
        assertEquals(1500.0, account.getBalance(), 0.01);
    }

    @Test
    void testDepositNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-100.0));
    }

    @Test
    void depositRejectsZeroOrNegative() {
        Account acc = new SavingsAccount("C2", "Saver", 100, 3.0, true);
        assertThrows(IllegalArgumentException.class, () -> acc.deposit(0));
        assertEquals(100, acc.getBalance());

        assertThrows(IllegalArgumentException.class, () -> acc.deposit(-10));
        assertEquals(100, acc.getBalance());
    }

    // ---- Withdrawal Tests ----
    @Test
    void testWithdrawValidAmount() {
        account.withdraw(200.0);
        assertEquals(800.0, account.getBalance(), 0.01);
    }

    @Test
    void testWithdrawAmountGreaterThanBalance() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(1600.0));
    }

    @Test
    void testWithdrawNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-100.0));
    }

    @Test
    void savingsWithdraw_exactBalance() {
        SavingsAccount sa = new SavingsAccount("C4", "SaverX", 100, 1.0, true);
        sa.withdraw(100);
        assertEquals(0, sa.getBalance(), 0.0001);
    }

    @Test
    void overdraftExactLimitAllowed() {
        CheckingAccount ca = new CheckingAccount("C5", "Checker", 0, 300, "ACC‑OD", true);
        ca.withdraw(300);
        assertEquals(-300, ca.getBalance(), 0.0001);
    }

    @Test
    void overdraftSlightlyOverLimitRejected() {
        CheckingAccount ca = new CheckingAccount("C8", "Edge", 0, 100, "ACC‑EDGE", true);
        assertThrows(IllegalArgumentException.class, () -> ca.withdraw(100.0001));
        assertEquals(0, ca.getBalance(), 0.0001);
    }

    @Test
    void savingsWithdrawLeavesZeroBalance() {
        SavingsAccount sa = new SavingsAccount("C11", "EmptyOut", 250, 1.0, true);
        sa.withdraw(250);
        assertEquals(0, sa.getBalance(), 0.0001);
    }

    @Test
    void withdrawFromInactiveAccountThrows() {
        Account acc = new SavingsAccount("C9", "Inactive", 500, 1.5, false);
        assertThrows(IllegalStateException.class, () -> acc.withdraw(100));
        assertEquals(500, acc.getBalance());
    }

    // ---- Balance Inquiry ----
    @Test
    void testGetBalance() {
        assertEquals(1000.0, account.getBalance(), 0.01);
    }

    // ---- Transaction Processing ----
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

    // ---- Activation + Interest ----
    @Test
    void setActivatedTogglesFlag() {
        Account acc = new CheckingAccount("C6", "Flaggy", 10, 50, true);
        acc.setActivated(false);
        assertFalse(acc.isActivated());
    }

    @Test
    void toggleActivationTwiceRestoresOriginalState() {
        Account acc = new CheckingAccount("C12", "FlipFlop", 100, 50, true);
        acc.setActivated(false);
        acc.setActivated(true);
        assertTrue(acc.isActivated());
    }

    @Test
    void addInterest_tinyBalanceRoundsUp() {
        SavingsAccount sa = new SavingsAccount("C7", "Tiny", 0.01, 10.0, true);
        sa.addInterest();
        assertTrue(sa.getBalance() > 0.01);
    }

    @Test
    void depositToInactiveAccountThrows() {
        Account acc = new SavingsAccount("C10", "Inactive", 100, 2.0, false);
        assertThrows(IllegalStateException.class, () -> acc.deposit(50));
        assertEquals(100, acc.getBalance());
    }

    @Test
    void addInterestWhenDeactivatedThrows() {
        SavingsAccount acc = new SavingsAccount("C13", "Dormant", 1000, 5.0, false);
        assertThrows(IllegalStateException.class, acc::addInterest);
    }

    // ---- Extra Constructor / Getter Tests ----
    @Test
    void testGetCustomerId() {
        Account acc = new SavingsAccount("C100", "TestCustomer", 500, 1.0, true);
        assertEquals("C100", acc.getCustomerId());
    }

    @Test
    void testGetHolderName() {
        Account acc = new CheckingAccount("C101", "Ali Baba", 300, 100, true);
        assertEquals("Ali Baba", acc.getAccountHolderName());
    }

    @Test
    void testIsActivatedTrueInitially() {
        Account acc = new CheckingAccount("C102", "ActiveGuy", 250, 150, true);
        assertTrue(acc.isActivated());
    }

    // ---- Edge Case: Deposit zero to inactive account should throw ----
    @Test
    void depositZeroToInactiveThrows() {
        Account acc = new SavingsAccount("C103", "Inactive", 100, 2.0, false);
        assertThrows(IllegalStateException.class, () -> acc.deposit(0));
    }

    // ---- Edge Case: Withdraw zero from inactive account should throw ----
    @Test
    void withdrawZeroFromInactiveThrows() {
        Account acc = new CheckingAccount("C104", "Inactive", 100, 100, false);
        assertThrows(IllegalStateException.class, () -> acc.withdraw(0));
    }

    // ---- Edge Case: Withdraw exact overdraft limit to test inclusive behavior
    @Test
    void checkingWithdrawToLimit() {
        CheckingAccount acc = new CheckingAccount("C105", "Overdrafter", 0, 100, true);
        acc.withdraw(100);
        assertEquals(-100, acc.getBalance(), 0.001);
    }

    // ---- Boundary Test: Add interest on large balance ----
    @Test
    void addInterestHighBalance() {
        SavingsAccount acc = new SavingsAccount("C106", "Richie", 1_000_000, 5.0, true);
        acc.addInterest();
        assertTrue(acc.getBalance() > 1_000_000);
    }

    // ---- Edge: Add interest 0% rate shouldn't change balance ----
    @Test
    void addZeroPercentInterestShouldNotChangeBalance() {
        SavingsAccount acc = new SavingsAccount("C107", "ZeroRate", 1000, 0.0, true);
        assertThrows(IllegalArgumentException.class, () -> acc.addInterest());
        assertEquals(1000, acc.getBalance(), 0.0001);
    }

    // ---- Edge: Add interest on 0 balance ----
    @Test
    void addInterestOnZeroBalanceShouldStayZero() {
        SavingsAccount acc = new SavingsAccount("C108", "Broke", 0.0, 10.0, true);
        assertThrows(IllegalArgumentException.class, () -> acc.addInterest());
        assertEquals(0.0, acc.getBalance(), 0.0001);
    }

    // ---- Edge Case: Toggle activation twice and confirm flips ----
    @Test
    void toggleActivationOffThenOn() {
        Account acc = new CheckingAccount("C109", "Toggle", 100, 50, true);
        acc.setActivated(false);
        assertFalse(acc.isActivated());
        acc.setActivated(true);
        assertTrue(acc.isActivated());
    }

    // ---- Null Safety Test for Transaction object with null fields ----
    @Test
    void processTransactionWithNullTypeThrows() {
        Account acc = new SavingsAccount("C110", "TypeNull", 200, 2.0, true);
        assertThrows(IllegalArgumentException.class, () ->{
            Transaction t = new Transaction(50, null, "ACC123", "ACC456", null);
            assertThrows(IllegalArgumentException.class, () -> acc.processTransaction(t));
            
        });
    }

    // ---- SavingsAccount Specific Tests ----
    @Test
    void testSavingsAccountCreationWithNegativeInterestRate() {
        assertThrows(IllegalArgumentException.class, () -> new SavingsAccount("C111", "Test User", 1000, -1.0, true));
    }

    @Test
    void testSavingsAccountAddInterest() {
        SavingsAccount sa = new SavingsAccount("C112", "Test User", 1000, 5.0, true);
        sa.addInterest();
        assertEquals(1000 * 1.05, sa.getBalance(), 0.01);
    }

    @Test
    void testSavingsAccountAddInterestInactive() {
        SavingsAccount sa = new SavingsAccount("C113", "Test User", 1000, 5.0, false);
        assertThrows(IllegalStateException.class, sa::addInterest);
    }

    // ---- CheckingAccount Specific Tests ----
    @Test
    void testCheckingAccountCreationWithNegativeOverdraftLimit() {
        assertThrows(IllegalArgumentException.class, () -> new CheckingAccount("C114", "Test User", 1000, -100, true));
    }

    @Test
    void testCheckingAccountWithdrawWithinOverdraftLimit() {
        CheckingAccount ca = new CheckingAccount("C115", "Test User", 0, 300, true);
        ca.withdraw(300);
        assertEquals(-300, ca.getBalance(), 0.01);
    }

    @Test
    void testCheckingAccountWithdrawExceedingOverdraftLimit() {
        CheckingAccount ca = new CheckingAccount("C116", "Test User", 0, 300, true);
        assertThrows(IllegalArgumentException.class, () -> ca.withdraw(301));
    }

    @Test
    void testCheckingAccountWithdrawInactive() {
        CheckingAccount ca = new CheckingAccount("C117", "Test User", 1000, 300, false);
        assertThrows(IllegalStateException.class, () -> ca.withdraw(100));
    }

    // ---- AccountManager Tests ----
    @Test
    void testAccountManagerInitialization() {
        Account acc = accountManager.createSavingsAccount("C0", "Test", 100, 1.0);
        assertNotNull(acc, "AccountManager should be initialized and able to create accounts");
    }

    @Test
    void testCreateSavingsAccount() {
        Account acc = accountManager.createSavingsAccount("C1", "John Doe", 1000, 5.0);
        assertNotNull(acc, "Savings account should be created.");
        assertEquals("John Doe", acc.getAccountHolderName(), "Holder name should match.");
        assertEquals(1000, acc.getBalance(), 0.01, "Initial balance should match.");
    }

    @Test
    void testCreateCheckingAccount() {
        Account acc = accountManager.createCheckingAccount("C2", "Jane Doe", 500, 100);
        assertNotNull(acc, "Checking account should be created.");
        assertEquals("Jane Doe", acc.getAccountHolderName(), "Holder name should match.");
        assertEquals(500, acc.getBalance(), 0.01, "Initial balance should match.");
    }

    @Test
    void testDeleteAccount() {
        Account acc = accountManager.createSavingsAccount(customer.getCustomerId(), "Test User", 100, 1.0);
        assertTrue(accountManager.deleteAccount(acc.getAccountNumber()), "Account should be deleted.");
    }

    @Test
    void testDeleteNonExistingAccount() {
        assertFalse(accountManager.deleteAccount("ACC-999999"), "Non-existing account should not be deleted.");
    }

    @Test
    void testProcessTransactionDeposit() {
        Account acc = accountManager.createSavingsAccount(customer.getCustomerId(), "Test User", 100, 1.0);
        Transaction t = transactionmanager.createTransaction(50, "deposit", null, acc.getAccountNumber());

        acc = accountManager.getAccountByNumber(acc.getAccountNumber());

        assertEquals(150, acc.getBalance(), 0.01, "Balance should be updated.");
    }

    @Test
    void testProcessTransactionWithdrawal() {
        Account acc = accountManager.createSavingsAccount(customer.getCustomerId(), "Test User", 100, 1.0);
        Transaction t = transactionmanager.createTransaction(50, "withdraw",acc.getAccountNumber(), null);

        acc = accountManager.getAccountByNumber(acc.getAccountNumber());

        assertEquals(50, acc.getBalance(), 0.01, "Balance should be updated.");
    }

    @Test
    void testProcessTransactionTransfer() {
        Account fromAcc = accountManager.createSavingsAccount(customer.getCustomerId(), "From User", 100, 1.0);
        Account toAcc = accountManager.createSavingsAccount(customer.getCustomerId(), "To User", 100, 1.0);
        Transaction t = transactionmanager.createTransaction(50, "transfer",fromAcc.getAccountNumber(), toAcc.getAccountNumber());

        fromAcc = accountManager.getAccountByNumber(fromAcc.getAccountNumber());
        toAcc = accountManager.getAccountByNumber(toAcc.getAccountNumber());
        
        assertEquals(50, fromAcc.getBalance(), 0.01, "From account balance should be updated.");
        assertEquals(150, toAcc.getBalance(), 0.01, "To account balance should be updated.");
    }


    @Test
    void testGetAccountByNumber() {
        Account acc = accountManager.createSavingsAccount(customer.getCustomerId(), "Test User", 100, 1.0);
        Account retrievedAcc = accountManager.getAccountByNumber(acc.getAccountNumber());
        assertNotNull(retrievedAcc, "Account should be retrieved.");
        assertEquals(acc.getBalance(), retrievedAcc.getBalance(), 0.01, "Balances should match.");
    }

    @Test
    void testUpdateAccountActivation() {
        Account acc = accountManager.createSavingsAccount(customer.getCustomerId(), "Test User", 100, 1.0);
        assertTrue(accountManager.updateAccountActivation(acc.getAccountNumber(), false),
                "Account should be deactivated.");
        acc = accountManager.getAccountByNumber(acc.getAccountNumber());
        assertFalse(acc.isActivated(), "Account should be deactivated.");
    }

    @Test
    void testIsAccountOwnedByCustomer() {
        Account acc = accountManager.createSavingsAccount(customer.getCustomerId(), "Test User", 100, 1.0);
        assertTrue(accountManager.isAccountOwnedByCustomer(acc.getAccountNumber(), customer.getCustomerId()),
                "Account should be owned by the customer.");
    }

    @Test
    void testDisplayAllAccounts() {
        accountManager.createSavingsAccount("C12", "Test User", 100, 1.0);
        accountManager.displayAllAccounts(); // This will trigger print, covered ✅
    }

    @Test
    void testCreateAccountWithNegativeOverdraftLimit() {
        assertThrows(IllegalArgumentException.class,
                () -> accountManager.createCheckingAccount("C13", "Test User", 100, -100));
    }

    @Test
    void testProcessInvalidTransaction() {
        assertThrows(IllegalArgumentException.class, () -> {
            Transaction t = new Transaction(50, "invalid", null, null, null);
            accountManager.processTransaction(t);
        });
    }

    @AfterEach
    public void tearDown() {
        try {
            DBconnection.closeConnection(DBconnection.getConnection()); // Close the database connection after each test
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging purposes
            fail("Failed to close the database connection: " + e.getMessage());
        }
    }
}