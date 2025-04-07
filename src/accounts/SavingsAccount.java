package accounts;

public class SavingsAccount extends Account {
    private double interestRate = 2.5; // % annual

    public SavingsAccount(String accountHolderName) {
        super(accountHolderName);
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
        balance += (balance * interestRate / 100);
    }
}
