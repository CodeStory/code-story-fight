import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Map;

import static com.google.common.base.Objects.firstNonNull;

public class Scores {
	private final Map<Integer, Integer> talksIds;

	@VisibleForTesting
	Scores(Map<Integer, Integer> talksIds) {
		this.talksIds = talksIds;
	}

	public Scores(String talkIdsJson) {
		Type typeToken = new TypeToken<Map<Integer, Integer>>() {
		}.getType();
		this.talksIds = new Gson().fromJson(talkIdsJson, typeToken);
	}

	public Scores(URL resource) throws IOException {
		this(Resources.toString(resource, Charsets.UTF_8));
	}

	public Integer getScore(Iterable<Integer> talkIds) {
		Integer sum = 0;
		for (Integer talkId : talkIds) {
			sum += firstNonNull(talksIds.get(talkId), 0);
		}
		return sum;
	}
}
