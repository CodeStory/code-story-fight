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

public class Votes {
	private final Map<Integer, Integer> talksIds;

	public Votes(URL resource) throws IOException {
		this(Resources.toString(resource, Charsets.UTF_8));
	}

	@VisibleForTesting
	Votes(Map<Integer, Integer> talksIds) {
		this.talksIds = talksIds;
	}

	@VisibleForTesting
	Votes(String talkIdsJson) {
		Type typeToken = new TypeToken<Map<Integer, Integer>>() {
		}.getType();
		this.talksIds = new Gson().fromJson(talkIdsJson, typeToken);
	}

	public Integer getScore(Iterable<Integer> talkIds) {
		int sum = 0;
		for (Integer talkId : talkIds) {
			sum += firstNonNull(talksIds.get(talkId), 0);
		}
		return sum;
	}
}
