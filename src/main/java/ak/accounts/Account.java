package ak.accounts;

import ak.transactions.Transaction;

public abstract class Account {
    protected String accountNumber;
    protected String accountHolderName;
    protected double balance;

    public Account(String accountHolderName) {
        this.accountHolderName = accountHolderName;
        this.accountNumber = generateAccountNumber();
        this.balance = 0.0;
    }

    protected String generateAccountNumber() {
        return "ACC-" + (int)(Math.random() * 100000);
    }

    public abstract void withdraw(double amount); // force child classes to define

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

    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder Name: " + accountHolderName);
        System.out.println("Current Balance: $" + String.format("%.2f", balance));
    }

    public void processTransaction(Transaction t) {
        if (t.getToAccount() != null && t.getToAccount().equals(this.accountNumber)) {
            this.deposit(t.getAmount()); // Credit transaction
        } 
        else if (t.getFromAccount() != null && t.getFromAccount().equals(this.accountNumber)) {
            this.withdraw(t.getAmount()); // Debit transaction
        }
    }
    
}
