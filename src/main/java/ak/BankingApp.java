package ak;


import ak.accounts.Account;
import ak.accounts.AccountManager;
import ak.accounts.CheckingAccount;
import ak.accounts.SavingsAccount;
import ak.customer.Customer;
import ak.customer.CustomerManager;
import ak.loans.Loan;
import ak.loans.LoanManager;
import ak.transactions.Transaction;
import ak.transactions.TransactionManager;
import ak.Authentication.*;
import ak.database.DBconnection;

import java.util.List;

public class BankingApp {
    public static void main(String[] args) {
        //Clear Database
        DBconnection.clearDatabase();


        // Initialize managers
        CustomerManager customerManager = new CustomerManager();
        AccountManager accountManager = new AccountManager();
        TransactionManager transactionManager = new TransactionManager(accountManager);
        LoanManager loanManager = new LoanManager();

        try {
            // Create customers
            System.out.println("Creating customers...");
            Customer customer1 = customerManager.addCustomer("Alice", "alice@example.com", "1234567890" , "Inferno" , PasswordUtils.hashPassword("1234"));
            Customer customer2 = customerManager.addCustomer("Bob", "bob@example.com", "0987654321" , "Bob" , PasswordUtils.hashPassword("1565"));

            System.out.println((customerManager.authenticateCustomer("Inferno", PasswordUtils.hashPassword("1234"))).getEmail());
            
        } finally {
            // Close resources
            transactionManager.close();
            accountManager.close();
            loanManager.close();
        }
    }
    
}