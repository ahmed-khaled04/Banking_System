package ak.transactions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Transaction {
    private String transactionId;
    private double amount;
    private String type; // e.g., "Deposit", "Withdrawal", "Transfer"
    private String fromAccount;
    private String toAccount;
    private String timestamp;

    public Transaction(double amount, String type, String fromAccount, String toAccount) {
        this.transactionId = generateTransactionId();
        this.amount = amount;
        this.type = type;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.timestamp = generateTimestamp();
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateTimestamp() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }

    public void printTransactionDetails() {
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Type: " + type);
        System.out.println("Amount: $" + amount);
        System.out.println("From: " + fromAccount);
        System.out.println("To: " + toAccount);
        System.out.println("Time: " + timestamp);
    }

    // Getters
    public String getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
