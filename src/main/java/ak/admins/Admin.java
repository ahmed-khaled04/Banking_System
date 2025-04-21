package ak.admins;

import ak.customer.Customer;
import ak.customer.CustomerManager;
import ak.accounts.Account;
import ak.accounts.AccountManager;

import java.util.List;

public class Admin {
    private String adminId;
    private String name;
    private String username;
    private String passwordHash;

    private CustomerManager customerManager;
    private AccountManager accountManager;

    public Admin(String adminId, String name, String username, String passwordHash) {
        this.adminId = adminId;
        this.name = name;
        this.username = username;
        this.passwordHash = passwordHash;
        this.customerManager = new CustomerManager();
        this.accountManager = new AccountManager();
    }

    // Getters
    public String getAdminId() {
        return adminId;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    // Admin functionality
    public List<Customer> getAllCustomers() {
        return customerManager.getAllCustomers();
    }

    public List<Account> getAllAccounts() {
        return accountManager.getAllAccounts();
    }

    public boolean deleteCustomer(String customerId) {
        return customerManager.removeCustomer(customerId);
    }

    public boolean deleteAccount(String accountNumber) {
        return accountManager.deleteAccount(accountNumber);
    }

    public Customer addCustomer(String name, String email, String phoneNumber, String username, String passwordHash) {
        return customerManager.addCustomer(name, email, phoneNumber, username, passwordHash);
    }

    public Account addSavingsAccount(String customerId, String holderName, double initialDeposit, double interestRate){
        return accountManager.createSavingsAccount(customerId, holderName, initialDeposit, interestRate);
    }
    public Account addCheckingAccount(String customerId, String holderName, double initialDeposit, double overdraft_limit){
        return accountManager.createCheckingAccount(customerId, holderName, initialDeposit, overdraft_limit);
    }
}