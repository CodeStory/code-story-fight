import com.google.inject.Guice;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.GuiceComponentProviderFactory;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.URI;

import static com.google.common.base.Objects.firstNonNull;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

public class FightServer {
	HttpServer httpServer;

	public static void main(String[] args) throws Exception {
		int port = parseInt(firstNonNull(System.getenv("PORT"), "8080"));
		new FightServer().start(port);
	}

	public void start(int port) throws IOException {
		ResourceConfig configuration = new DefaultResourceConfig(FightResource.class);
		GuiceComponentProviderFactory ioc = new GuiceComponentProviderFactory(configuration, Guice.createInjector(new JerseyModule()));

		httpServer = HttpServerFactory.create(
			URI.create(format("http://localhost:%d/", port)),
			configuration,
			ioc);
		httpServer.start();
	}

	public void stop() {
		httpServer.stop(0);
	}
}
