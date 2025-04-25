package ak;

import ak.loans.Loan;
import org.junit.jupiter.api.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * White‑box tests for Loan
 * ✔ Covers every constructor branch
 * ✔ Exercises financial calculations at nominal, boundary, and precision‑edge inputs
 * ✔ Verifies immutability of internal state
 * ✔ Covers auto‑ID constructor path
 */
class LoanWhiteBoxTest {

    // 1. Constructor branch coverage

    @Test
    @DisplayName("constructor throws when loanId is null or empty")
    void ctorFailsOnNullOrEmptyId() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Loan(null, "C1", "A1", 1000, 5, 12)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Loan("", "C1", "A1", 1000, 5, 12)));
    }

    @Test
    @DisplayName("constructor throws when customerId is null or empty")
    void ctorFailsOnBadCustomerId() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Loan("L1", null, "A1", 1000, 5, 12)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Loan("L1", "", "A1", 1000, 5, 12)));
    }

    @Test
    @DisplayName("constructor throws when accountNumber is null or empty")
    void ctorFailsOnBadAccount() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Loan("L1", "C1", null, 1000, 5, 12)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Loan("L1", "C1", "", 1000, 5, 12)));
    }

    @Test
    @DisplayName("constructor throws on non‑positive numbers")
    void ctorFailsOnNonPositiveNumbers() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Loan("L1", "C1", "A1", 0, 5, 12)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Loan("L1", "C1", "A1", 1000, 0, 12)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Loan("L1", "C1", "A1", 1000, 5, 0)));
    }

    // 2. Financial calculations – nominal & edge cases

    @Nested
    @DisplayName("Payment calculation scenarios")
    class PaymentFormulaTests {

        @Test
        void nominalCase() {
            Loan loan = new Loan("NOM", "C1", "A1", 10_000, 6, 24);
            assertEquals(443.21, loan.calculateMonthlyPayment(), 0.01);
        }

        @Test
        void singleMonthDuration() {
            Loan loan = new Loan("ONE", "C1", "A1", 2_000, 12, 1);
            double expected = 2_000 * (1 + 0.12 / 12);
            assertEquals(expected, loan.calculateMonthlyPayment(), 0.0001);
        }

        @Test
        @DisplayName("tiny loan amount boundary")
        void tinyLoan() {
            Loan loan = new Loan("TINY", "C1", "A1", 0.01, 5, 12);
            BigDecimal monthly = BigDecimal.valueOf(loan.calculateMonthlyPayment())
                    .setScale(6, RoundingMode.HALF_UP);
            assertTrue(monthly.doubleValue() > 0, "payment should not be zero");
        }

        @Test
        @DisplayName("huge loan & long duration boundary")
        void hugeLoan() {
            double amount = 1_000_000_000; // 1 billion
            int months = 360; // 30 years
            Loan loan = new Loan("HUGE", "C1", "A1", amount, 3, months);
            double monthly = loan.calculateMonthlyPayment();

            assertAll(
                    () -> assertTrue(monthly < amount),
                    () -> assertTrue(monthly > amount * (0.03 / 12)));
        }

        @Test
        @DisplayName("monthly * duration ≈ total within 1 cent")
        void centsPrecision() {
            Loan loan = new Loan("PREC", "C1", "A1", 9_999.99, 4.375, 77);
            double diff = Math.abs(
                    loan.calculateMonthlyPayment() * 77 - loan.calculateTotalRepayment());
            assertTrue(diff < 0.01, "rounding error > 1 cent");
        }

        @Test
        void totalRepaymentIsCorrect() {
            Loan loan = new Loan("TOTAL", "C2", "A2", 5000, 6, 10);
            double expected = loan.calculateMonthlyPayment() * 10;
            assertEquals(expected, loan.calculateTotalRepayment(), 0.01);
        }

        @Test
        void zeroInterestStillCalculatesProperly() {
            Loan loan = new Loan("ZERO", "C7", "A7", 1000, 0.0001, 10);
            assertTrue(loan.calculateMonthlyPayment() > 0);
        }
    }

    // 3. Immutability check
    @Test
    void allFieldsAreFinal() {
        Set<String> expected = Set.of(
                "loanId", "customerId", "accountNumber",
                "loanAmount", "interestRate", "durationInMonths");
        for (Field f : Loan.class.getDeclaredFields()) {
            assertTrue(expected.contains(f.getName()),
                    "unexpected field: " + f.getName());
            assertTrue(java.lang.reflect.Modifier.isFinal(f.getModifiers()),
                    f.getName() + " should be final");
        }
    }

    // 4. Auto-ID constructor path
    @Test
    void autoIdConstructorGeneratesPrefixedId() {
        Loan loan = new Loan("C9", "A9", 1_000, 5, 6);
        assertTrue(loan.getLoanId().startsWith("LOAN-"));
    }

    @Test
    void autoGeneratedLoanIdsAreUnique() {
        Loan l1 = new Loan("C5", "A5", 1000, 5, 6);
        Loan l2 = new Loan("C6", "A6", 1000, 5, 6);
        assertNotEquals(l1.getLoanId(), l2.getLoanId());
    }

    // 5. Getter coverage
    @Test
    void gettersReturnExpectedValues() {
        Loan loan = new Loan("L9", "C9", "A9", 3000, 5, 12);

        assertEquals("L9", loan.getLoanId());
        assertEquals("C9", loan.getCustomerId());
        assertEquals("A9", loan.getAccountNumber());
        assertEquals(3000, loan.getLoanAmount());
        assertEquals(5.0, loan.getInterestRate());
        assertEquals(12, loan.getDurationInMonths());
    }

    // 6. Constructor accepts just-above-zero values
    @Test
    void constructorAcceptsMinimumPositiveInputs() {
        Loan loan = new Loan("MIN", "C4", "A4", 0.0001, 0.0001, 1);
        assertNotNull(loan);
    }

    // 7. Constructor rejects extreme interest rates
    @Test
    void constructorRejectsExtremeInterestRate() {
        assertThrows(IllegalArgumentException.class, () ->
                new Loan("EXT", "C3", "A3", 1000, 1000, 12));
    }
}