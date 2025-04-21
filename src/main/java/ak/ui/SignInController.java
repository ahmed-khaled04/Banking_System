package ak.ui;

import java.io.IOException;

import ak.App;
import ak.admins.Admin;
import ak.admins.AdminManager;
import ak.customer.Customer;
import ak.customer.CustomerManager;
import ak.Authentication.PasswordUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;

public class SignInController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private CustomerManager customerManager;
    private AdminManager adminManager;

    public SignInController() {
        // Initialize the managers
        this.customerManager = new CustomerManager();
        this.adminManager = new AdminManager();
    }

    @FXML
    private void handleRegister() {
        try {
            App.setRoot("register"); // Navigate to the registration page
        } catch (IOException e) {
            errorLabel.setText("Failed to load the registration page.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSignInAsCustomer() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            errorLabel.setText("Username and password cannot be empty.");
            return;
        }

        // Authenticate the customer
        Customer customer = customerManager.authenticateCustomer(username, PasswordUtils.hashPassword(password));
        if (customer != null) {
            errorLabel.setText("");
            System.out.println("Welcome, " + customer.getName() + "!");
            try {
                // Redirect to the dashboard view
                App.setRoot("dashboard");

                // Pass the customer ID to the dashboard controller
                DashboardController controller = App.getController();
                controller.loadCustomerAccounts(customer.getCustomerId());
            } catch (Exception e) {
                errorLabel.setText("Failed to load the dashboard.");
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    private void handleSignInAsAdmin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            errorLabel.setText("Username and password cannot be empty.");
            return;
        }

        // Authenticate the admin
        Admin admin = adminManager.authenticateAdmin(username, PasswordUtils.hashPassword(password));
        if (admin != null) {
            errorLabel.setText("");
            System.out.println("Welcome, Admin " + admin.getName() + "!");
            try {
                // Redirect to the admin page
                App.setRoot("admin");
            } catch (Exception e) {
                errorLabel.setText("Failed to load the admin page.");
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Invalid admin username or password.");
        }
    }
}