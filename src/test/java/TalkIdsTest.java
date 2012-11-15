import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;

public class TalkIdsTest {
	@Test
	public void should_find_ids_from_keywords() {
		TalkIds talkIds = new TalkIds(ImmutableMap.of(
			1, asList("javafx", "keyword"),
			2, asList("AngularJS", "anotherKeyword"),
			3, asList("nothing")));

		int[] ids = talkIds.withKeyword("javafx");

		assertThat(ids).containsOnly(1);
	}

	@Test
	public void should_find_ids_from_keywords_with_different_case() {
		TalkIds talkIds = new TalkIds(ImmutableMap.of(
			42, asList("javaFX")));

		int[] ids = talkIds.withKeyword("JAVAfx");

		assertThat(ids).containsOnly(42);
	}

	@Test
	public void should_read_planning_as_json() {
		TalkIds talkIds = new TalkIds(Resources.getResource("planning.json"));

		int[] ids = talkIds.withKeyword("AngularJs");

		assertThat(ids).containsOnly(931, 805);
	}

	@Test
	public void should_search_on_all_metadata() {
		TalkIds talkIds = new TalkIds(Resources.getResource("planning.json"));

		int[] ids = talkIds.withKeyword("JavaFX");

		assertThat(ids).hasSize(13);
	}
}
