package ak.accounts;

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
        double interest = balance * interestRate / 100;
        balance += interest;
        System.out.printf("Added $%.2f interest to %s%n", interest, accountNumber);
    }
}
