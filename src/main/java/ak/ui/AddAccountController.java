package ak.ui;

import java.io.IOException;

import ak.App;
import ak.accounts.AccountManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AddAccountController {

    @FXML
    private TextField customerIdField;

    @FXML
    private TextField accountTypeField;

    @FXML
    private TextField initialDepositField;

    @FXML
    private TextField holderNameField;

    @FXML
    private TextField overdraftLimitField; // For Checking Account (optional)

    @FXML
    private TextField interestRateField; // For Savings Account (optional)

    private AccountManager accountManager;

    public AddAccountController() {
        this.accountManager = new AccountManager();
    }

    @FXML
    private void handleAddAccount() {
        String customerId = customerIdField.getText();
        String accountType = accountTypeField.getText().toLowerCase();
        String initialDepositText = initialDepositField.getText();
        String holderName = holderNameField.getText(); 

        if (customerId.isEmpty() || accountType.isEmpty() || initialDepositText.isEmpty()|| holderName.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        try {
            double initialDeposit = Double.parseDouble(initialDepositText);

            if (accountType.equals("savings")) {
                String interestRateText = interestRateField.getText();
                if (interestRateText.isEmpty()) {
                    showAlert("Error", "Interest rate is required for a Savings Account.");
                    return;
                }
                double interestRate = Double.parseDouble(interestRateText);
                accountManager.createSavingsAccount(customerId, holderName ,initialDeposit, interestRate);
                showAlert("Success", "Savings Account created successfully!");
            } else if (accountType.equals("checking")) {
                String overdraftLimitText = overdraftLimitField.getText();
                if (overdraftLimitText.isEmpty()) {
                    showAlert("Error", "Overdraft limit is required for a Checking Account.");
                    return;
                }
                double overdraftLimit = Double.parseDouble(overdraftLimitText);
                accountManager.createCheckingAccount(customerId, holderName ,initialDeposit, overdraftLimit);
                showAlert("Success", "Checking Account created successfully!");
            } else {
                showAlert("Error", "Invalid account type. Please enter 'Savings' or 'Checking'.");
            }

            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid input. Please enter valid numbers for deposit, interest rate, or overdraft limit.");
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
        accountTypeField.clear();
        initialDepositField.clear();
        overdraftLimitField.clear();
        interestRateField.clear();
        holderNameField.clear();
    }
}