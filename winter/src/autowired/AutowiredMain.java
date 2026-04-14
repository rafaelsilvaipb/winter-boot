package autowired;

import java.util.HashMap;
import java.util.Map;

import static autowired.AutowiredReflection.injectDependencies;

public class AutowiredMain {

    public static void main(String[] args) throws Exception {
        Map<Class<?>, Object> container = new HashMap<>();

        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService();

        container.put(UserRepository.class, userRepository);
        container.put(UserService.class, userService);

        injectDependencies(container);

        userService.execute();
    }
}