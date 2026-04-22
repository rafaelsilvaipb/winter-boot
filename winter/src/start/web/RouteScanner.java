package start.web;

import start.annotations.DeleteMapping;
import start.annotations.GetMapping;
import start.annotations.PostMapping;
import start.annotations.PutMapping;
import start.annotations.RequestMapping;
import start.annotations.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteScanner {

    public static List<RouteDefinition> scanRoutes(Map<Class<?>, Object> container) {
        List<RouteDefinition> routes = new ArrayList<>();

        for (Object object : container.values()) {
            Class<?> clazz = object.getClass();

            if (!clazz.isAnnotationPresent(RestController.class)) {
                continue;
            }

            String basePath = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                basePath = clazz.getAnnotation(RequestMapping.class).value();
            }

            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    String fullPath = normalizePath(basePath, method.getAnnotation(GetMapping.class).value());
                    routes.add(new RouteDefinition("GET", fullPath, object, method));
                }

                if (method.isAnnotationPresent(PostMapping.class)) {
                    String fullPath = normalizePath(basePath, method.getAnnotation(PostMapping.class).value());
                    routes.add(new RouteDefinition("POST", fullPath, object, method));
                }

                if (method.isAnnotationPresent(PutMapping.class)) {
                    String fullPath = normalizePath(basePath, method.getAnnotation(PutMapping.class).value());
                    routes.add(new RouteDefinition("PUT", fullPath, object, method));
                }

                if (method.isAnnotationPresent(DeleteMapping.class)) {
                    String fullPath = normalizePath(basePath, method.getAnnotation(DeleteMapping.class).value());
                    routes.add(new RouteDefinition("DELETE", fullPath, object, method));
                }
            }
        }

        return routes;
    }

    private static String normalizePath(String basePath, String methodPath) {
        if (methodPath == null) {
            methodPath = "";
        }

        String full = (basePath + "/" + methodPath).replaceAll("/+", "/");

        if (!full.startsWith("/")) {
            full = "/" + full;
        }

        if (full.length() > 1 && full.endsWith("/")) {
            full = full.substring(0, full.length() - 1);
        }

        return full;
    }
}