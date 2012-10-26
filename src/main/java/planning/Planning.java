package planning;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.inject.Singleton;

@Singleton
public class Planning {
  private final SetMultimap<String, String> talksPerUser = HashMultimap.create();

  public Iterable<String> stars(String login) {
    return talksPerUser.get(login);
  }

  public void star(String login, String talkId) {
    talksPerUser.put(login, talkId);
  }

  public void unstar(String login, String talkId) {
    talksPerUser.remove(login, talkId);
  }
}
