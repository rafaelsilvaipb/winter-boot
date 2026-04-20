package start.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
        String rawPath = requestLine.split(" ")[1];

        String path = rawPath.contains("?") ? rawPath.split("\\?", 2)[0] : rawPath;

        if (path.equals("/favicon.ico")) {
            out.write("HTTP/1.1 204 No Content\r\n\r\n".getBytes(StandardCharsets.UTF_8));
            out.flush();
            return;
        }

        RouteDefinition route = findRoute(method, path);

        if (route == null) {
            sendText(out, 404, "Not Found", "Rota não encontrada");
            out.flush();
            return;
        }

        Method handlerMethod = route.getHandlerMethod();
        Object controller = route.getControllerInstance();

        Object result = handlerMethod.invoke(controller);

        String body = result != null ? result.toString() : "";

        sendText(out, 200, "OK", body);
        out.flush();
    }

    private RouteDefinition findRoute(String httpMethod, String path) {
        for (RouteDefinition route : routes) {
            if (route.getHttpMethod().equalsIgnoreCase(httpMethod)
                    && route.getPath().equals(path)) {
                return route;
            }
        }

        return null;
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
