import com.google.inject.Inject;

public class Scorer {
	private final TalkIds talkIds;
	private final Votes votes;

	@Inject
	public Scorer(TalkIds talkIds, Votes votes) {
		this.talkIds = talkIds;
		this.votes = votes;
	}

	public int get(String keyword) {
		int[] ids = talkIds.withKeyword(keyword);
		return votes.getScore(ids);
	}
}
