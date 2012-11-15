import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.google.common.collect.Maps;
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

	private final Scorer scorer;
	private final TopFights topFights;
	private final Mustache indexTemplate;

	@Inject
	public FightResource(Scorer scorer,TopFights topFights) {
		this.scorer = scorer;
		this.topFights = topFights;
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
	@Path("images/star.png")
	@Produces("image/png")
	public Response star() {
		return staticResource("web/star.png");
	}

	@GET
	@Path("fight/{left}/{right}")
	@Produces("text/html;charset=UTF-8")
	public String fight(@PathParam("left") String leftKeyword, @PathParam("right") String rightKeyword) {
		topFights.log(leftKeyword, rightKeyword);
		Map<String, Object> templateData = Maps.newHashMap();
		templateData.put("leftKeyword", leftKeyword);
		templateData.put("rightKeyword", rightKeyword);
		templateData.put("leftScore", scorer.get(leftKeyword));
		templateData.put("rightScore", scorer.get(rightKeyword));

		TopFight topFight = topFights.get();

		templateData.put("topLeftKeyword", topFight.getLeft());
		templateData.put("topRightKeyword", topFight.getRight());

		return indexTemplate.execute(new StringWriter(), templateData).toString();
	}

	private static Response staticResource(String path) {
		File file = new File(path);
		long modified = file.lastModified();

		return Response.ok(file).lastModified(new Date(modified)).expires(new Date(modified + ONE_MONTH)).build();
	}
}
