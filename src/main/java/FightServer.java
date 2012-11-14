import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class FightServer {
		HttpServer server;

	public static void main(String[] args) throws Exception {
		new FightServer().start(8080);
	}

	public void start(int port) throws IOException {
		server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext("/", new HttpHandler() {
			@Override
			public void handle(HttpExchange exchange) throws IOException {
				String body = Files.toString(new File("index.html"), Charsets.UTF_8);

				byte[] response = body.getBytes();
				exchange.sendResponseHeaders(200, response.length);
				exchange.getResponseHeaders().add("content-type", "text/html");
				exchange.getResponseBody().write(response);
				exchange.close();
			}
		});
		server.start();
	}

	public void stop() {
		server.stop(0);
	}
}
