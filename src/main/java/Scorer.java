import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Map;

@Singleton
public class Scorer {
	private final TalkIds talkIds;
	private final Votes votes;

	@Inject
	public Scorer(TalkIds talkIds, Votes votes) {
		this.talkIds = talkIds;
		this.votes = votes;
	}

	public Map<String, Object> get(String leftKeyword, String rightKeyword) {
		return ImmutableMap.<String, Object>of(
			"leftKeyword", leftKeyword,
			"rightKeyword", rightKeyword,
			"leftScore", get(leftKeyword),
			"rightScore", get(rightKeyword));
	}

	private int get(String keyword) {
		int[] ids = talkIds.withKeyword(keyword);
		return votes.score(ids);
	}
}
