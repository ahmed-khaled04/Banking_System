package ak;
	    

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import ak.financial.InterestCalculator;

public class InterestCalculatorTest {

    // SAVINGS ACCOUNT INTEREST CALCULATION
    @Test
    public void testSimpleInterestCalculation() {
        double principal = 1000.0;
        double annualRate = 0.05; // 5%
        int timeInYears = 2;

        double expectedInterest = principal * annualRate * timeInYears;
        double actualInterest = InterestCalculator.calculateSimpleInterest(principal, annualRate, timeInYears);

        assertEquals(expectedInterest, actualInterest, 0.0001);
    }

    @Test
    public void testCompoundInterestCalculation() {
        double principal = 1000.0;
        double annualRate = 0.05; // 5%
        int timesCompoundedPerYear = 4; // Quarterly
        int years = 2;

        double expectedAmount = principal
                * Math.pow(1 + (annualRate / timesCompoundedPerYear), timesCompoundedPerYear * years);
        double actualAmount = InterestCalculator.calculateCompoundInterest(principal, annualRate,
                timesCompoundedPerYear, years);

        assertEquals(expectedAmount, actualAmount, 0.0001);
    }

    // LOAN INTEREST CALCULATION
    @Test
    public void testZeroPrincipal() {
        double principal = 0.0;
        double annualRate = 0.05;
        int timeInYears = 2;

        double expectedInterest = 0.0;
        double actualInterest = InterestCalculator.calculateSimpleInterest(principal, annualRate, timeInYears);

        assertEquals(expectedInterest, actualInterest, 0.0001);
    }

    @Test
    public void testNegativePrincipalThrowsException() {
        double principal = -1000.0;
        double annualRate = 0.05;
        int timeInYears = 2;

        assertThrows(IllegalArgumentException.class, () -> {
            InterestCalculator.calculateSimpleInterest(principal, annualRate, timeInYears);
        });
    }

    @Test
    public void testNegativeRateThrowsException() {
        double principal = 1000.0;
        double annualRate = -0.05;
        int timeInYears = 2;

        assertThrows(IllegalArgumentException.class, () -> {
            InterestCalculator.calculateSimpleInterest(principal, annualRate, timeInYears);
        });
    }

    @Test
    public void testNegativeTimeThrowsException() {
        double principal = 1000.0;
        double annualRate = 0.05;
        int timeInYears = -2;

        assertThrows(IllegalArgumentException.class, () -> {
            InterestCalculator.calculateSimpleInterest(principal, annualRate, timeInYears);
        });
    }
}
