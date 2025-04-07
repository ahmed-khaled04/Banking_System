import accounts.*;
import customers.*;
import loans.*;
import transactions.*;
import java.util.List;

public class BankingApp {
    public static void main(String[] args) {
        // Initialize all managers
        CustomerManager customerManager = new CustomerManager();
        AccountManager accountManager = new AccountManager();
        LoanManager loanManager = new LoanManager();
        TransactionManager transactionManager = new TransactionManager(accountManager);

        System.out.println("=== BANKING SYSTEM TEST ===");

        // Create customers
        System.out.println("\n--- Creating Customers ---");
        Customer john = customerManager.addCustomer("John Doe", "john@example.com", "555-0101");
        Customer jane = customerManager.addCustomer("Jane Smith", "jane@example.com", "555-0202");

        // Create accounts
        System.out.println("\n--- Creating Accounts ---");
        Account johnSavings = accountManager.createSavingsAccount(john.getName());
        Account johnChecking = accountManager.createCheckingAccount(john.getName());
        Account janeSavings = accountManager.createSavingsAccount(jane.getName());

        // Link accounts to customers
        customerManager.addAccountToCustomer(john.getCustomerId(), johnSavings);
        customerManager.addAccountToCustomer(john.getCustomerId(), johnChecking);
        customerManager.addAccountToCustomer(jane.getCustomerId(), janeSavings);

        // Perform transactions
        System.out.println("\n--- Performing Transactions ---");
        transactionManager.createTransaction(1000.00, "Deposit", null, johnSavings.getAccountNumber());
        transactionManager.createTransaction(500.00, "Deposit", null, johnChecking.getAccountNumber());
        transactionManager.createTransaction(200.00, "Transfer", johnSavings.getAccountNumber(), janeSavings.getAccountNumber());
        transactionManager.createTransaction(100.00, "Withdrawal", johnChecking.getAccountNumber(), null);

        // Create loans
        System.out.println("\n--- Creating Loans ---");
        Loan johnMortgage = loanManager.createLoan(john.getCustomerId(), 200000, 3.5, 360);
        Loan janeCarLoan = loanManager.createLoan(jane.getCustomerId(), 25000, 5.0, 60);

        // Display customer information
        System.out.println("\n--- Customer Information ---");
        customerManager.listCustomerAccounts(john.getCustomerId());
        customerManager.listCustomerAccounts(jane.getCustomerId());

        // Display account information
        System.out.println("\n--- Account Information ---");
        accountManager.displayAllAccounts();

        // Display transactions
        System.out.println("\n--- Transaction History ---");
        transactionManager.printAccountStatement(johnSavings.getAccountNumber());
        transactionManager.printAccountStatement(johnChecking.getAccountNumber());
        transactionManager.printAccountStatement(janeSavings.getAccountNumber());

        // Display loan information
        System.out.println("\n--- Loan Information ---");
        loanManager.printCustomerLoans(john.getCustomerId());
        loanManager.printCustomerLoans(jane.getCustomerId());

        // Test account transfers
        System.out.println("\n--- Testing Account Transfer ---");
        accountManager.transferFunds(johnSavings.getAccountNumber(), janeSavings.getAccountNumber(), 150.00);
        transactionManager.printAccountStatement(johnSavings.getAccountNumber());
        transactionManager.printAccountStatement(janeSavings.getAccountNumber());

        // Test interest application
        System.out.println("\n--- Applying Interest to Savings Accounts ---");
        System.out.println("Before interest:");
        johnSavings.displayAccountInfo();
        janeSavings.displayAccountInfo();
        
        accountManager.applyInterestToSavingsAccounts();
        
        System.out.println("\nAfter interest:");
        johnSavings.displayAccountInfo();
        janeSavings.displayAccountInfo();

        // Test transaction reversal
        System.out.println("\n--- Testing Transaction Reversal ---");
        List<Transaction> johnTransactions = transactionManager.getTransactionsByAccount(johnSavings.getAccountNumber());
        if (!johnTransactions.isEmpty()) {
            Transaction toReverse = johnTransactions.get(0);
            System.out.println("Reversing transaction:");
            toReverse.printTransactionDetails();
            transactionManager.reverseTransaction(toReverse.getTransactionId());
            transactionManager.printAccountStatement(johnSavings.getAccountNumber());
        }

        // Test customer updates
        System.out.println("\n--- Testing Customer Updates ---");
        customerManager.updateCustomer(john.getCustomerId(), "Johnathan Doe", null, "555-0102");
        Customer updatedJohn = customerManager.getCustomerById(john.getCustomerId());
        System.out.println("Updated name: " + updatedJohn.getName());
        System.out.println("Updated phone: " + updatedJohn.getPhoneNumber());

        // Test loan updates
        System.out.println("\n--- Testing Loan Updates ---");
        loanManager.updateLoan(johnMortgage.getLoanId(), null, 3.25, null);
        loanManager.printCustomerLoans(john.getCustomerId());

        // Test customer search
        System.out.println("\n--- Testing Customer Search ---");
        List<Customer> johns = customerManager.findCustomersByName("john");
        System.out.println("Found " + johns.size() + " customers matching 'john'");
        Customer emailSearch = customerManager.findCustomerByEmail("jane@example.com");
        System.out.println("Found by email: " + emailSearch.getName());

        System.out.println("\n=== TESTING COMPLETE ===");
    }
}