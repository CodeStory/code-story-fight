import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import com.google.inject.Singleton;

import java.util.Iterator;
import java.util.Map;

@Singleton
public class Planning {
  private SetMultimap<String, String> registrationsPerUser = HashMultimap.create();
  private Map<String, String> slotPerTalk = Maps.newHashMap();

  public Iterable<String> registrations(String login) {
    return registrationsPerUser.get(login);
  }

  public void register(String login, String talkId) {
    String slot = slotPerTalk.get(talkId);
    if (null == slot) {
      return;
    }

    Iterator talkIds = registrationsPerUser.get(login).iterator();
    while (talkIds.hasNext()) {
      if (slot.equals(slotPerTalk.get(talkIds.next()))) {
        talkIds.remove();
      }
    }

    registrationsPerUser.put(login, talkId);
  }

  public void unregister(String login, String talkId) {
    registrationsPerUser.remove(login, talkId);
  }

  public void createTalk(String talkId, String slot) {
    slotPerTalk.put(talkId, slot);
  }
}
