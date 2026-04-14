package autowired;

import java.lang.reflect.Field;
import java.util.Map;

public class AutowiredReflection {
    static void injectDependencies(Map<Class<?>, Object> container) throws Exception {
        for (Object object : container.values()) {
            Class<?> clazz = object.getClass();

            for (Field field : clazz.getDeclaredFields()) {

                if (field.isAnnotationPresent(Autowired.class)) {
                    Object dependency = container.get(field.getType());

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
}
