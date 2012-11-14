import com.google.common.base.Splitter;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Maps.newHashMap;
import static java.lang.Integer.valueOf;

public class Scores {
	private Map<Integer, Integer> talksIds;

	public Scores(Map<Integer, Integer> talksIds) {
		this.talksIds = talksIds;
	}

	public Scores(String talkIdsJson) {
		this.talksIds = newHashMap();
		Iterable<String> talkAndScores = Splitter.on(",").split(talkIdsJson.subSequence(1, talkIdsJson.length() - 1));
		for (String talkAndScore : talkAndScores) {
			Iterable<String> split = Splitter.on(':').trimResults().split(talkAndScore);
			this.talksIds.put(valueOf(getFirst(split, "-1")), valueOf(getLast(split, "-1")));
		}
	}

	public Integer getScore(Iterable<Integer> talkIds) {
		Integer sum = 0;
		for (Integer talkId : talkIds) {
			sum += firstNonNull(talksIds.get(talkId), 0);
		}
		return sum;
	}
}
