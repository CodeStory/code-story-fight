import com.google.common.collect.Maps;
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

	protected int get(String keyword) {
		int[] ids = talkIds.withKeyword(keyword);
		return votes.score(ids);
	}
}
