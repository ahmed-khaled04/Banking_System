package ak.accounts;

public class SavingsAccount extends Account {
    private double interestRate = 2.5;

    public SavingsAccount(String customerId, String accountHolderName, double initialBalance, double interestRate , String accountNumber , boolean activated) {
        super(customerId, accountHolderName, initialBalance , accountNumber , activated);
        if(interestRate < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative.");
        }
        if(initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.interestRate = interestRate;
    }

    public SavingsAccount(String customerId, String accountHolderName, double initialBalance, double interestRate , boolean activated) {
        super(customerId, accountHolderName, initialBalance , activated);
        if(interestRate < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative.");
        }
        if(initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.interestRate = interestRate;
    }

    @Override
    public void withdraw(double amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount <= balance) {
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient funds for withdrawal.");
        }
    }

    public void addInterest() {
        double interest = getBalance() * (interestRate / 100);
        deposit(interest);
    }

    public double getInterestRate() {
        return interestRate;
    }
}