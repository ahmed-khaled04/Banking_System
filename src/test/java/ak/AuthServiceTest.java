package ak;

import ak.Authentication.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    public void setUp() {
        authService = new AuthService();
    }

    @Test
    public void testSuccessfulLogin() {
        boolean result = authService.login("validUser", "validPassword");
        assertTrue(result, "Login should succeed with valid credentials.");
    }

    @Test
    public void testLoginWithInvalidUsername() {
        boolean result = authService.login("invalidUser", "validPassword");
        assertFalse(result, "Login should fail with an invalid username.");
    }

    @Test
    public void testLoginWithInvalidPassword() {
        boolean result = authService.login("validUser", "invalidPassword");
        assertFalse(result, "Login should fail with an invalid password.");
    }

    @Test
    public void testLoginWithNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            authService.login(null, "somePassword");
        }, "Login should throw IllegalArgumentException when username is null.");
    }

    @Test
    public void testLoginWithNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            authService.login("someUser", null);
        }, "Login should throw IllegalArgumentException when password is null.");
    }

    @Test
    public void testLogout() {
        authService.login("validUser", "validPassword");
        authService.logout("validUser");
        assertFalse(authService.isAuthenticated("validUser"), "User should be logged out.");
    }

    @Test
    public void testIsAuthenticated() {
        authService.login("validUser", "validPassword");
        assertTrue(authService.isAuthenticated("validUser"), "User should be authenticated after login.");
    }
}
