import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.inject.Singleton;

@Singleton
public class Planning {
  private SetMultimap<String, String> registrationsPerUser = HashMultimap.create();

  public Iterable<String> registrations(String login) {
    return registrationsPerUser.get(login);
  }

  public void register(String login, String talkId) {
    registrationsPerUser.put(login, talkId);
  }

  public void unregister(String login, String talkId) {
    registrationsPerUser.remove(login, talkId);
  }
}
