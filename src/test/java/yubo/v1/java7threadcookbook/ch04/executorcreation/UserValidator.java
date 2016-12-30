package yubo.v1.java7threadcookbook.ch04.executorcreation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class UserValidator {
    private String name;

    public UserValidator(final String name) {
        this.name = name;
    }

    public boolean validate(final String name, final String password) {
        final Random random = new Random();

        try {
            long duration = (long) (Math.random() * 10);
            System.out.printf("Validator %s: Validating a user during %d seconds%n", this.name, duration);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            return false;
        }

        return random.nextBoolean();
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) {
        String username = "test";
        String password = "test";

        UserValidator ldapValidator = new UserValidator("LDAP");
        UserValidator dbValidator = new UserValidator("DB");

        TaskValidator ldapTask = new TaskValidator(ldapValidator, username, password);
        TaskValidator dbTask = new TaskValidator(dbValidator, username, password);

        List<TaskValidator> tasks = new ArrayList<>();
        tasks.add(ldapTask);
        tasks.add(dbTask);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        try {
            String result = executorService.invokeAny(tasks);
            System.out.printf("Result : %s%n", result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.printf("end of executor service%n");
        executorService.shutdown();
    }
}

class TaskValidator implements Callable<String> {
    private UserValidator userValidator;
    private String name;
    private String password;

    public TaskValidator(final UserValidator userValidator, final String name, final String password) {
        this.userValidator = userValidator;
        this.name = name;
        this.password = password;
    }

    @Override
    public String call() throws Exception {
        if (!userValidator.validate(name, password)) {
            System.out.printf("%s: The user has not been found%n", userValidator.getName());
            throw new Exception("Error validating user");
        }
        System.out.printf("%s: The user has been found\n", userValidator.getName());
        return userValidator.getName();
    }
}
