import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Map;

public class FightServer implements HttpHandler {
	final Scorer scorer;
	HttpServer server;

	FightServer(Scorer scorer) {
		this.scorer = scorer;
	}

	public static void main(String[] args) throws Exception {
		URL planningUrl = Resources.getResource("planning.json");
		URL votesUrl = Resources.getResource("starsPerTalk.json");

		TalkIds talkIds = new TalkIds(planningUrl);
		Votes votes = new Votes(votesUrl);
		Scorer scorer = new Scorer(talkIds, votes);

		new FightServer(scorer).start(8080);
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		DefaultMustacheFactory mustacheFactory = new DefaultMustacheFactory();
		Mustache mustacheTemplate = mustacheFactory.compile("index.html");
		StringWriter writer = new StringWriter();
		Map<String, String> data = ImmutableMap.of(
			"leftKeyword", "AngularJS",
			"rightKeyword", "JavaFX",
			"leftScore", "37",
			"rightScore", "42");
		mustacheTemplate.execute(writer, data);


		byte[] response = writer.toString().getBytes();
		exchange.sendResponseHeaders(200, response.length);
		exchange.getResponseHeaders().add("content-type", "text/html");
		exchange.getResponseBody().write(response);
		exchange.close();
	}

	public void start(int port) throws IOException {
		server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext("/", this);
		server.start();
	}

	public void stop() {
		server.stop(0);
	}
}
