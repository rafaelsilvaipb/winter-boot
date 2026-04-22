package start.web;

import start.annotations.PathVariable;
import start.annotations.RequestBody;
import start.annotations.RequestParam;

import java.lang.reflect.Parameter;

public class MethodParameterResolver {

    public static Object[] resolveParameters(java.lang.reflect.Method method, HttpRequestData request) throws Exception {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> parameterType = parameter.getType();

            if (parameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam annotation = parameter.getAnnotation(RequestParam.class);
                String rawValue = request.getQueryParams().get(annotation.value());
                args[i] = JsonUtils.convertValue(rawValue, parameterType);
                continue;
            }

            if (parameter.isAnnotationPresent(PathVariable.class)) {
                PathVariable annotation = parameter.getAnnotation(PathVariable.class);
                String rawValue = request.getPathVariables().get(annotation.value());
                args[i] = JsonUtils.convertValue(rawValue, parameterType);
                continue;
            }

            if (parameter.isAnnotationPresent(RequestBody.class)) {
                args[i] = JsonUtils.fromJson(request.getBody(), parameterType);
                continue;
            }

            args[i] = null;
        }

        return args;
    }
}
