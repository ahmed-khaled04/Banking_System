package ak.ui;

import ak.App;
import ak.accounts.AccountManager;
import ak.loans.LoanRequestManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoanRequestController {

    @FXML
    private TextField loanAmountField;

    @FXML
    private TextField loanReasonField;

    private String accountNumber;
    private AccountManager accountManager;

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @FXML
    private void handleSubmitLoanRequest() {
        String loanAmountText = loanAmountField.getText();
        String loanReason = loanReasonField.getText();

        if (loanAmountText.isEmpty() || loanReason.isEmpty()) {
            showAlert("Error", "Both loan amount and reason are required.");
            return;
        }

        try {
            double loanAmount = Double.parseDouble(loanAmountText);

            if (loanAmount <= 0) {
                showAlert("Error", "Loan amount must be greater than zero.");
                return;
            }

            // Submit the loan request
            LoanRequestManager loanRequestManager = new LoanRequestManager();
            boolean success = loanRequestManager.submitLoanRequest(accountNumber, loanAmount, loanReason);

            if (success) {
                showAlert("Success", "Loan request submitted successfully!");
                clearFields();
            } else {
                showAlert("Error", "Failed to submit loan request. Please try again.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid loan amount. Please enter a valid number.");
        }
    }

    @FXML
    private void handleBackToAccount() {
        try {
            App.setRoot("account");
            accountManager = new AccountManager();
            AccountController controller = App.getController();
            controller.loadAccountDetails(accountManager.getAccountByNumber(accountNumber));
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

    private void clearFields() {
        loanAmountField.clear();
        loanReasonField.clear();
    }
}