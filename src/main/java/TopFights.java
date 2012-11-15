import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AtomicLongMap;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

@Singleton
public class TopFights {

	private final AtomicLongMap<TopFight> fightCount;

	public TopFights() {
		fightCount = AtomicLongMap.create();
	}

	public TopFight get() {
		Set<Map.Entry<TopFight,Long>> topFights = fightCount.asMap().entrySet();
		ArrayList<Map.Entry<TopFight,Long>> entries1 = Lists.newArrayList(topFights);
		Collections.sort(entries1, new Comparator<Map.Entry<TopFight, Long>>() {
			@Override
			public int compare(Map.Entry<TopFight, Long> o1, Map.Entry<TopFight, Long> o2) {
				return Long.compare(o2.getValue(), o1.getValue());
			}
		});
		TopFight topFight1 = entries1.get(0).getKey();
		return topFight1;
	}

	public void log(String left, String right) {
		fightCount.incrementAndGet(new TopFight(left, right));
	}

}
