import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AtomicLongMap;
import com.sun.org.apache.xpath.internal.SourceTree;

import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.ImmutableMap.copyOf;
import static com.google.common.io.Files.toByteArray;
import static com.google.common.io.Files.write;
import static com.google.common.util.concurrent.AtomicLongMap.create;

@Singleton
public class TopFights {
	private AtomicLongMap<TopFight> fightCount;
	private File outputFile;

	public TopFights() {
		init(true);
	}

	/*package*/ TopFights(boolean doReadFile) {
		init(doReadFile);
	}

	private void init(boolean readFile) {
		fightCount = create();
		outputFile = new File("fightCount.bin");
		if (readFile) {
			loadFromDisk();
		}
	}

	public void empty() {
		fightCount.clear();
	}

	public Set<TopFight> get() {
		Set<Map.Entry<TopFight, Long>> topFightEntries = fightCount.asMap().entrySet();
		ArrayList<Map.Entry<TopFight, Long>> entries = Lists.newArrayList(topFightEntries);
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
		saveOnDisk();
	}

	public synchronized void loadFromDisk() {
		if (!outputFile.exists()) {
			return;
		}
		try {
			byte[] raw = toByteArray(outputFile);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(raw));
			fightCount = create((Map<TopFight, Long>) ois.readObject());
			ois.close();
		} catch (ClassNotFoundException | IOException exception) {
			throw new RuntimeException(exception.getMessage());
		}
	}

	public synchronized void saveOnDisk() {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
			oos.writeObject(copyOf(fightCount.asMap()));
			oos.flush();
			write(byteArrayOutputStream.toByteArray(), outputFile);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe.getMessage());
		}
	}

}
