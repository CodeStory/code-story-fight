import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AtomicLongMap;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@Singleton
public class TopFights {

	private final AtomicLongMap<TopFight> fightCount;

	public TopFights() {
		fightCount = AtomicLongMap.create();
	}

	public Set<TopFight> get() {
		Set<Map.Entry<TopFight,Long>> topFightEntries = fightCount.asMap().entrySet();
		ArrayList<Map.Entry<TopFight,Long>> entries = Lists.newArrayList(topFightEntries);
		Collections.sort(entries, new Comparator<Map.Entry<TopFight, Long>>() {
			@Override
			public int compare(Map.Entry<TopFight, Long> o1, Map.Entry<TopFight, Long> o2) {
				return Long.compare(o2.getValue(), o1.getValue());
			}
		});

		Set<TopFight> topFights = new LinkedHashSet();

		for (Map.Entry<TopFight, Long> topFightLongEntry : entries) {
			topFights.add(topFightLongEntry.getKey());
			if (topFights.size() == 5) {
				return topFights;
			}
		}

		return topFights;
	}

	public void log(String left, String right) {
		fightCount.incrementAndGet(new TopFight(left, right));
	}

}
