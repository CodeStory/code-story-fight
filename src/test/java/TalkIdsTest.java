import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class TalkIdsTest {

	@Test
	public void should_find_ids_from_keywords() {
		TalkIds talkIds = new TalkIds(ImmutableMap.of(
				1, ImmutableList.of("javafx", "keyword"),
				2, ImmutableList.of("AngularJS", "anotherKeyword"),
				3, (List<String>) ImmutableList.of("nothing")));

		Set<Integer> ids = talkIds.withKeyword("javafx");

		assertThat(ids).containsOnly(1);
	}

	@Test
	public void should_find_ids_from_keywords_with_different_case() {
		TalkIds talkIds = new TalkIds(ImmutableMap.of(
				42, (List<String>) ImmutableList.of("javaFX")));

		Set<Integer> ids = talkIds.withKeyword("JAVAfx");

		assertThat(ids).containsOnly(42);
	}

}
