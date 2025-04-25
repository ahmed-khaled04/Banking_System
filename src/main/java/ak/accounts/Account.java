package ak.accounts;

import ak.transactions.Transaction;

public abstract class Account {
    protected String accountNumber;
    protected String accountHolderName;
    protected double balance;
    protected boolean activated;
    protected String customerId; // New field for customer ID
    static int accountID = 1;

    public Account(String customerId, String accountHolderName, double initialBalance, String accountNumber , boolean activated) {
        this.activated = activated;
        this.customerId = customerId;
        this.accountHolderName = accountHolderName;
        if (accountNumber != null && !accountNumber.isEmpty()) {
            this.accountNumber = accountNumber;
        } else {
            this.accountNumber = generateAccountNumber();
        }             
        if(balance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.balance = initialBalance;
    }
    public Account(String customerId, String accountHolderName, double initialBalance , boolean activated) {
        this.activated = activated;
        this.customerId = customerId;
        this.accountHolderName = accountHolderName;            
        this.accountNumber = generateAccountNumber();
        if(balance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.balance = initialBalance;
    }

    protected String generateAccountNumber() {
        return "ACC-" + String.format("%06d", (int) (Math.random() * 1000000));
    }

    public abstract void withdraw(double amount);

    public void deposit(double amount) {
        if(!activated){
            throw new IllegalStateException("Account is Deactivated");
        }
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited $" + amount + " to " + accountNumber);
        }
        else {
            throw new IllegalArgumentException("Deposit amount must be positive.");
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

     // Getter and Setter for activated
     public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
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