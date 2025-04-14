package ak.transactions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ak.accounts.Account;
import ak.accounts.AccountManager;

public class TransactionManager {
    private List<Transaction> transactions;
    private AccountManager accountManager;

    public TransactionManager(AccountManager accountManager) {
        this.transactions = new ArrayList<>();
        this.accountManager = accountManager;
    }

    /**
     * Records a new transaction in the system
     * @param amount Transaction amount (must be positive)
     * @param type Transaction type (e.g., "Deposit", "Withdrawal", "Transfer")
     * @param fromAccount Source account ID (can be null for deposits)
     * @param toAccount Destination account ID (can be null for withdrawals)
     * @return The newly created Transaction object
     * @throws IllegalArgumentException if amount is invalid or type is null/empty
     */
    public Transaction createTransaction(double amount, String type, String fromAccount, String toAccount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction type cannot be empty");
        }
        if (fromAccount == null && toAccount == null) {
            throw new IllegalArgumentException("At least one account (from or to) must be specified");
        }

        if (toAccount != null) {
            // This would require access to AccountManager
            System.out.println("[DEBUG] Should deposit to " + toAccount);
        }
        if (fromAccount != null) {
            System.out.println("[DEBUG] Should withdraw from " + fromAccount);
        }


        Transaction newTransaction = new Transaction(amount, type, fromAccount, toAccount);
        transactions.add(newTransaction);

        // Update account balances
        if (toAccount != null) {
            Account acc = accountManager.getAccountByNumber(toAccount);
            if (acc != null) acc.deposit(amount);
        }
        if (fromAccount != null) {
            Account acc = accountManager.getAccountByNumber(fromAccount);
            if (acc != null) acc.withdraw(amount);
        }

        System.out.println("Transaction recorded: " + newTransaction.getTransactionId());
        return newTransaction;
    }

    /**
     * Retrieves a transaction by its ID
     * @param transactionId The ID of the transaction to find
     * @return The Transaction object if found, null otherwise
     */
    public Transaction getTransactionById(String transactionId) {
        return transactions.stream()
                .filter(t -> t.getTransactionId().equals(transactionId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all transactions for a specific account
     * @param accountId The account ID to search for
     * @return List of transactions involving the account
     */
    public List<Transaction> getTransactionsByAccount(String accountId) {
        return transactions.stream()
                .filter(t -> accountId.equals(t.getFromAccount()) || accountId.equals(t.getToAccount()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all transactions in the system
     * @return List of all transactions
     */
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions); // Return a copy to prevent external modification
    }

    /**
     * Retrieves transactions filtered by type
     * @param type The transaction type to filter by (e.g., "Deposit")
     * @return List of matching transactions
     */
    public List<Transaction> getTransactionsByType(String type) {
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves transactions within a specific time range
     * @param startTime Start timestamp (inclusive, format "yyyy-MM-dd HH:mm:ss")
     * @param endTime End timestamp (inclusive, format "yyyy-MM-dd HH:mm:ss")
     * @return List of transactions within the time range
     */
    public List<Transaction> getTransactionsByTimeRange(String startTime, String endTime) {
        return transactions.stream()
                .filter(t -> t.getTimestamp().compareTo(startTime) >= 0 && 
                             t.getTimestamp().compareTo(endTime) <= 0)
                .collect(Collectors.toList());
    }

    /**
     * Calculates the total balance change for an account from all transactions
     * @param accountId The account ID to calculate for
     * @return Net balance change (positive for deposits, negative for withdrawals)
     */
    public double calculateAccountBalanceChange(String accountId) {
        return transactions.stream()
                .mapToDouble(t -> {
                    if (accountId.equals(t.getToAccount())) {
                        return t.getAmount(); // Deposits to this account
                    } else if (accountId.equals(t.getFromAccount())) {
                        return -t.getAmount(); // Withdrawals from this account
                    }
                    return 0;
                })
                .sum();
    }

    /**
     * Prints transaction history for an account
     * @param accountId The account ID to print history for
     */
    public void printAccountStatement(String accountId) {
        List<Transaction> accountTransactions = getTransactionsByAccount(accountId);
        if (accountTransactions.isEmpty()) {
            System.out.println("No transactions found for account: " + accountId);
            return;
        }

        System.out.println("Transaction Statement for Account: " + accountId);
        System.out.println("--------------------------------------------");
        for (Transaction t : accountTransactions) {
            t.printTransactionDetails();
            System.out.println("--------------------------------------------");
        }
        System.out.printf("Net Balance Change: $%.2f%n", calculateAccountBalanceChange(accountId));
    }

    /**
     * Reverses a transaction by creating an opposite transaction
     * @param transactionId The ID of the transaction to reverse
     * @return The reversal transaction, or null if original transaction wasn't found
     */
    public Transaction reverseTransaction(String transactionId) {
        Transaction original = getTransactionById(transactionId);
        if (original == null) {
            System.out.println("Transaction not found with ID: " + transactionId);
            return null;
        }

        String reversalType = "Reversal of " + original.getType();
        Transaction reversal = createTransaction(
            original.getAmount(),
            reversalType,
            original.getToAccount(), // Reverse the flow
            original.getFromAccount()
        );

        System.out.println("Transaction reversed: " + original.getTransactionId());
        return reversal;
    }
}