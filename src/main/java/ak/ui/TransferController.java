package ak.ui;

import ak.App;
import ak.accounts.Account;
import ak.accounts.AccountManager;
import ak.transactions.Transaction;
import ak.transactions.TransactionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

public class TransferController {

    @FXML
    private TextField recipientAccountField;

    @FXML
    private TextField transferAmountField;

    private String currentAccountNumber;
    private AccountManager accountManager;
    private TransactionManager transactionManager;

    public TransferController() {
        this.accountManager = new AccountManager();
        this.transactionManager = new TransactionManager(accountManager);
    }

    public void setCurrentAccountNumber(String accountNumber) {
        this.currentAccountNumber = accountNumber;
    }

    @FXML
    private void handleTransferMoney() {
        String recipientAccountNumber = recipientAccountField.getText();
        String transferAmountText = transferAmountField.getText();

        if (recipientAccountNumber.isEmpty() || transferAmountText.isEmpty()) {
            showAlert("Error", "Both recipient account number and transfer amount are required.");
            return;
        }

        try {
            double transferAmount = Double.parseDouble(transferAmountText);

            if (transferAmount <= 0) {
                showAlert("Error", "Transfer amount must be greater than zero.");
                return;
            }

            // Perform the transfer
            Transaction transaction = transactionManager.createTransaction(
                transferAmount,
                "Transfer",
                currentAccountNumber,
                recipientAccountNumber
            );

            if (transaction != null) {
                showAlert("Success", "Money transferred successfully!");
                clearTransferFields();
            } else {
                showAlert("Error", "Failed to transfer money. Please check the recipient account details.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid transfer amount. Please enter a valid number.");
        }
    }

    @FXML
    private void handleBackToAccount() {
        try {
            App.setRoot("account");
            AccountController controller = App.getController();
            controller.loadAccountDetails(accountManager.getAccountByNumber(currentAccountNumber));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearTransferFields() {
        recipientAccountField.clear();
        transferAmountField.clear();
    }
}