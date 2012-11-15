import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Map;

import static com.google.common.base.Objects.firstNonNull;

@Singleton
public class Votes {
	private final URL url;

	@Inject
	public Votes(@Named("votesUrl") URL url) {
		this.url = url;
	}

	public int score(int... talkIds) {
		Map<Integer, Integer> votes = parseVotes();

		int sum = 0;
		for (int talkId : talkIds) {
			sum += firstNonNull(votes.get(talkId), 0);
		}
		return sum;
	}

	private Map<Integer, Integer> parseVotes() {
		Type typeToken = new TypeToken<Map<Integer, Integer>>() {
		}.getType();


		try {
			return new Gson().fromJson(Resources.toString(url, Charsets.UTF_8), typeToken);
		} catch (IOException e) {
			throw new IllegalStateException("Unable to read votes json", e);
		}
	}
}
