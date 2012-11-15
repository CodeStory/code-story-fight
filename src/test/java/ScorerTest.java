import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScorerTest {
	Talks talkIds = mock(Talks.class);
	Votes votes = mock(Votes.class);
	Scorer scorer = new Scorer(talkIds, votes);

	@Test
	public void should_compute_scores_for_two_keywords() {
		when(talkIds.ids("angularJs")).thenReturn(new int[]{1, 2});
		when(talkIds.ids("java")).thenReturn(new int[]{3, 4, 5});
		when(votes.score(1, 2)).thenReturn(100);
		when(votes.score(3, 4, 5)).thenReturn(66);

		assertThat(scorer.get("angularJs")).isEqualTo(100);
		assertThat(scorer.get("java")).isEqualTo(66);
	}
}
