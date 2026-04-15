package start;

import start.impl.UserRepositoryImpl;

import java.util.HashMap;
import java.util.Map;

import static start.StartReflection.injectDependencies;
import static start.StartReflection.isComponent;

public class Main {

    public static void main(String[] args) throws Exception {
        Map<Class<?>, Object> container = new HashMap<>();

        Class<?>[] classes = {
                UserService.class,
                UserRepositoryImpl.class
        };

        for (Class<?> clazz : classes) {
            if (isComponent(clazz)) {
                Object instance = clazz.getDeclaredConstructor().newInstance();

                container.put(clazz, instance);

                for (Class<?> interfaceType : clazz.getInterfaces()) {
                    container.put(interfaceType, instance);
                }
            }
        }

        injectDependencies(container);

        UserService userService = (UserService) container.get(UserService.class);
        userService.execute();
    }
}