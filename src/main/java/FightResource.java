import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.util.Map;

@Path("/")
public class FightResource {
	final Scorer scorer;

	public FightResource() throws IOException {
		URL planningUrl = Resources.getResource("planning.json");
		URL votesUrl = Resources.getResource("starsPerTalk.json");

		TalkIds talkIds = new TalkIds(planningUrl);
		Votes votes = new Votes(votesUrl);
		this.scorer = new Scorer(talkIds, votes);
	}

	@GET
	public Response index() {
		return Response.seeOther(URI.create("/fight/AngularJS/JavaFX")).build();
	}

	@GET
	@Path("fight/{left}/{right}")
	@Produces("text/html;charset=UTF-8")
	public String fight(@PathParam("left") String leftKeyword, @PathParam("right") String rightKeyword) {
		Map<String, Object> data = ImmutableMap.<String, Object>of(
			"leftKeyword", leftKeyword,
			"rightKeyword", rightKeyword,
			"leftScore", scorer.get(leftKeyword),
			"rightScore", scorer.get(rightKeyword));

		Mustache template = new DefaultMustacheFactory().compile("index.html");
		StringWriter html = new StringWriter();
		template.execute(html, data);

		return html.toString();
	}
}
