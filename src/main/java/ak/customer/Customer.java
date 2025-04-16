package ak.customer;

import ak.accounts.Account;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer {
    private String customerId;
    private String name;
    private String email;
    private String phoneNumber;
    private String username;
    private String passwordHash;
    private List<Account> accounts;

    public Customer(String name, String email, String phoneNumber) {
        this.customerId = generateCustomerId();
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.accounts = new ArrayList<>();
    }

    public Customer(String name, String email, String phoneNumber , String username, String passwordHash) {
        this.customerId = generateCustomerId();
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.passwordHash = passwordHash;
        this.accounts = new ArrayList<>();
    }

    public Customer(String customerId, String name, String email, String phoneNumber) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.accounts = new ArrayList<>();
    }
    public Customer(String customerId, String name, String email, String phoneNumber , String username, String passwordHash) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.passwordHash = passwordHash;
        this.accounts = new ArrayList<>();
    }

    private String generateCustomerId() {
        return "CUST-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public void addAccount(Account account) {
        accounts.add(account);
        System.out.println("Account added for customer: " + name);
    }

    public void listAccounts() {
        System.out.println("Accounts for customer: " + name);
        for (Account acc : accounts) {
            acc.displayAccountInfo();
            System.out.println("------------");
        }
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getUsername() {
        return username;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
}
