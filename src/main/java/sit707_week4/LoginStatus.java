package sit707_week4;

/**
 * Encapsulates login status and message.
 * @author Ahsan Habib
 */
public class LoginStatus {

	private boolean loginSuccess = false;
	private String errorMsg = "";
	
	public LoginStatus(boolean status, String errorMsg) {
		this.loginSuccess = status;
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
