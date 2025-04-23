package ak.ui;

import java.io.IOException;
import java.util.List;

import ak.App;
import ak.customer.Customer;
import ak.customer.CustomerManager;
import ak.accounts.Account;
import ak.accounts.AccountManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminController {

    private CustomerManager customerManager;
    private AccountManager accountManager;

    @FXML
    private TableView<Customer> customersTable;

    @FXML
    private TableColumn<Customer, String> customerIdColumn;

    @FXML
    private TableColumn<Customer, String> customerNameColumn;

    @FXML
    private TableColumn<Customer, String> customerEmailColumn;

    @FXML
    private TableView<Account> accountsTable;

    @FXML
    private TableColumn<Account, String> accountNumberColumn;

    @FXML
    private TableColumn<Account, String> accountTypeColumn;

    @FXML
    private TableColumn<Account, Double> accountBalanceColumn;
    
    @FXML
    private TableColumn<Account, Boolean> accountActivatedColumn;

    @FXML
    private Label errorLabel;

    public AdminController() {
        this.customerManager = new CustomerManager();
        this.accountManager = new AccountManager();
    }

    @FXML
    public void initialize() {
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        accountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        accountBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        accountActivatedColumn.setCellValueFactory(new PropertyValueFactory<>("activated"));
    }

    @FXML
    private void handleViewCustomers() {
        System.out.println("Viewing all customers...");
        List<Customer> customers = customerManager.getAllCustomers();
        ObservableList<Customer> customerList = FXCollections.observableArrayList(customers);
        customersTable.setItems(customerList);
    }

    @FXML
    private void handleViewAccounts() {
        System.out.println("Viewing all accounts...");
        List<Account> accounts = accountManager.getAllAccounts();
        ObservableList<Account> accountList = FXCollections.observableArrayList(accounts);
        accountsTable.setItems(accountList);
    }

    @FXML
    private void handleAddAccount() {
        System.out.println("Adding a new account...");
        try {
            App.setRoot("addAccount"); // Navigate to the Add Account page
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddTransaction() {
        System.out.println("Adding a new transaction...");
        try {
            App.setRoot("addTransaction"); // Navigate to the Add Transaction page
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddLoan() {
        System.out.println("Adding a new loan...");
        try {
            App.setRoot("addLoan"); // Navigate to the Add Loan page
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteAccount() {
        errorLabel.setText(""); // Clear any previous error messages

        Account selectedAccount = accountsTable.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            errorLabel.setText("No account selected. Please select an account to delete.");
            return;
        }

        boolean success = accountManager.deleteAccount(selectedAccount.getAccountNumber());
        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Account deleted successfully.");
            alert.showAndWait();
            handleViewAccounts(); // Refresh the accounts table
        } else {
            errorLabel.setText("Failed to delete the account. Please try again.");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            App.setRoot("signin"); // Redirect to the sign-in page
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleReviewLoanRequests() {
        try {
            App.setRoot("reviewLoanRequests");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleToggleActivation() {
        Account selectedAccount = accountsTable.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            errorLabel.setText("No account selected. Please select an account to toggle activation.");
            return;
        }

        boolean newStatus = !selectedAccount.isActivated();
        boolean success = accountManager.updateAccountActivation(selectedAccount.getAccountNumber(), newStatus);

        if (success) {
            selectedAccount.setActivated(newStatus);
            accountsTable.refresh(); // Refresh the table
            errorLabel.setText("Account activation status updated successfully.");
        } else {
            errorLabel.setText("Failed to update account activation status.");
        }
    }

    
}