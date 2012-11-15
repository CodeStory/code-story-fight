import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import org.junit.Test;

import java.util.Set;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;

public class TalksTest {
	@Test
	public void should_find_ids_from_keywords() {
		Talks talks = new Talks(ImmutableMap.of(
			1, asList("javafx", "keyword"),
			2, asList("AngularJS", "anotherKeyword"),
			3, asList("nothing")));

		int[] ids = talks.ids("javafx");

		assertThat(ids).containsOnly(1);
	}

	@Test
	public void should_find_ids_from_keywords_with_different_case() {
		Talks talks = new Talks(ImmutableMap.of(
			42, asList("javaFX")));

		int[] ids = talks.ids("JAVAfx");

		assertThat(ids).containsOnly(42);
	}

	@Test
	public void should_read_planning_as_json() {
		Talks talks = new Talks(Resources.getResource("planning.json"));

		int[] ids = talks.ids("AngularJs");

		assertThat(ids).containsOnly(931, 805);
	}

	@Test
	public void should_search_on_all_metadata() {
		Talks talks = new Talks(Resources.getResource("planning.json"));

		int[] ids = talks.ids("JavaFX");

		assertThat(ids).hasSize(13);
	}

	@Test
	public void should_extract_words_from_planning() {
		Talks talks = new Talks(Resources.getResource("planning.json"));

		Set<String> extractedWords = talks.extractWords();

		assertThat(extractedWords).contains("maven").excludes("Maven");
	}
}
