import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.StringWriter;
import java.net.URI;
import java.util.Map;

@Singleton
@Path("/")
public class FightResource {
	final Scorer scorer;
	final Mustache indexTemplate;

	@Inject
	public FightResource(Scorer scorer) {
		this.scorer = scorer;
		indexTemplate = new DefaultMustacheFactory().compile("web/index.html");
	}

	@GET
	public Response index() {
		return Response.seeOther(URI.create("/fight/AngularJS/JavaFX")).build();
	}

	@GET
	@Path("index.html")
	public Response oldIndex() {
		return index();
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
		Map<String, Object> data = scorer.get(leftKeyword, rightKeyword);

		return indexTemplate.execute(new StringWriter(), data).toString();
	}
}
