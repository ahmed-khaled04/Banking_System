package ak;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    AccountTest.class,
    CustomerTest.class,
    TransactionTest.class,
    TransactionManagerTest.class,
    LoanTest.class,
    LoanDetailsTest.class, // Added LoanDetailsTest
    LoanRequestTest.class, // Added LoanRequestTest
    LoanRequestManagerTest.class, // Added LoanRequestManagerTest
    InterestCalculatorTest.class,
    PasswordUtilsTest.class
})

public class AllTestsSuite {
    // EMPTY CLASS. It is used only as a holder for the above annotations.
}