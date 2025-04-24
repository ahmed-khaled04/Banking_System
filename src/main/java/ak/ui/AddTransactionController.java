package ak.ui;

import java.io.IOException;

import ak.App;
import ak.accounts.*;
import ak.transactions.Transaction;
import ak.transactions.TransactionManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AddTransactionController {

    @FXML
    private ComboBox<String> transactionTypeField;

    @FXML
    private TextField fromAccountField;

    @FXML
    private TextField toAccountField;

    @FXML
    private TextField amountField;

    private TransactionManager transactionManager;
    private AccountManager accountManager; // Assuming you have an AccountManager class

    public AddTransactionController() {
        this.accountManager = new AccountManager(); // Ensure AccountManager is properly initialized
        this.transactionManager = new TransactionManager(accountManager); // Ensure TransactionManager is properly initialized
    }

    @FXML
    public void initialize() {
        // Populate the transaction type dropdown
        transactionTypeField.getItems().addAll("Deposit", "Withdrawal", "Transfer");
        transactionTypeField.setValue("Deposit"); // Set default value
    }

    @FXML
    private void handleAddTransaction() {
        String transactionType = transactionTypeField.getValue();
        String fromAccount = fromAccountField.getText();
        String toAccount = toAccountField.getText();
        String amountText = amountField.getText();
    
        if (transactionType == null || amountText.isEmpty()) {
            showAlert("Error", "Transaction type and amount are required.");
            return;
        }
    
        try {
            double amount = Double.parseDouble(amountText);
    
            if (amount <= 0) {
                showAlert("Error", "Transaction amount must be greater than zero.");
                return;
            }
    
            // Check account activation status and balance
            if (transactionType.equals("Deposit") || transactionType.equals("Transfer")) {
                if (!toAccount.isEmpty()) {
                    Account destinationAccount = accountManager.getAccountByNumber(toAccount);
                    if (destinationAccount == null || !destinationAccount.isActivated()) {
                        showAlert("Error", "The destination account is not activated or does not exist. Transactions can only be made to activated accounts.");
                        return;
                    }
                }
            }
    
            if (transactionType.equals("Withdrawal") || transactionType.equals("Transfer")) {
                if (!fromAccount.isEmpty()) {
                    Account sourceAccount = accountManager.getAccountByNumber(fromAccount);
                    if (sourceAccount == null || !sourceAccount.isActivated()) {
                        showAlert("Error", "The source account is not activated or does not exist. Transactions can only be made from activated accounts.");
                        return;
                    }
    
                    // Check if the source account has sufficient balance
                    if (amount > sourceAccount.getBalance()) {
                        showAlert("Error", "Insufficient balance in the source account. Transaction cannot be completed.");
                        return;
                    }
                }
            }
    
            Transaction success = null; // Initialize success variable
    
            switch (transactionType) {
                case "Deposit":
                    if (toAccount.isEmpty()) {
                        showAlert("Error", "To Account is required for a deposit.");
                        return;
                    }
                    success = transactionManager.createTransaction(amount, "Deposit", null, toAccount);
                    break;
    
                case "Withdrawal":
                    if (fromAccount.isEmpty()) {
                        showAlert("Error", "From Account is required for a withdrawal.");
                        return;
                    }
                    success = transactionManager.createTransaction(amount, "Withdrawal", fromAccount, null);
                    break;
    
                case "Transfer":
                    if (fromAccount.isEmpty() || toAccount.isEmpty()) {
                        showAlert("Error", "Both From Account and To Account are required for a transfer.");
                        return;
                    }
                    success = transactionManager.createTransaction(amount, "Transfer", fromAccount, toAccount);
                    break;
    
                default:
                    showAlert("Error", "Invalid transaction type.");
                    return;
            }
    
            if (success != null) {
                showAlert("Success", "Transaction added successfully!");
                clearFields();
            } else {
                showAlert("Error", "Failed to add transaction. Please check the account details.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid amount. Please enter a valid number.");
        }
    }
    
    @FXML
    private void handleBack() {
        try {
            App.setRoot("admin"); // Navigate back to the admin dashboard
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
        fromAccountField.clear();
        toAccountField.clear();
        amountField.clear();
        transactionTypeField.setValue("Deposit");
    }
}