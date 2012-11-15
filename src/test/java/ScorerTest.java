import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScorerTest {
	TalkIds talkIds = mock(TalkIds.class);
	Votes votes = mock(Votes.class);

	@Test
	public void should_compute_score_for_keywords() {
		when(talkIds.withKeyword("angularJs")).thenReturn(new int[]{1, 2});
		when(votes.getScore(1, 2)).thenReturn(100);

		Scorer scorer = new Scorer(talkIds, votes);
		int score = scorer.get("angularJs");

		assertThat(score).isEqualTo(100);
	}
}
