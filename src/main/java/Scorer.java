public class Scorer {
	private final TalkIds talkIds;
	private final Scores scores;

	public Scorer(TalkIds talkIds, Scores scores) {

		this.talkIds = talkIds;
		this.scores = scores;
	}

	public int get(String keyword) {
		return scores.getScore(talkIds.withKeyword(keyword));
	}
}
