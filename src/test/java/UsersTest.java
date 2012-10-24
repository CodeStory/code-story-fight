import auth.User;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class UsersTest {
  Users users = new Users();

  @Test
  public void should_find_user_by_id() {
    User user = new User(42L, "foo", "bar", "baz");
    users.add(user);

    assertThat(users.withId(42L)).isEqualTo(user);
  }

  @Test
  public void should_remove_user_by_id() {
    User user = new User(42L, "foo", "bar", "baz");
    users.add(user);

    users.remove(42L);

    assertThat(users.withId(42L)).isNull();
  }
}
