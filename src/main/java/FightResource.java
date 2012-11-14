import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.StringWriter;
import java.net.URI;
import java.util.Map;

@Path("/")
public class FightResource {
	final Scorer scorer;

	@Inject
	public FightResource(Scorer scorer) {
		this.scorer = scorer;
	}

	@GET
	public Response index() {
		return Response.seeOther(URI.create("/fight/AngularJS/JavaFX")).build();
	}

	@GET
	@Path("style.less")
	@Produces("text/css;charset=UTF-8")
	public File style() {
		return new File("web/style.less");
	}

	@GET
	@Path("images/bg.jpeg")
	@Produces("text/css;charset=UTF-8")
	public File background() {
		return new File("web/bg.jpeg");
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

		Mustache template = new DefaultMustacheFactory().compile("web/index.html");
		StringWriter html = new StringWriter();
		template.execute(html, data);

		return html.toString();
	}
}
