package start.web;

import java.lang.reflect.Field;

public class JsonSerializer {

    public static String toJson(Object obj) throws Exception {

        if (obj == null) {
            return "null";
        }

        // Se for String → já retorna como JSON string
        if (obj instanceof String) {
            return "\"" + obj + "\"";
        }

        // Se for número ou boolean → retorna direto
        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }

        Class<?> clazz = obj.getClass();

        StringBuilder json = new StringBuilder();
        json.append("{");

        Field[] fields = clazz.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {

            Field field = fields[i];
            field.setAccessible(true);

            String name = field.getName();
            Object value = field.get(obj);

            json.append("\"").append(name).append("\":");

            if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else {
                json.append(value);
            }

            if (i < fields.length - 1) {
                json.append(",");
            }
        }

        json.append("}");

        return json.toString();
    }
}