import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.URI;

public class FightServer {
	HttpServer httpServer;

	public static void main(String[] args) throws Exception {
		new FightServer().start(8080);
	}

	public void start(int port) throws IOException {
		ResourceConfig configuration = new DefaultResourceConfig(FightResource.class);
		httpServer = HttpServerFactory.create(URI.create("http://localhost:8080/"), configuration);
		httpServer.start();
	}

	public void stop() {
		httpServer.stop(0);
	}
}
