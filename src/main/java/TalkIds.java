import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public class TalkIds {

	private Map<Integer, List<String>> keywordsByIds;

	public TalkIds(Map<Integer, List<String>> keywordsByIds) {
		this.keywordsByIds = keywordsByIds;
	}

	public Set<Integer> withKeyword(String keyword) {
		Set<Integer> ids = newHashSet();
		for (Map.Entry<Integer, List<String>> keywordsForId : keywordsByIds.entrySet()) {
			for (String talkKeyword : keywordsForId.getValue()) {
				if (talkKeyword.equalsIgnoreCase(keyword)) {
					ids.add(keywordsForId.getKey());
				}
			}
		}
		return ids;
	}
}
