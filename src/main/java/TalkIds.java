import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public class TalkIds {
	private final Map<Integer, List<String>> keywordsByIds;

	@VisibleForTesting
	 TalkIds(Map<Integer, List<String>> keywordsByIds) {
		this.keywordsByIds = keywordsByIds;
	}

	public TalkIds(URL planningUrl) throws IOException {
		keywordsByIds = Maps.newHashMap();

		Days days = new Gson().fromJson(Resources.toString(planningUrl, Charsets.UTF_8), Days.class);
		for (Day day : days.days) {
			for (Slot slot : day.slots) {
				for (Talk talk : slot.talks) {
					List<String> keywords = Lists.newArrayList();
					keywords.add(talk.title);
					keywords.add(talk.summary);
					keywords.addAll(talk.tags);
					keywords.addAll(talk.speakers);
					keywordsByIds.put(talk.id, keywords);
				}
			}
		}
	}

	public Set<Integer> withKeyword(String keyword) {
		Set<Integer> ids = newHashSet();
		for (Map.Entry<Integer, List<String>> keywordsForId : keywordsByIds.entrySet()) {
			for (String talkKeyword : keywordsForId.getValue()) {
				if (talkKeyword.toLowerCase().contains(keyword.toLowerCase())) {
					ids.add(keywordsForId.getKey());
				}
			}
		}
		return ids;
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
