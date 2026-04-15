package start;

import start.annotations.Autowired;
import start.annotations.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

public class StartReflection {

    public static void injectDependencies(Map<Class<?>, Object> container) throws Exception {
        for (Object object : container.values()) {
            Class<?> clazz = object.getClass();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Object dependency = findDependency(container, field.getType());

                    if (dependency == null) {
                        throw new RuntimeException(
                                "Dependência não encontrada para: " + field.getType().getName()
                        );
                    }

                    field.setAccessible(true);
                    field.set(object, dependency);
                }
            }
        }
    }

    private static Object findDependency(Map<Class<?>, Object> container, Class<?> fieldType) {
        Object directDependency = container.get(fieldType);
        if (directDependency != null) {
            return directDependency;
        }

        for (Map.Entry<Class<?>, Object> entry : container.entrySet()) {
            Class<?> registeredClass = entry.getKey();

            if (fieldType.isAssignableFrom(registeredClass)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public static boolean isComponent(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Component.class)) {
            return true;
        }

        for (Annotation annotation : clazz.getAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();

            if (annotationType.isAnnotationPresent(Component.class)) {
                return true;
            }
        }

        return false;
    }
}
