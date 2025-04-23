package ak.accounts;

public class CheckingAccount extends Account {
    private double overdraftLimit = 500;

    public CheckingAccount(String customerId, String accountHolderName, double initialBalance, double overdraftLimit , boolean activated) {
        super(customerId, accountHolderName, initialBalance , activated);
        this.overdraftLimit = overdraftLimit;
    }
    
    public CheckingAccount(String customerId, String accountHolderName, double initialBalance, double overdraftLimit , String accountNumber , boolean activated) {
        super(customerId, accountHolderName, initialBalance , accountNumber, activated);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= balance + overdraftLimit) {
            balance -= amount;
        } else {
            System.out.println("Overdraft limit exceeded in Checking Account.");
        }
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }
}