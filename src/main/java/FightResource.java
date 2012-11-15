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
import java.util.Date;
import java.util.Map;

@Singleton
@Path("/")
public class FightResource {
	private static final long ONE_MONTH = 1000L * 3600 * 24 * 30;

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
	@Produces("image/jpeg")
	public Response background() {
		return staticResource("web/bg.jpeg");
	}

	@GET
	@Path("fight/{left}/{right}")
	@Produces("text/html;charset=UTF-8")
	public String fight(@PathParam("left") String leftKeyword, @PathParam("right") String rightKeyword) {
		Map<String, Object> data = scorer.get(leftKeyword, rightKeyword);

		return indexTemplate.execute(new StringWriter(), data).toString();
	}

	private static Response staticResource(String path) {
		File file = new File(path);
		long modified = file.lastModified();

		return Response.ok(file).lastModified(new Date(modified)).expires(new Date(modified + ONE_MONTH)).build();
	}
}
