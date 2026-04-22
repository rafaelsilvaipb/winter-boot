package start.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteMatcher {

    public static RouteMatch findRoute(List<RouteDefinition> routes, String httpMethod, String requestPath) {
        for (RouteDefinition route : routes) {
            if (!route.getHttpMethod().equalsIgnoreCase(httpMethod)) {
                continue;
            }

            Map<String, String> pathVariables = matchPath(route.getPath(), requestPath);

            if (pathVariables != null) {
                return new RouteMatch(route, pathVariables);
            }
        }

        return null;
    }

    private static Map<String, String> matchPath(String routePath, String requestPath) {
        String[] routeParts = normalize(routePath).split("/");
        String[] requestParts = normalize(requestPath).split("/");

        if (routeParts.length != requestParts.length) {
            return null;
        }

        Map<String, String> pathVariables = new HashMap<>();

        for (int i = 0; i < routeParts.length; i++) {
            String routePart = routeParts[i];
            String requestPart = requestParts[i];

            if (routePart.startsWith("{") && routePart.endsWith("}")) {
                String variableName = routePart.substring(1, routePart.length() - 1);
                pathVariables.put(variableName, requestPart);
                continue;
            }

            if (!routePart.equals(requestPart)) {
                return null;
            }
        }

        return pathVariables;
    }

    private static String normalize(String path) {
        if (path == null || path.isEmpty()) {
            return "/";
        }

        String normalized = path.replaceAll("/+", "/");

        if (normalized.length() > 1 && normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        return normalized;
    }
}