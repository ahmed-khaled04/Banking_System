package ak;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import ak.admins.Admin;


@Suite
@SelectClasses({
    AccountTest.class,
    AccountManagerTest.class,
    AccountWhiteBoxTest.class,
    AuthServiceTest.class,
    LoanWhiteBoxTest.class,
    TransactionWhiteBoxTest.class,
    AdminTest.class,
    AdminManagerTest.class,
    CustomerTest.class,
    TransactionTest.class,
    TransactionManagerTest.class,
    LoanTest.class,
    LoanManagerTest.class,
    LoanDetailsTest.class, // Added LoanDetailsTest
    LoanRequestTest.class, // Added LoanRequestTest
    LoanRequestManagerTest.class, // Added LoanRequestManagerTest
    InterestCalculatorTest.class,
    PasswordUtilsTest.class
})

public class AllTestsSuite {
    // EMPTY CLASS. It is used only as a holder for the above annotations.
}