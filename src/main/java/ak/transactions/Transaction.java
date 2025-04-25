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
    private String createdAt;

    public Transaction(double amount, String type, String fromAccount, String toAccount , String createdAt) {
        this.transactionId = generateTransactionId();
        if (amount <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction type cannot be empty");
        }
        if (fromAccount == null && toAccount == null) {
            throw new IllegalArgumentException("At least one account (from or to) must be specified");
        }
        if(type.equalsIgnoreCase("transfer")){
            if (fromAccount == null || toAccount == null) {
                throw new IllegalArgumentException("Both accounts must be specified for transfer transactions");
            }
        }
        
        this.amount = amount;
        this.type = type;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.createdAt = createdAt;
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
        System.out.println("Time: " + createdAt);
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
        return createdAt;
    }

    public String getDate(){
        return createdAt;
    }
}
