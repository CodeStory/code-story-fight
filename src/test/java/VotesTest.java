import com.google.common.io.Resources;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class VotesTest {
	@Test
	public void should_sum_up_scores_with_a_real_url() {
		Votes scores = new Votes(Resources.getResource("starsPerTalk.json"));

		int score = scores.score(931, 805);

		assertThat(score).isEqualTo(37);
	}
}
