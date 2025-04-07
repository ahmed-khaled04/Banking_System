package loans;

import java.util.UUID;

public class Loan {
    private String loanId;
    private String customerId;
    private double loanAmount;
    private double interestRate; // as a percentage, e.g., 5.5 for 5.5%
    private int durationInMonths;

    public Loan(String customerId, double loanAmount, double interestRate, int durationInMonths) {
        this.loanId = generateLoanId();
        this.customerId = customerId;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.durationInMonths = durationInMonths;
    }

    private String generateLoanId() {
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

    // Getters
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
