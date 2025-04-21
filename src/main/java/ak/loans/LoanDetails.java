package ak.loans;

public class LoanDetails {
    private final int durationInMonths;
    private final double interestRate;

    public LoanDetails(int durationInMonths, double interestRate) {
        this.durationInMonths = durationInMonths;
        this.interestRate = interestRate;
    }

    public int getDurationInMonths() {
        return durationInMonths;
    }

    public double getInterestRate() {
        return interestRate;
    }
}