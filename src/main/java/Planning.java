import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AtomicLongMap;

import java.util.Map;

public class Planning {
  private final AtomicLongMap<String> registrationsPerTag = AtomicLongMap.create();
  private final ListMultimap<String, String> tagsPerSession = ArrayListMultimap.create();
  private final Map<String, String> userRegistrations = Maps.newHashMap();
  private final Map<String, Integer> slotPerSession = Maps.newHashMap();

  public void createSession(String sessionId, int slot, String... tags) {
    slotPerSession.put(sessionId, slot);

    for (String tag : tags) {
      tagsPerSession.put(sessionId, tag);
    }
  }

  public void register(String login, String sessionId) {
    int slot = slotPerSession.get(sessionId);

    unregister(login, sessionId);
    userRegistrations.put(slot + login, sessionId);
    updateRegistrations(sessionId, +1);
  }

  public void unregister(String login, String sessionId) {
    int slot = slotPerSession.get(sessionId);

    String oldSession = userRegistrations.remove(slot + login);
    if (oldSession != null) {
      updateRegistrations(oldSession, -1);
    }
  }

  public long registrations(String tag) {
    return registrationsPerTag.get(tag);
  }

  private void updateRegistrations(String oldSession, long delta) {
    for (String tag : tagsPerSession.get(oldSession)) {
      registrationsPerTag.addAndGet(tag, delta);
    }
  }
}
