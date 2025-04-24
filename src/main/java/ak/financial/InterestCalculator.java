package ak.financial;

public class InterestCalculator {

    public static double calculateSimpleInterest(double principal, double annualRate, int timeInYears) {
        if (principal < 0) {
            throw new IllegalArgumentException("Principal cannot be negative");
        }
        if (annualRate < 0) {
            throw new IllegalArgumentException("Annual rate cannot be negative");
        }
        if (timeInYears < 0) {
            throw new IllegalArgumentException("Time in years cannot be negative");
        }
        return principal * annualRate * timeInYears;
    }

    public static double calculateCompoundInterest(double principal, double annualRate, int timesCompoundedPerYear, int years) {
        if (principal < 0) {
            throw new IllegalArgumentException("Principal cannot be negative");
        }
        if (annualRate < 0) {
            throw new IllegalArgumentException("Annual rate cannot be negative");
        }
        if (timesCompoundedPerYear <= 0) {
            throw new IllegalArgumentException("Times compounded per year must be positive");
        }
        if (years < 0) {
            throw new IllegalArgumentException("Years cannot be negative");
        }
        return principal * Math.pow(1 + (annualRate / timesCompoundedPerYear), timesCompoundedPerYear * years);
    }
}