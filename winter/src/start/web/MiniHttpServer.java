package start.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiniHttpServer {

    private final List<RouteDefinition> routes;

    public MiniHttpServer(List<RouteDefinition> routes) {
        this.routes = routes;
    }

    public void start(int port) throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Servidor HTTP rodando na porta " + port);

        while (true) {
            Socket client = serverSocket.accept();

            try {
                handle(client);
            } catch (Exception e) {
                e.printStackTrace();
                sendText(client.getOutputStream(), 500, "Internal Server Error", "Erro interno no servidor");
            } finally {
                client.close();
            }
        }
    }

    private void handle(Socket client) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        OutputStream out = client.getOutputStream();

        String requestLine = in.readLine();

        if (requestLine == null || requestLine.isEmpty()) {
            return;
        }

        System.out.println("Request: " + requestLine);

        String method = requestLine.split(" ")[0];
        String fullPath = requestLine.split(" ")[1];

        String path;
        String queryString = null;

        if (fullPath.contains("?")) {
            String[] parts = fullPath.split("\\?", 2);
            path = parts[0];
            queryString = parts[1];
        } else {
            path = fullPath;
        }

        Map<String, String> headers = new HashMap<>();
        String line;
        int contentLength = 0;

        while ((line = in.readLine()) != null && !line.isEmpty()) {
            String[] headerParts = line.split(":", 2);
            if (headerParts.length == 2) {
                String headerName = headerParts[0].trim();
                String headerValue = headerParts[1].trim();
                headers.put(headerName, headerValue);

                if (headerName.equalsIgnoreCase("Content-Length")) {
                    contentLength = Integer.parseInt(headerValue);
                }
            }
        }

        char[] bodyChars = new char[contentLength];
        if (contentLength > 0) {
            in.read(bodyChars, 0, contentLength);
        }
        String body = new String(bodyChars);

        if (path.equals("/favicon.ico")) {
            out.write("HTTP/1.1 204 No Content\r\n\r\n".getBytes(StandardCharsets.UTF_8));
            out.flush();
            return;
        }

        RouteMatch routeMatch = RouteMatcher.findRoute(routes, method, path);

        if (routeMatch == null) {
            sendText(out, 404, "Not Found", "Rota não encontrada");
            out.flush();
            return;
        }

        HttpRequestData requestData = new HttpRequestData(
                method,
                path,
                RequestParser.parseQueryParams(queryString),
                routeMatch.getPathVariables(),
                body
        );

        RouteDefinition route = routeMatch.getRouteDefinition();
        Method handlerMethod = route.getHandlerMethod();
        Object controller = route.getControllerInstance();

        Object[] args = MethodParameterResolver.resolveParameters(handlerMethod, requestData);

        Object result = handlerMethod.invoke(controller, args);

        if (result == null) {
            sendJson(out, "null");
        } else if (result instanceof String) {
            sendText(out, 200, "OK", result.toString());
        } else {
            String json = JsonSerializer.toJson(result);
            sendJson(out, json);
        }
        out.flush();
    }

    private void sendJson(OutputStream out, String json) throws Exception {

        byte[] body = json.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        String headers =
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: application/json; charset=UTF-8\r\n" +
                        "Content-Length: " + body.length + "\r\n" +
                        "\r\n";

        out.write(headers.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        out.write(body);
    }

    private void sendText(OutputStream out, int statusCode, String statusText, String bodyText) throws Exception {
        byte[] body = bodyText.getBytes(StandardCharsets.UTF_8);

        String headers =
                "HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
                        "Content-Type: text/plain; charset=UTF-8\r\n" +
                        "Content-Length: " + body.length + "\r\n" +
                        "\r\n";

        out.write(headers.getBytes(StandardCharsets.UTF_8));
        out.write(body);
    }
}