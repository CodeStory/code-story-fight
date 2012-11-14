import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;

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
		Votes scores = new Votes(votesUrl);
		Scorer scorer = new Scorer(talkIds, scores);

		new FightServer(scorer).start(8080);
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String body = Files.toString(new File("index.html"), Charsets.UTF_8);

		byte[] response = body.getBytes();
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
