import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Map;

import static com.google.common.base.Objects.firstNonNull;

public class Votes {
	private final Map<Integer, Integer> talksIds;

	@Inject
	public Votes(@Named("votesUrl") URL url) throws IOException {
		Type typeToken = new TypeToken<Map<Integer, Integer>>() {
		}.getType();
		this.talksIds = new Gson().fromJson(Resources.toString(url, Charsets.UTF_8), typeToken);
	}

	public int getScore(int... talkIds) {
		int sum = 0;
		for (int talkId : talkIds) {
			sum += firstNonNull(talksIds.get(talkId), 0);
		}
		return sum;
	}
}
