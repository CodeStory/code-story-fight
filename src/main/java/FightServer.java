import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
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
	final Injector injector;
	HttpServer httpServer;

	public FightServer(Module... modules) {
		injector = Guice.createInjector(Modules.override(new JerseyModule()).with(modules));
	}

	public static void main(String[] args) throws Exception {
		int port = parseInt(firstNonNull(System.getenv("PORT"), "8080"));
		new FightServer().start(port);
	}

	public void start(int port) throws IOException {
		ResourceConfig configuration = new DefaultResourceConfig(FightResource.class);
		GuiceComponentProviderFactory ioc = new GuiceComponentProviderFactory(configuration, injector);

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
