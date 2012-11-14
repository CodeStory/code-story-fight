import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import org.junit.Test;

import java.io.IOException;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;

public class VotesTest {
	@Test
	public void should_sum_up_scores_from_talk_ids() {
		Votes scores = new Votes(ImmutableMap
			.of(931, 19, 805, 18, 200, 3));

		assertThat(scores.getScore(newArrayList(931, 805))).isEqualTo(37);
	}

	@Test
	public void should_sum_up_scores_with_a_real_URL() throws IOException {
		Votes scores = new Votes(Resources.getResource("starsPerTalk.json"));

		assertThat(scores.getScore(newArrayList(931, 805))).isEqualTo(37);
	}
}
