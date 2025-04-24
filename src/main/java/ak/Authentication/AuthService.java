package ak.Authentication;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private Map<String, String> users = new HashMap<>();
    private Map<String, Boolean> loggedInUsers = new HashMap<>();

    public AuthService() {
        // Add some users for testing
        users.put("validUser", PasswordUtils.hashPassword("validPassword"));
    }

    public boolean login(String username, String password) {
        if (username == null || password == null)
            throw new IllegalArgumentException("Username or password cannot be null");

        String hashed = users.get(username);
        if (hashed != null && PasswordUtils.verifyPassword(password, hashed)) {
            loggedInUsers.put(username, true);
            return true;
        }
        return false;
    }

    public void logout(String username) {
        loggedInUsers.remove(username);
    }

    public boolean isAuthenticated(String username) {
        return loggedInUsers.getOrDefault(username, false);
    }
}
