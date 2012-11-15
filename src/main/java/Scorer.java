import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Scorer {
	private final Talks talkIds;
	private final Votes votes;

	@Inject
	public Scorer(Talks talkIds, Votes votes) {
		this.talkIds = talkIds;
		this.votes = votes;
	}

	protected int get(String keyword) {
		int[] ids = talkIds.ids(keyword);
		return votes.score(ids);
	}
}
