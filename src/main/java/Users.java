import auth.User;
import com.google.common.collect.Maps;

import java.util.Map;

public class Users {
  private final Map<Long, User> users = Maps.newHashMap();

  public void add(User user) {
    users.put(user.getId(), user);
  }

  public User withId(Long userId) {
    return users.get(userId);
  }

  public User remove(Long userId) {
    return users.remove(userId);
  }
}
