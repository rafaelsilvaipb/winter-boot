package start.web;

import java.util.Map;

public class HttpRequestData {

    private final String method;
    private final String path;
    private final Map<String, String> queryParams;
    private final Map<String, String> pathVariables;
    private final String body;

    public HttpRequestData(String method,
                           String path,
                           Map<String, String> queryParams,
                           Map<String, String> pathVariables,
                           String body) {
        this.method = method;
        this.path = path;
        this.queryParams = queryParams;
        this.pathVariables = pathVariables;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public Map<String, String> getPathVariables() {
        return pathVariables;
    }

    public String getBody() {
        return body;
    }
}