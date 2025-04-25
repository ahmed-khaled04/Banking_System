package ak.accounts;

public class CheckingAccount extends Account {
    private double overdraftLimit = 500;

    public CheckingAccount(String customerId, String accountHolderName, double initialBalance, double overdraftLimit , boolean activated) {
        super(customerId, accountHolderName, initialBalance , activated);
        if(overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        }
        if(initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.overdraftLimit = overdraftLimit;
    }
    
    public CheckingAccount(String customerId, String accountHolderName, double initialBalance, double overdraftLimit , String accountNumber , boolean activated) {
        super(customerId, accountHolderName, initialBalance , accountNumber, activated);
        if(overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        }
        if(initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount <= balance + overdraftLimit) {
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Withdrawal exceeds overdraft limit.");
        }
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }
}