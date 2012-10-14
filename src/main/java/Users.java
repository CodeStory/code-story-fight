import auth.User;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Map;

public class Users {
  private Map<Long, User> users = Maps.newHashMap();

  public void add(User user) {
    users.put(user.getId(), user);
  }

  public User withId(Long userId) {
    return users.get(userId);
  }
}
