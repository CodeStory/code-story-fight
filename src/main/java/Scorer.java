import java.util.Set;

public class Scorer {
	private final TalkIds talkIds;
	private final Scores scores;

	public Scorer(TalkIds talkIds, Scores scores) {
		this.talkIds = talkIds;
		this.scores = scores;
	}

	public int get(String keyword) {
		Set<Integer> ids = talkIds.withKeyword(keyword);
		return scores.getScore(ids);
	}
}
