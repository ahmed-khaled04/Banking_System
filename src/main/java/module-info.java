module ak {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens ak to javafx.fxml;
    opens ak.ui to javafx.fxml;
    opens ak.accounts to javafx.base;
    opens ak.customer to javafx.base;
    opens ak.loans to javafx.base;
    exports ak;
}
