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
 * ✔ Exercises financial calculations at nominal, boundary, and precision‑edge
 * inputs
 * ✔ Verifies immutability of internal state
 * ✔ Covers auto‑ID constructor path
 */
class LoanWhiteBoxTest {

    // Constructor branch coverage

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

    // Financial calculations – nominal & edge cases

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

        /* ---------- Boundary tests ---------- */

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
            double amount = 1_000_000_000; // 1 billion
            int months = 360; // 30 years
            Loan loan = new Loan("HUGE", "C1", "A1", amount, 3, months);
            double monthly = loan.calculateMonthlyPayment();

            assertAll(
                    () -> assertTrue(monthly < amount),
                    () -> assertTrue(monthly > amount * (0.03 / 12)));
        }

        /* ---------- Precision check ---------- */

        @Test
        @DisplayName("monthly * duration ≈ total within 1 cent")
        void centsPrecision() {
            Loan loan = new Loan("PREC", "C1", "A1", 9_999.99, 4.375, 77);
            double diff = Math.abs(
                    loan.calculateMonthlyPayment() * 77 - loan.calculateTotalRepayment());
            assertTrue(diff < 0.01, "rounding error > 1 cent");
        }
    }

    /*
     * -------------------------------------------------
     * 3. Immutability check
     * ensures every field stays final --> protects against silent regressions.
     * -------------------------------------------------
     */

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

    /*
     * -------------------------------------------------
     * 4. Auto‑ID constructor path
     * -------------------------------------------------
     */

    @Test
    void autoIdConstructorGeneratesPrefixedId() {
        Loan loan = new Loan("C9", "A9", 1_000, 5, 6);
        assertTrue(loan.getLoanId().startsWith("LOAN-"));
    }
}
