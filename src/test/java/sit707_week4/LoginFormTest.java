package sit707_week4;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests functions in LoginForm.
 * Student Name: Girish Sirpali
 * Student ID: 222121587
 */
public class LoginFormTest {

    @Test
    public void testStudentIdentity() {
        String studentId = "222121587";
        Assert.assertEquals("222121587", studentId);
    }

    @Test
    public void testStudentName() {
        String studentName = "Girish Sirpali";
        Assert.assertEquals("Girish Sirpali", studentName);
    }

    // TC1: Empty username and password
    @Test
    public void testFailEmptyUsernameAndEmptyPassword() {
        LoginStatus status = LoginForm.login("", "");
        Assert.assertFalse(status.isLoginSuccess());
        Assert.assertEquals("Empty Username", status.getErrorMsg());
    }

    // TC2: Empty username and wrong password
    @Test
    public void testFailEmptyUsernameAndWrongPassword() {
        LoginStatus status = LoginForm.login("", "wrong");
        Assert.assertFalse(status.isLoginSuccess());
        Assert.assertEquals("Empty Username", status.getErrorMsg());
    }

    // TC3: Empty username and correct password
    @Test
    public void testFailEmptyUsernameAndCorrectPassword() {
        LoginStatus status = LoginForm.login("", "Girish_pass");
        Assert.assertFalse(status.isLoginSuccess());
        Assert.assertEquals("Empty Username", status.getErrorMsg());
    }

    // TC4: Wrong username and empty password
    @Test
    public void testFailWrongUsernameAndEmptyPassword() {
        LoginStatus status = LoginForm.login("wrong", "");
        Assert.assertFalse(status.isLoginSuccess());
        Assert.assertEquals("Empty Password", status.getErrorMsg());
    }

    // TC5: Wrong username and wrong password
    @Test
    public void testFailWrongUsernameAndWrongPassword() {
        LoginStatus status = LoginForm.login("wrong", "wrong");
        Assert.assertFalse(status.isLoginSuccess());
        Assert.assertEquals("Credential mismatch", status.getErrorMsg());
    }

    // TC6: Wrong username and correct password
    @Test
    public void testFailWrongUsernameAndCorrectPassword() {
        LoginStatus status = LoginForm.login("wrong", "Girish_pass");
        Assert.assertFalse(status.isLoginSuccess());
        Assert.assertEquals("Credential mismatch", status.getErrorMsg());
    }

    // TC7: Correct username and empty password
    @Test
    public void testFailCorrectUsernameAndEmptyPassword() {
        LoginStatus status = LoginForm.login("Girish", "");
        Assert.assertFalse(status.isLoginSuccess());
        Assert.assertEquals("Empty Password", status.getErrorMsg());
    }

    // TC8: Correct username and wrong password
    @Test
    public void testFailCorrectUsernameAndWrongPassword() {
        LoginStatus status = LoginForm.login("Girish", "wrong");
        Assert.assertFalse(status.isLoginSuccess());
        Assert.assertEquals("Credential mismatch", status.getErrorMsg());
    }

    // TC9: Correct username and password, empty validation code
    @Test
    public void testCorrectLoginButEmptyValidationCode() {
        LoginStatus status = LoginForm.login("Girish", "Girish_pass");
        Assert.assertTrue(status.isLoginSuccess());
        Assert.assertFalse(LoginForm.validateCode(""));
    }

    // TC10: Correct username and password, wrong validation code
    @Test
    public void testCorrectLoginButWrongValidationCode() {
        LoginStatus status = LoginForm.login("Girish", "Girish_pass");
        Assert.assertTrue(status.isLoginSuccess());
        Assert.assertFalse(LoginForm.validateCode("wrong_code"));
    }

    // TC11: Correct username and password, correct validation code
    @Test
    public void testCorrectLoginAndCorrectValidationCode() {
        LoginStatus status = LoginForm.login("Girish", "Girish_pass");
        Assert.assertTrue(status.isLoginSuccess());
        Assert.assertTrue(LoginForm.validateCode("123456"));
    }
}
