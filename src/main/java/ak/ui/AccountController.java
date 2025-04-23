package ak.ui;

import ak.App;
import ak.accounts.Account;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class AccountController {
    private Account currentAccount; 
    private String customerId; 

    @FXML
    private Label accountNumberLabel;

    @FXML
    private Label accountTypeLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label activationStatusLabel;

    
    @FXML
    private Button transferButton;


    @FXML
    private Button loanButton;

    @FXML
    private Button transactionHistoryButton;



    public void loadAccountDetails(Account account) {

        this.currentAccount = account; // Set the current account
        this.customerId = account.getCustomerId(); // Set the customer ID
        accountNumberLabel.setText("Account Number: " + account.getAccountNumber());
        accountTypeLabel.setText("Account Type: " + account.getAccountType());
        balanceLabel.setText("Balance: $" + account.getBalance());


        // Update activation status
        if (account.isActivated()) {
            activationStatusLabel.setText("Status: Activated");
            activationStatusLabel.setStyle("-fx-text-fill: green;");
            transferButton.setDisable(false);
            loanButton.setDisable(false);
        } else {
            activationStatusLabel.setText("Status: Not Activated");
            activationStatusLabel.setStyle("-fx-text-fill: red;");
            transferButton.setDisable(true);
            loanButton.setDisable(true);
        }


        
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleViewTransactionHistory() {
        try {
            App.setRoot("transactionHistory"); // Navigate to the transaction history page
            TransactionHistoryController controller = App.getController();
            controller.loadTransactionsForAccount(currentAccount.getAccountNumber());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load transaction history.");
        }
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            App.setRoot("dashboard"); // Navigate back to the dashboard
            // Pass the customer ID to the dashboard controller
            DashboardController controller = App.getController();
            controller.loadCustomerAccounts(customerId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGoToTransfer() {
        try {
            App.setRoot("transfer");
            TransferController controller = App.getController();
            controller.setCurrentAccountNumber(currentAccount.getAccountNumber());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleGoToLoanRequest() {
        try {
            App.setRoot("loanRequest");
            LoanRequestController controller = App.getController();
            controller.setAccountNumber(currentAccount.getAccountNumber());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}