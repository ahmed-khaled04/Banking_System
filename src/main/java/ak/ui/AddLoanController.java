package ak.ui;

import java.io.IOException;

import ak.App;
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
    private TextField accountNumberField; // New field for account number

    @FXML
    private TextField loanAmountField;

    @FXML
    private TextField interestRateField;

    @FXML
    private TextField durationField; // New field for loan duration in months

    private LoanManager loanManager;

    public AddLoanController() {
        this.loanManager = new LoanManager(); // Ensure LoanManager is properly initialized
    }

    @FXML
    private void handleAddLoan() {
        String customerId = customerIdField.getText();
        String accountNumber = accountNumberField.getText(); // Get account number input
        String loanAmountText = loanAmountField.getText();
        String interestRateText = interestRateField.getText();
        String durationText = durationField.getText(); // Get duration input

        if (customerId.isEmpty() || accountNumber.isEmpty() || loanAmountText.isEmpty() || interestRateText.isEmpty() || durationText.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        try {
            double loanAmount = Double.parseDouble(loanAmountText);
            double interestRate = Double.parseDouble(interestRateText);
            int duration = Integer.parseInt(durationText); // Parse duration as an integer

            // Add the loan using LoanManager
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
        customerIdField.clear();
        accountNumberField.clear(); // Clear the account number field
        loanAmountField.clear();
        interestRateField.clear();
        durationField.clear(); // Clear the duration field
    }
}