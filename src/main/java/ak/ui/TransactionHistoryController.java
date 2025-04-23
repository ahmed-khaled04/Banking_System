package ak.ui;

import java.io.IOException;

import ak.App;
import ak.accounts.AccountManager;
import ak.transactions.Transaction;
import ak.transactions.TransactionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class TransactionHistoryController {

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, String> dateColumn;

    @FXML
    private TableColumn<Transaction, String> typeColumn;

    @FXML
    private TableColumn<Transaction, Double> amountColumn;

    private TransactionManager transactionManager;
    private AccountManager accountManager;
    private String currentAccountNumber;

    public TransactionHistoryController() {
        this.accountManager = new AccountManager();
        this.transactionManager = new TransactionManager(accountManager);
    }

    @FXML
    public void initialize() {
        // Initialize table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    public void loadTransactionsForAccount(String accountNumber) {
        currentAccountNumber = accountNumber;
        ObservableList<Transaction> transactions = FXCollections.observableArrayList(
            transactionManager.getTransactionsByAccount(accountNumber)
        );

        if (transactions.isEmpty()) {
            System.out.println("No transactions found for account: " + accountNumber);
        }

        transactionTable.setItems(transactions);
    }

    @FXML
    private void handleBackToAccount() {
        try {
            App.setRoot("account"); // Navigate back to the account view
            AccountController controller = App.getController();
            controller.loadAccountDetails(accountManager.getAccountByNumber(currentAccountNumber));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to navigate back to the account view.");
        }
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}