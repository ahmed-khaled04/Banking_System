package ak.accounts;

public class SavingsAccount extends Account {
    private double interestRate = 2.5;

    public SavingsAccount(String customerId, String accountHolderName, double initialBalance, double interestRate , String accountNumber) {
        super(customerId, accountHolderName, initialBalance , accountNumber);
        this.interestRate = interestRate;
    }

    public SavingsAccount(String customerId, String accountHolderName, double initialBalance, double interestRate) {
        super(customerId, accountHolderName, initialBalance);
        this.interestRate = interestRate;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
        } else {
            System.out.println("Insufficient funds in Savings Account.");
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