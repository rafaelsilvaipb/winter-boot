package start;

import start.impl.UserRepositoryImpl;
import start.web.MiniHttpServer;
import start.web.RouteDefinition;
import start.web.RouteScanner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static start.StartReflection.injectDependencies;
import static start.StartReflection.isComponent;

public class Main {

    public static void main(String[] args) throws Exception {
        Map<Class<?>, Object> container = new HashMap<>();

        Class<?>[] classes = {
                UserService.class,
                UserRepositoryImpl.class,
                UserController.class
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

        List<RouteDefinition> routes = RouteScanner.scanRoutes(container);

        for (RouteDefinition route : routes) {
            System.out.println(route.getHttpMethod() + " " + route.getPath());
        }

        MiniHttpServer server = new MiniHttpServer(routes);
        server.start(8080);
    }
}