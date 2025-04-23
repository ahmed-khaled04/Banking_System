package ak.ui;

import ak.App;
import ak.accounts.AccountManager;
import ak.loans.LoanRequestManager;
import ak.transactions.Transaction;
import ak.transactions.TransactionManager;
import ak.loans.Loan;
import ak.loans.LoanDetails;
import ak.loans.LoanManager;
import ak.loans.LoanRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Optional;

public class ReviewLoanRequestsController {

    @FXML
    private TableView<LoanRequest> loanRequestsTable;

    @FXML
    private TableColumn<LoanRequest, String> requestIdColumn;

    @FXML
    private TableColumn<LoanRequest, String> accountNumberColumn;

    @FXML
    private TableColumn<LoanRequest, Double> loanAmountColumn;

    @FXML
    private TableColumn<LoanRequest, String> loanReasonColumn;

    @FXML
    private TableColumn<LoanRequest, String> statusColumn;

    private LoanManager loanManager;
    private LoanRequestManager loanRequestManager;
    private AccountManager accountManager;
    

    public ReviewLoanRequestsController() {
        this.loanRequestManager = new LoanRequestManager();
        this.loanManager = new LoanManager();
        this.accountManager = new AccountManager();
    }

    @FXML
    public void initialize() {
        requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        loanAmountColumn.setCellValueFactory(new PropertyValueFactory<>("loanAmount"));
        loanReasonColumn.setCellValueFactory(new PropertyValueFactory<>("loanReason"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadLoanRequests();
    }

    private void loadLoanRequests() {
        ObservableList<LoanRequest> loanRequests = FXCollections.observableArrayList(loanRequestManager.getAllLoanRequests());
        loanRequestsTable.setItems(loanRequests);
    }

    @FXML
    private void handleAcceptRequest() {
        LoanRequest selectedRequest = loanRequestsTable.getSelectionModel().getSelectedItem();
        if (selectedRequest == null) {
            showAlert("Error", "No loan request selected.");
            return;
        }

        // Check if the request is already accepted or rejected
        if ("Accepted".equalsIgnoreCase(selectedRequest.getStatus()) || "Rejected".equalsIgnoreCase(selectedRequest.getStatus())) {
            showAlert("Error", "This loan request has already been processed.");
            return;
        }

        // Check if the account is activated
        String accountNumber = selectedRequest.getAccountNumber();
        if (!accountManager.getAccountByNumber(accountNumber).isActivated()) {
            showAlert("Error", "The account associated with this loan request is not activated. Loan requests can only be accepted for activated accounts.");
            return;
        }

        Optional<ButtonType> result = showConfirmation("Accept Loan Request", "Are you sure you want to accept this loan request?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Show a dialog to get loan duration and interest rate
            LoanDetails loanDetails = showLoanDetailsDialog();
            if (loanDetails == null) {
                showAlert("Error", "Loan creation canceled.");
                return;
            }

            boolean success = loanRequestManager.updateLoanRequestStatus(selectedRequest.getRequestId(), "Accepted");
            if (success) {
                // Create a loan and deposit the money into the account
                TransactionManager transactionManager = new TransactionManager(accountManager);
                String custID = accountManager.getAccountByNumber(accountNumber).getCustomerId();
                Loan loanCreated = loanManager.createLoan(custID, accountNumber, selectedRequest.getLoanAmount(), loanDetails.getInterestRate(), loanDetails.getDurationInMonths());
                if (loanCreated != null) {
                    // Deposit the loan amount into the account
                    Transaction transaction = transactionManager.createTransaction(selectedRequest.getLoanAmount(), "Deposit", null, selectedRequest.getAccountNumber());
                    if (transaction != null) {
                        // Update the account balance
                        showAlert("Success", "Loan request accepted and loan created. Amount deposited into the account.");
                    } else {
                        showAlert("Error", "Failed to deposit money into the account.");
                    }
                } else {
                    showAlert("Error", "Failed to create loan or deposit money into the account.");
                }
                loadLoanRequests();
            } else {
                showAlert("Error", "Failed to accept loan request.");
            }
        }
    }

    private LoanDetails showLoanDetailsDialog() {
        // Create a custom dialog for loan details
        Dialog<LoanDetails> dialog = new Dialog<>();
        dialog.setTitle("Loan Details");
        dialog.setHeaderText("Enter the loan duration and interest rate");

        // Set the button types
        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        // Create the input fields
        TextField durationField = new TextField();
        durationField.setPromptText("Duration (Months)");

        TextField interestRateField = new TextField();
        interestRateField.setPromptText("Interest Rate (%)");

        // Add the fields to a grid pane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Duration (Months):"), 0, 0);
        grid.add(durationField, 1, 0);
        grid.add(new Label("Interest Rate (%):"), 0, 1);
        grid.add(interestRateField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a LoanDetails object when the confirm button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                try {
                    int duration = Integer.parseInt(durationField.getText());
                    double interestRate = Double.parseDouble(interestRateField.getText());
                    return new LoanDetails(duration, interestRate);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Invalid input. Please enter valid numbers for duration and interest rate.");
                    return null;
                }
            }
            return null;
        });

        Optional<LoanDetails> result = dialog.showAndWait();
        return result.orElse(null);
    }

    
    @FXML
    private void handleRejectRequest() {
        LoanRequest selectedRequest = loanRequestsTable.getSelectionModel().getSelectedItem();
        if (selectedRequest == null) {
            showAlert("Error", "No loan request selected.");
            return;
        }

        // Check if the request is already accepted or rejected
        if ("Accepted".equalsIgnoreCase(selectedRequest.getStatus()) || "Rejected".equalsIgnoreCase(selectedRequest.getStatus())) {
            showAlert("Error", "This loan request has already been processed.");
            return;
        }

        Optional<ButtonType> result = showConfirmation("Reject Loan Request", "Are you sure you want to reject this loan request?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = loanRequestManager.updateLoanRequestStatus(selectedRequest.getRequestId(), "Rejected");
            if (success) {
                showAlert("Success", "Loan request rejected.");
                loadLoanRequests();
            } else {
                showAlert("Error", "Failed to reject loan request.");
            }
        }
    }

    @FXML
    private void handleBackToAdmin() {
        try {
            App.setRoot("admin");
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

    private Optional<ButtonType> showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }
}