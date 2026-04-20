package start.web;

import java.lang.reflect.Method;

public class RouteDefinition {

    private final String httpMethod;
    private final String path;
    private final Object controllerInstance;
    private final Method handlerMethod;

    public RouteDefinition(String httpMethod, String path, Object controllerInstance, Method handlerMethod) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.controllerInstance = controllerInstance;
        this.handlerMethod = handlerMethod;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public Object getControllerInstance() {
        return controllerInstance;
    }

    public Method getHandlerMethod() {
        return handlerMethod;
    }
}
