package ak.ui;

import ak.customer.CustomerManager;

import java.io.IOException;

import ak.App;
import ak.Authentication.PasswordUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private CustomerManager customerManager;

    public RegisterController() {
        this.customerManager = new CustomerManager();
    }

    @FXML
    private void handleRegister() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
    
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("All fields are required.");
            return;
        }
    
        // Check if the username already exists
        if (customerManager.getCustomerByUsername(username) != null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Registration Failed");
            alert.setHeaderText(null);
            alert.setContentText("An account with this username already exists. Please choose a different username.");
            alert.showAndWait();
            return;
        }
    
        // Hash the password and add the customer
        String hashedPassword = PasswordUtils.hashPassword(password);
        customerManager.addCustomer(name, email, phone, username, hashedPassword);
    
        // Show success message
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Registration Successful");
        alert.setHeaderText(null);
        alert.setContentText("Registration successful! Redirecting to the sign-in page...");
        alert.showAndWait();
    
        // Redirect to the sign-in page
        try {
            App.setRoot("signin");
        } catch (IOException e) {
            errorLabel.setText("Failed to redirect to the sign-in page.");
            e.printStackTrace();
        }
    }
    @FXML
    private void handleBackToSignIn() {
        try {
            App.setRoot("signin"); // Navigate back to the sign-in page
        } catch (IOException e) {
            errorLabel.setText("Failed to load the sign-in page.");
            e.printStackTrace();
        }
}
}