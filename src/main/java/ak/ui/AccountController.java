package ak.ui;

import ak.App;
import ak.accounts.Account;
import javafx.fxml.FXML;
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

    public void loadAccountDetails(Account account) {

        this.currentAccount = account; // Set the current account
        this.customerId = account.getCustomerId(); // Set the customer ID
        accountNumberLabel.setText("Account Number: " + account.getAccountNumber());
        accountTypeLabel.setText("Account Type: " + account.getAccountType());
        balanceLabel.setText("Balance: $" + account.getBalance());
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