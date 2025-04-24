package ak.ui;

import java.io.IOException;
import java.util.List;

import ak.App;
import ak.accounts.Account;
import ak.accounts.AccountManager;
import ak.customer.CustomerManager;
import ak.loans.Loan;
import ak.loans.LoanManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AddLoanController {

    @FXML
    private TextField customerIdField;

    @FXML
    private TextField accountNumberField; 

    @FXML
    private TextField loanAmountField;

    @FXML
    private TextField interestRateField;

    @FXML
    private TextField durationField; 

    private LoanManager loanManager;

    public AddLoanController() {
        this.loanManager = new LoanManager(); 
    }

    @FXML
    private void handleAddLoan() {
        String customerId = customerIdField.getText();
        String accountNumber = accountNumberField.getText();
        String loanAmountText = loanAmountField.getText();
        String interestRateText = interestRateField.getText();
        String durationText = durationField.getText();

        
    
        if (customerId.isEmpty() || accountNumber.isEmpty() || loanAmountText.isEmpty() || interestRateText.isEmpty() || durationText.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }


    
        try {
            double loanAmount = Double.parseDouble(loanAmountText);
            double interestRate = Double.parseDouble(interestRateText);
            int duration = Integer.parseInt(durationText);
    
            AccountManager accountManager = new AccountManager();
            CustomerManager customerManager = new CustomerManager();
            

            if (customerManager.getCustomerById(customerId) == null) {
                showAlert("Error", "The specified customer ID does not exist.");
                return;
            }


            Account acc = accountManager.getAccountByNumber(accountNumber);
            if (acc == null) {
                showAlert("Error", "The specified account number does not exist.");
                return;
            }
    
            // Check if the account exists and is activated
            if (!accountManager.getAccountByNumber(accountNumber).isActivated()) {
                showAlert("Error", "The account associated with this loan is not activated. Loans can only be added to activated accounts.");
                return;
            }
    
            if (!accountManager.isAccountOwnedByCustomer(accountNumber, customerId)) {
                showAlert("Error", "The specified account does not belong to the provided customer ID.");
                return;
            }
    
            Loan success = loanManager.createLoan(customerId, accountNumber, loanAmount, interestRate, duration);
    
            if (success != null) {
                showAlert("Success", "Loan added successfully!");
                clearFields();
            } else {
                showAlert("Error", "Failed to add loan. Please check the customer and account details.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid loan amount, interest rate, or duration. Please enter valid numbers.");
        }
    }
    
    
    @FXML
    private void handleBack() {
        try {
            App.setRoot("admin"); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        customerIdField.clear();
        accountNumberField.clear(); 
        loanAmountField.clear();
        interestRateField.clear();
        durationField.clear(); 
    }
}