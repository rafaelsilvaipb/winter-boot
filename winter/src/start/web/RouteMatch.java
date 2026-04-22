package start.web;

import java.util.Map;

public class RouteMatch {

    private final RouteDefinition routeDefinition;
    private final Map<String, String> pathVariables;

    public RouteMatch(RouteDefinition routeDefinition, Map<String, String> pathVariables) {
        this.routeDefinition = routeDefinition;
        this.pathVariables = pathVariables;
    }

    public RouteDefinition getRouteDefinition() {
        return routeDefinition;
    }

    public Map<String, String> getPathVariables() {
        return pathVariables;
    }
}