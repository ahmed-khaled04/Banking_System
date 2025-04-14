package ak.loans;

import java.util.UUID;

public class Loan {
    private final String loanId;
    private final String customerId;
    private final double loanAmount;
    private final double interestRate; // as a percentage, e.g., 5.5 for 5.5%
    private final int durationInMonths;

    /**
     * Constructor for creating new loans (auto-generates ID)
     */
    public Loan(String customerId, double loanAmount, double interestRate, int durationInMonths) {
        this(generateLoanId(), customerId, loanAmount, interestRate, durationInMonths);
    }

    /**
     * Constructor for existing loans (with known ID)
     */
    public Loan(String loanId, String customerId, double loanAmount, 
               double interestRate, int durationInMonths) {
        if (loanId == null || loanId.isEmpty()) {
            throw new IllegalArgumentException("Loan ID cannot be null or empty");
        }
        if (customerId == null || customerId.isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }
        if (loanAmount <= 0) {
            throw new IllegalArgumentException("Loan amount must be positive");
        }
        if (interestRate <= 0) {
            throw new IllegalArgumentException("Interest rate must be positive");
        }
        if (durationInMonths <= 0) {
            throw new IllegalArgumentException("Loan duration must be positive");
        }

        this.loanId = loanId;
        this.customerId = customerId;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.durationInMonths = durationInMonths;
    }

    private static String generateLoanId() {
        return "LOAN-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public double calculateMonthlyPayment() {
        double monthlyInterest = interestRate / 100 / 12;
        return (loanAmount * monthlyInterest) / (1 - Math.pow(1 + monthlyInterest, -durationInMonths));
    }

    public double calculateTotalRepayment() {
        return calculateMonthlyPayment() * durationInMonths;
    }

    public void printLoanDetails() {
        System.out.println("Loan ID: " + loanId);
        System.out.println("Customer ID: " + customerId);
        System.out.println("Loan Amount: $" + loanAmount);
        System.out.println("Interest Rate: " + interestRate + "%");
        System.out.println("Duration: " + durationInMonths + " months");
        System.out.println("Monthly Payment: $" + String.format("%.2f", calculateMonthlyPayment()));
        System.out.println("Total Repayment: $" + String.format("%.2f", calculateTotalRepayment()));
    }

    // Getters - no setters to maintain immutability
    public String getLoanId() {
        return loanId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public int getDurationInMonths() {
        return durationInMonths;
    }
}