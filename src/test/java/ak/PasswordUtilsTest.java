package ak;

import ak.Authentication.PasswordUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilsTest {

    @Test
    public void testHashPassword_NotNull() {
        String password = "mySecret123";
        String hash = PasswordUtils.hashPassword(password);
        assertNotNull(hash, "Hashed password should not be null.");
    }

    @Test
    public void testHashPassword_Consistency() {
        String password = "repeatablePassword";
        String hash1 = PasswordUtils.hashPassword(password);
        String hash2 = PasswordUtils.hashPassword(password);
        assertEquals(hash1, hash2, "Hashing the same password should return the same hash.");
    }

    @Test
    public void testVerifyPassword_Correct() {
        String password = "securePassword";
        String hash = PasswordUtils.hashPassword(password);
        assertTrue(PasswordUtils.verifyPassword(password, hash), "Password verification should succeed with correct password.");
    }

    @Test
    public void testVerifyPassword_Incorrect() {
        String originalPassword = "securePassword";
        String hash = PasswordUtils.hashPassword(originalPassword);
        assertFalse(PasswordUtils.verifyPassword("wrongPassword", hash), "Password verification should fail with incorrect password.");
    }

    @Test
    public void testDifferentPasswordsHaveDifferentHashes() {
        String hash1 = PasswordUtils.hashPassword("password1");
        String hash2 = PasswordUtils.hashPassword("password2");
        assertNotEquals(hash1, hash2, "Different passwords should produce different hashes.");
    }
}
