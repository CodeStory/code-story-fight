package planning;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.google.common.util.concurrent.AtomicLongMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Singleton
public class Planning {
  private final File root;
  private final LoadingCache<String, Set<String>> talksPerUser = CacheBuilder.newBuilder().build(new CacheLoader<String, Set<String>>() {
    @Override
    public Set<String> load(String key) {
      return Sets.newHashSet();
    }
  });
  private final AtomicLongMap<String> starsPerTalk = AtomicLongMap.create();

  @Inject
  public Planning(@Named("planning.root") File root) throws IOException {
    this.root = root;
    load(root);
  }

  public Set<String> stars(String login) {
    Set<String> userTalks = userTalks(login);
    synchronized (userTalks) {
      return ImmutableSet.copyOf(userTalks);
    }
  }

  public void star(String login, String talkId) {
    Set<String> userTalks = userTalks(login);
    synchronized (userTalks) {
      userTalks.add(talkId);

      writeToFile(login, "+", talkId);
    }
    starsPerTalk.incrementAndGet(talkId);
  }

  public void unstar(String login, String talkId) {
    Set<String> userTalks = userTalks(login);
    synchronized (userTalks) {
      userTalks.remove(talkId);

      writeToFile(login, "-", talkId);
    }
    starsPerTalk.decrementAndGet(talkId);
  }

  private void writeToFile(String login, String action, String talkId) {
    try {
      File file = new File(root, login);
      Files.append(action + talkId + "\n", file, Charsets.UTF_8);
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  private Set<String> userTalks(String login) {
    return talksPerUser.getUnchecked(login);
  }

  private void load(File root) throws IOException {
    root.mkdirs();

    File[] files = root.listFiles();
    if (null == files) {
      return;
    }

    for (File file : files) {
      String login = file.getName();
      Set<String> talkIds = loadUserTalks(file);

      talksPerUser.put(login, talkIds);

      for (String talkId : talkIds) {
        starsPerTalk.incrementAndGet(talkId);
      }
    }
  }

  private Set<String> loadUserTalks(File file) throws IOException {
    return Files.readLines(file, Charsets.UTF_8, new LineProcessor<Set<String>>() {
      private final Set<String> talkIds = Sets.newHashSet();

      @Override
      public boolean processLine(String line) {
        String talkId = line.substring(1);

        if (line.startsWith("-")) {
          talkIds.remove(talkId);
        } else {
          talkIds.add(talkId);
        }

        return true;
      }

      @Override
      public Set<String> getResult() {
        return talkIds;
      }
    });
  }

  public Map<String, Long> countPerTalk() {
    return starsPerTalk.asMap();
  }
}
