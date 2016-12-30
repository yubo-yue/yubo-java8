package yubo.v1.io;

import java.io.Console;
import java.util.Arrays;

public class Password {
    public static void main(String[] args) {
        final Console console = System.console();

        if (null == console) {
            return;
        }

        String login = console.readLine("Enter your login:");
        char[] oldPassword = console.readPassword("Enter your old password:");

        if (verify(login, oldPassword)) {
            boolean noMatchs;

            do {
                char[] newPassword1 = console.readPassword("Enter your new password: ");
                char[] newPassword2 = console.readPassword("Enter new password again: ");
                noMatchs = ! Arrays.equals(newPassword1, newPassword2);
                if (noMatchs) {
                    console.format("Passwords don't match. Try again.%n");
                } else {
                    change(login, newPassword1);
                    console.format("Password for %s changed.%n", login);
                }

                Arrays.fill(newPassword1, ' ');
                Arrays.fill(newPassword2, ' ');
            } while (noMatchs);
        }
    }

    // Dummy change method.
    static boolean verify(String login, char[] password) {
        // This method always returns
        // true in this example.
        // Modify this method to verify
        // password according to your rules.
        return true;
    }

    // Dummy change method.
    static void change(String login, char[] password) {
        // Modify this method to change
        // password according to your rules.
    }
}
