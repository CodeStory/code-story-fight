import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import com.google.common.primitives.Ints;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
public class TalkIds {
	private final Map<Integer, List<String>> keywordsByIds;

	@VisibleForTesting
	TalkIds(Map<Integer, List<String>> keywordsByIds) {
		this.keywordsByIds = keywordsByIds;
	}

	@Inject
	public TalkIds(@Named("planningUrl") URL url) throws IOException {
		Days days = new Gson().fromJson(Resources.toString(url, Charsets.UTF_8), Days.class);

		keywordsByIds = Maps.newHashMap();
		for (Day day : days.days) {
			for (Slot slot : day.slots) {
				for (Talk talk : slot.talks) {
					keywordsByIds.put(talk.id, ImmutableList.<String>builder()
						.add(talk.title)
						.add(talk.summary)
						.addAll(talk.tags)
						.addAll(talk.speakers)
						.build());
				}
			}
		}
	}

	public int[] withKeyword(String keyword) {
		Set<Integer> ids = Sets.newHashSet();

		for (Map.Entry<Integer, List<String>> keywordsForId : keywordsByIds.entrySet()) {
			for (String talkKeyword : keywordsForId.getValue()) {
				if (talkKeyword.toLowerCase().contains(keyword.toLowerCase())) {
					ids.add(keywordsForId.getKey());
				}
			}
		}

		return Ints.toArray(ids);
	}

	static class Days {
		List<Day> days;
	}

	static class Day {
		List<Slot> slots;
	}

	static class Slot {
		List<Talk> talks;
	}

	static class Talk {
		int id;
		String title;
		String summary;
		List<String> tags;
		List<String> speakers;
	}
}
