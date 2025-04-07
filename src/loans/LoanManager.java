package loans;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LoanManager {
    private List<Loan> loans;

    public LoanManager() {
        this.loans = new ArrayList<>();
    }

    /**
     * Creates and adds a new loan to the system
     * @param customerId ID of the customer taking the loan
     * @param loanAmount Principal loan amount
     * @param interestRate Annual interest rate (as percentage)
     * @param durationInMonths Loan term in months
     * @return The newly created Loan object
     */
    public Loan createLoan(String customerId, double loanAmount, double interestRate, int durationInMonths) {
        if (loanAmount <= 0) {
            throw new IllegalArgumentException("Loan amount must be positive");
        }
        if (interestRate <= 0) {
            throw new IllegalArgumentException("Interest rate must be positive");
        }
        if (durationInMonths <= 0) {
            throw new IllegalArgumentException("Loan duration must be positive");
        }

        Loan newLoan = new Loan(customerId, loanAmount, interestRate, durationInMonths);
        loans.add(newLoan);
        System.out.println("Loan created successfully with ID: " + newLoan.getLoanId());
        return newLoan;
    }

    /**
     * Retrieves a loan by its ID
     * @param loanId The ID of the loan to find
     * @return The Loan object if found, null otherwise
     */
    public Loan getLoanById(String loanId) {
        return loans.stream()
                .filter(loan -> loan.getLoanId().equals(loanId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all loans for a specific customer
     * @param customerId ID of the customer
     * @return List of the customer's loans
     */
    public List<Loan> getLoansByCustomer(String customerId) {
        return loans.stream()
                .filter(loan -> loan.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all loans in the system
     * @return List of all loans
     */
    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans); // Return a copy to prevent external modification
    }

    /**
     * Updates loan details (creates a new loan if changes affect financial calculations)
     * @param loanId ID of the loan to update
     * @param newAmount New loan amount (null to keep existing)
     * @param newRate New interest rate (null to keep existing)
     * @param newDuration New duration in months (null to keep existing)
     * @return The updated Loan object, or null if loan wasn't found
     */
    public Loan updateLoan(String loanId, Double newAmount, Double newRate, Integer newDuration) {
        Loan existingLoan = getLoanById(loanId);
        if (existingLoan == null) {
            System.out.println("Loan not found with ID: " + loanId);
            return null;
        }

        // If any financial parameter changes, create a new loan
        if (newAmount != null || newRate != null || newDuration != null) {
            double amount = newAmount != null ? newAmount : existingLoan.getLoanAmount();
            double rate = newRate != null ? newRate : existingLoan.getInterestRate();
            int duration = newDuration != null ? newDuration : existingLoan.getDurationInMonths();

            Loan updatedLoan = createLoan(
                existingLoan.getCustomerId(),
                amount,
                rate,
                duration
            );

            // Remove the old loan
            loans.remove(existingLoan);
            return updatedLoan;
        }

        // If no financial parameters changed, return the existing loan
        return existingLoan;
    }

    /**
     * Removes a loan from the system
     * @param loanId ID of the loan to remove
     * @return true if removal was successful, false otherwise
     */
    public boolean removeLoan(String loanId) {
        Loan loan = getLoanById(loanId);
        if (loan == null) {
            System.out.println("Loan not found with ID: " + loanId);
            return false;
        }

        loans.remove(loan);
        System.out.println("Loan removed successfully: " + loanId);
        return true;
    }

    /**
     * Calculates the total monthly payment for a customer's loans
     * @param customerId ID of the customer
     * @return Total monthly payment amount
     */
    public double calculateCustomerMonthlyPayment(String customerId) {
        return getLoansByCustomer(customerId).stream()
                .mapToDouble(Loan::calculateMonthlyPayment)
                .sum();
    }

    /**
     * Calculates the total remaining repayment for a customer's loans
     * @param customerId ID of the customer
     * @return Total remaining repayment amount
     */
    public double calculateCustomerTotalRemaining(String customerId) {
        return getLoansByCustomer(customerId).stream()
                .mapToDouble(Loan::calculateTotalRepayment)
                .sum();
    }

    /**
     * Prints details of all loans for a specific customer
     * @param customerId ID of the customer
     */
    public void printCustomerLoans(String customerId) {
        List<Loan> customerLoans = getLoansByCustomer(customerId);
        if (customerLoans.isEmpty()) {
            System.out.println("No loans found for customer ID: " + customerId);
            return;
        }

        System.out.println("Loans for customer ID: " + customerId);
        System.out.println("----------------------------");
        for (Loan loan : customerLoans) {
            loan.printLoanDetails();
            System.out.println("----------------------------");
        }
        System.out.printf("Total Monthly Payments: $%.2f%n", calculateCustomerMonthlyPayment(customerId));
        System.out.printf("Total Remaining Repayment: $%.2f%n", calculateCustomerTotalRemaining(customerId));
    }
}