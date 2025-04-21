package ak.ui;

import ak.App;
import ak.accounts.Account;
import ak.accounts.AccountManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class DashboardController {

    @FXML
    private TableView<Account> accountsTable;

    @FXML
    private TableColumn<Account, String> accountNumberColumn;

    @FXML
    private TableColumn<Account, String> accountTypeColumn;

    @FXML
    private TableColumn<Account, Double> balanceColumn;

    private AccountManager accountManager;
    private String customerId; // Set this when the user logs in

    public DashboardController() {
        this.accountManager = new AccountManager();
    }

    @FXML
    public void initialize() {
        // Set up table columns
        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        accountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
    }

    public void loadCustomerAccounts(String customerId) {
        this.customerId = customerId;
        System.out.println("Loading accounts for customer ID: " + customerId);
        List<Account> accounts = accountManager.getAccountsByCustomerId(customerId);
        ObservableList<Account> accountList = FXCollections.observableArrayList(accounts);
        accountsTable.setItems(accountList);
    }

    @FXML
    private void handleGoToAccount() {
        Account selectedAccount = accountsTable.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            System.out.println("No account selected.");
            return;
        }

        System.out.println("Navigating to account: " + selectedAccount.getAccountNumber());
        try {
            // Pass the selected account to the account page
            App.setRoot("account");
            AccountController controller = App.getController();
            controller.loadAccountDetails(selectedAccount);
        } catch (IOException e) {
            e.printStackTrace();
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
}