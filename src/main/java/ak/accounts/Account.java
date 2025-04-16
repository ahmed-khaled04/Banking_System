package ak.accounts;

import ak.transactions.Transaction;

public abstract class Account {
    protected String accountNumber;
    protected String accountHolderName;
    protected double balance;
    protected String customerId; // New field for customer ID
    static int accountID = 1;

    public Account(String customerId, String accountHolderName, double initialBalance, String accountNumber) {
        this.customerId = customerId;
        this.accountHolderName = accountHolderName;
        if (accountNumber != null && !accountNumber.isEmpty()) {
            this.accountNumber = accountNumber;
        } else {
            this.accountNumber = generateAccountNumber();
        }             
        this.balance = initialBalance;
    }
    public Account(String customerId, String accountHolderName, double initialBalance) {
        this.customerId = customerId;
        this.accountHolderName = accountHolderName;            
        this.accountNumber = generateAccountNumber();
        this.balance = initialBalance;
    }

    protected String generateAccountNumber() {
        return "ACC-" + (int) (Math.random() * 100000);
    }

    public abstract void withdraw(double amount);

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited $" + amount + " to " + accountNumber);
        }
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getAccountType() {
        return this.getClass().getSimpleName();
    }

    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder Name: " + accountHolderName);
        System.out.println("Customer ID: " + customerId);
        System.out.println("Current Balance: $" + String.format("%.2f", balance));
    }

    public void processTransaction(Transaction t) {
        if (t.getToAccount() != null && t.getToAccount().equals(this.accountNumber)) {
            this.deposit(t.getAmount());
        } else if (t.getFromAccount() != null && t.getFromAccount().equals(this.accountNumber)) {
            this.withdraw(t.getAmount());
        }
    }
}