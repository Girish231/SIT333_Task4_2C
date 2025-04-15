package sit707_week4;

public class Main {
    public static void main(String[] args) {
        LoginStatus status = LoginForm.login(null, null);
        System.out.println("[Empty username, empty password] >> " + status);

        status = LoginForm.login(null, "xyz");
        System.out.println("[Empty username, wrong password] >> " + status);

        status = LoginForm.login("abc", "xyz");
        System.out.println("[Wrong username, wrong password] >> " + status);

        status = LoginForm.login("Girish", null);
        System.out.println("[Correct username, empty password] >> " + status);

        status = LoginForm.login("Girish", "wrong");
        System.out.println("[Correct username, wrong password] >> " + status);

        status = LoginForm.login(null, "Girish_pass");
        System.out.println("[Empty username, Correct password] >> " + status);

        status = LoginForm.login("abc", "Girish_pass");
        System.out.println("[Wrong username, Correct password] >> " + status);

        status = LoginForm.login("Girish", "Girish_pass");
        System.out.println("[Correct username, Correct password] >> " + status);

        if (status.isLoginSuccess()) {
            System.out.println("\tEmpty code >> " + LoginForm.validateCode(null));
            System.out.println("\tWrong code >> " + LoginForm.validateCode("abcd"));
            System.out.println("\tCorrect code >> " + LoginForm.validateCode("123456"));
        }
    }
}
