package ak.accounts;

public class CheckingAccount extends Account {
    private double overdraftLimit = 500;

    public CheckingAccount(String accountHolderName) {
        super(accountHolderName);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= balance + overdraftLimit) {
            balance -= amount;
        } else {
            System.out.println("Overdraft limit exceeded in Checking Account.");
        }
    }
}
