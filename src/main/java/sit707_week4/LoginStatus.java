package sit707_week4;

/**
 * Encapsulates login status and message.
 * Updated to work with tests that expect null on success and exact error messages on failure.
 * @author Ahsan
 */
public class LoginStatus {

    private boolean loginSuccess;
    private String errorMsg;

    public LoginStatus(boolean loginSuccess, String errorMsg) {
        this.loginSuccess = loginSuccess;
        this.errorMsg = errorMsg;
    }

    public boolean isLoginSuccess() {
        return loginSuccess;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String toString() {
        return "LoginStatus [loginSuccess=" + loginSuccess + ", errorMsg=" + errorMsg + "]";
    }
}
