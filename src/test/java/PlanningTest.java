import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class PlanningTest {
  Planning planning = new Planning();

  @Before
  public void setUp() {
    planning.createSession("session01", 0, "TAG1", "TAG2");
    planning.createSession("session02", 1, "OTHER_TAG");
    planning.createSession("8h-salleB", 2, "TAGB");
    planning.createSession("8h-salleA", 2, "TAGA");
  }

  @Test
  public void should_read_registrations_for_empty_planning() {
    assertThat(planning.registrations("TAG1")).isZero();
    assertThat(planning.registrations("TAG2")).isZero();
    assertThat(planning.registrations("OTHER_TAG")).isZero();
  }

  @Test
  public void should_register() {
    planning.register("dgageot", "session01");

    assertThat(planning.registrations("TAG1")).isEqualTo(1);
    assertThat(planning.registrations("TAG2")).isEqualTo(1);
  }

  @Test
  public void should_count_registrations_per_tag() {
    planning.register("dgageot", "session01");
    planning.register("elemerdy", "session01");
    planning.register("dgageot", "session02");

    assertThat(planning.registrations("TAG1")).isEqualTo(2);
    assertThat(planning.registrations("TAG2")).isEqualTo(2);
    assertThat(planning.registrations("OTHER_TAG")).isEqualTo(1);
  }

  @Test
  public void should_register_only_once_per_session() {
    planning.register("dgageot", "session01");
    planning.register("dgageot", "session01");

    assertThat(planning.registrations("TAG1")).isEqualTo(1);
  }

  @Test
  public void should_register_only_once_per_user_per_slot() {
    planning.register("dgageot", "8h-salleB");
    planning.register("dgageot", "8h-salleA");
    planning.register("elemerdy", "8h-salleB");

    assertThat(planning.registrations("TAGA")).isEqualTo(1);
    assertThat(planning.registrations("TAGB")).isEqualTo(1);
  }

  @Test
  public void should_unregister() {
    planning.register("dgageot", "8h-salleA");
    planning.unregister("dgageot", "8h-salleA");

    assertThat(planning.registrations("TAGA")).isZero();
  }
}
