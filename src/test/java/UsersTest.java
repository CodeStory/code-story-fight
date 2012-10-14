import auth.User;
import org.fest.assertions.Assertions;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class UsersTest {

  @Test
  public void should_find_user_by_id() throws Exception {
    Users users = new Users();
    User user = new User(42L, "foo", "bar", "baz");
    users.add(user);

    assertThat(users.withId(42L)).isEqualTo(user);
  }
}
