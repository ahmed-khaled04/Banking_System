package ak.accounts;

import java.util.*;

public class AccountManager {
    private Map<String, Account> accounts;

    public AccountManager() {
        accounts = new HashMap<>();
    }

    // Create and register a new SavingsAccount
    public Account createSavingsAccount(String holderName) {
        Account account = new SavingsAccount(holderName);
        createAccount(account);
        return account;
    }

    // Create and register a new CheckingAccount
    public Account createCheckingAccount(String holderName) {
        Account account = new CheckingAccount(holderName);
        createAccount(account);
        return account;
    }

    // Register any account type (used internally)
    public boolean createAccount(Account account) {
        if (accounts.containsKey(account.getAccountNumber())) {
            System.out.println("Account already exists.");
            return false;
        }
        accounts.put(account.getAccountNumber(), account);
        System.out.println("Account created successfully for " + account.accountHolderName);
        return true;
    }

    // Delete an account by account number
    public boolean deleteAccount(String accountNumber) {
        if (accounts.containsKey(accountNumber)) {
            accounts.remove(accountNumber);
            System.out.println("Account deleted.");
            return true;
        } else {
            System.out.println("Account not found.");
            return false;
        }
    }

    // Transfer funds between accounts
    public boolean transferFunds(String fromAccountNumber, String toAccountNumber, double amount) {
        Account from = accounts.get(fromAccountNumber);
        Account to = accounts.get(toAccountNumber);

        if (from == null || to == null) {
            System.out.println("One or both accounts not found.");
            return false;
        }

        if (from.getBalance() < amount) {
            System.out.println("Insufficient funds.");
            return false;
        }

        from.withdraw(amount);
        to.deposit(amount);
        System.out.println("Transfer successful.");
        return true;
    }

    // Apply interest to all savings accounts
    public void applyInterestToSavingsAccounts() {
        for (Account account : accounts.values()) {
            if (account instanceof SavingsAccount) {
                ((SavingsAccount) account).addInterest();
            }
        }
        System.out.println("Interest applied to all savings accounts.");
    }

    // Show info for all accounts
    public void displayAllAccounts() {
        for (Account account : accounts.values()) {
            account.displayAccountInfo();
            System.out.println("----------------------------");
        }
    }

    // Look up an account by number
    public Account getAccountByNumber(String accountNumber) {
        return accounts.get(accountNumber);
    }
}
