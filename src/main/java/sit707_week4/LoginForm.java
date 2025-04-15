package sit707_week4;

/**
 * Authenticates user based on username, password and validation code.
 * Updated to support detailed login status for unit tests.
 * @author Ahsan
 */
public class LoginForm {
    
    private static final String USERNAME = "Girish";            
    private static final String PASSWORD = "Girish_pass";       
    private static final String VALIDATION_CODE = "123456";

    public static LoginStatus login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return new LoginStatus(false, "Empty Username");
        }

        if (password == null || password.trim().isEmpty()) {
            return new LoginStatus(false, "Empty Password");
        }

        if (!username.equals(USERNAME) || !password.equals(PASSWORD)) {
            return new LoginStatus(false, "Credential mismatch");
        }

        return new LoginStatus(true, null);
    }

    public static boolean validateCode(String code) {
        return code != null && code.equals(VALIDATION_CODE);
    }
}
