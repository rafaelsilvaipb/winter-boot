package start.web;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class JsonUtils {

    public static <T> T fromJson(String json, Class<T> clazz) throws Exception {
        if (json == null || json.isBlank()) {
            return null;
        }

        Map<String, String> values = parseJsonToMap(json);

        T instance = clazz.getDeclaredConstructor().newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            String rawValue = values.get(field.getName());
            if (rawValue == null) {
                continue;
            }

            Object convertedValue = convertValue(rawValue, field.getType());
            field.set(instance, convertedValue);
        }

        return instance;
    }

    private static Map<String, String> parseJsonToMap(String json) {
        Map<String, String> map = new HashMap<>();

        String content = json.trim();

        if (content.startsWith("{")) {
            content = content.substring(1);
        }

        if (content.endsWith("}")) {
            content = content.substring(0, content.length() - 1);
        }

        if (content.isBlank()) {
            return map;
        }

        String[] pairs = splitJsonPairs(content);

        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);

            if (keyValue.length < 2) {
                continue;
            }

            String key = removeQuotes(keyValue[0].trim());
            String value = keyValue[1].trim();

            map.put(key, removeQuotes(value));
        }

        return map;
    }

    private static String[] splitJsonPairs(String content) {
        java.util.List<String> parts = new java.util.ArrayList<>();

        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if (c == '"') {
                insideQuotes = !insideQuotes;
            }

            if (c == ',' && !insideQuotes) {
                parts.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        if (!current.isEmpty()) {
            parts.add(current.toString());
        }

        return parts.toArray(new String[0]);
    }

    private static String removeQuotes(String value) {
        String result = value.trim();

        if (result.startsWith("\"") && result.endsWith("\"") && result.length() >= 2) {
            result = result.substring(1, result.length() - 1);
        }

        return result;
    }

    public static Object convertValue(String value, Class<?> targetType) {
        if (targetType == String.class) {
            return value;
        }

        if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(value);
        }

        if (targetType == long.class || targetType == Long.class) {
            return Long.parseLong(value);
        }

        if (targetType == boolean.class || targetType == Boolean.class) {
            return Boolean.parseBoolean(value);
        }

        if (targetType == double.class || targetType == Double.class) {
            return Double.parseDouble(value);
        }

        return value;
    }
}
