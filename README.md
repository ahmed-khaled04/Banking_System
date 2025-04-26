# Banking GUI Application

This project is a **Banking Application** built using **JavaFX** for the user interface and **PostgreSQL** as the database. It provides functionality for managing accounts, transactions, loans, and loan requests, with a focus on modularity and testability.

---

## **Features**
- **Account Management**:
  - Create, update, and delete savings and checking accounts.
  - View account details and transaction history.

- **Transaction Management**:
  - Perform deposits, withdrawals, and transfers.
  - View transaction history for specific accounts.

- **Loan Management**:
  - Submit loan requests and review them.
  - Approve or reject loan requests.
  - Calculate monthly payments and total repayment for loans.

- **Admin Dashboard**:
  - Manage customers, accounts, and loans.
  - Review loan requests and generate audit reports.

---

## **Technologies Used**
- **JavaFX**: For building the graphical user interface.
- **PostgreSQL**: For database management.
- **JUnit 5**: For unit and integration testing.
- **Maven**: For dependency management and build automation.

---

## **Setup Instructions**

### **1. Prerequisites**
- **Java 17** or higher
- **Maven** (for dependency management)
- **PostgreSQL** (as the database)

### **2. Database Setup**
1. Install PostgreSQL and create a database (e.g., `banking_gui`).
2. Update the database connection details in [`DBconnection.java`](src/main/java/ak/database/DBconnection.java):
   ```java
   private static final String URL = "jdbc:postgresql://localhost:5432/banking_gui";
   private static final String USER = "your_username";
   private static final String PASSWORD = "your_password";
   ```
3. Run the SQL scripts to create the required tables:
   - `accounts`
   - `customers`
   - `transactions`
   - `loans`
   - `loan_requests`
   - `admins`

### **3. Run the Application**
1. Build the project using Maven:
   ```bash
   mvn clean install
   ```
2. Run the application:
   ```bash
   mvn javafx:run
   ```

---

## **Testing**
This project uses **JUnit 5** for unit and integration testing.

### **Run All Tests**
```bash
mvn test
```

### **Run a Specific Test**
```bash
mvn test -Dtest=ClassNameTest
```

### **Generate Test Coverage Report**
1. Add the JaCoCo plugin to `pom.xml`:
   ```xml
   <plugin>
       <groupId>org.jacoco</groupId>
       <artifactId>jacoco-maven-plugin</artifactId>
       <version>0.8.10</version>
       <executions>
           <execution>
               <goals>
                   <goal>prepare-agent</goal>
               </goals>
           </execution>
           <execution>
               <id>report</id>
               <phase>verify</phase>
               <goals>
                   <goal>report</goal>
               </goals>
           </execution>
       </executions>
   </plugin>
   ```
2. Run the tests and generate the report:
   ```bash
   mvn verify
   ```
3. Open the coverage report at `target/site/jacoco/index.html`.

---

## **Key Classes**
- **[`App.java`](src/main/java/ak/App.java)**: Main entry point for the JavaFX application.
- **[`DBconnection.java`](src/main/java/ak/database/DBconnection.java)**: Manages database connections.
- **[`LoanManager.java`](src/main/java/ak/loans/LoanManager.java)**: Handles loan creation, retrieval, and calculations.
- **[`TransactionManager.java`](src/main/java/ak/transactions/TransactionManager.java)**: Manages transactions between accounts.

---

## **Contributing**
1. Fork the repository.
2. Create a new branch for your feature or bug fix:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Add feature-name"
   ```
4. Push to your branch:
   ```bash
   git push origin feature-name
   ```
5. Open a pull request.

---

## **License**
This project is licensed under the Testing Team 11 License. See the `LICENSE` file for details.


---

## **Acknowledgments**
- **JavaFX** for the UI framework.
- **PostgreSQL** for the database.
- **JUnit 5** for testing.
- **Maven** for build automation.