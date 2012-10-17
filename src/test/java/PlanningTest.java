import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class PlanningTest {
  Planning planning = new Planning();

  @Before
  public void setUp() {
    planning.createTalk("talk01", "tuesday-8h");
    planning.createTalk("talk02", "tuesday-9h");
    planning.createTalk("talk01-monday-8h", "monday-8h");
    planning.createTalk("talk02-monday-8h", "monday-8h");
  }

  @Test
  public void should_read_registrations_for_empty_planning() {
    assertThat(planning.registrations("login")).isEmpty();
  }

  @Test
  public void should_register() {
    planning.register("dgageot", "talk01");

    assertThat(planning.registrations("dgageot")).containsOnly("talk01");
  }

  @Test
  public void should_register_multiple_users() {
    planning.register("dgageot", "talk01");
    planning.register("dgageot", "talk02");
    planning.register("elemerdy", "talk01");

    assertThat(planning.registrations("dgageot")).containsOnly("talk01", "talk02");
    assertThat(planning.registrations("elemerdy")).containsOnly("talk01");
  }

  @Test
  public void should_unregister() {
    planning.register("dgageot", "talk01");
    planning.unregister("dgageot", "talk01");

    assertThat(planning.registrations("dgageot")).isEmpty();
  }

  @Test
  public void should_register_only_once_per_slot() {
    planning.register("dgageot", "talk01-monday-8h");
    planning.register("dgageot", "talk02-monday-8h");
    planning.register("elemerdy", "talk01-monday-8h");

    assertThat(planning.registrations("dgageot")).containsOnly("talk02-monday-8h");
    assertThat(planning.registrations("elemerdy")).containsOnly("talk01-monday-8h");
  }
}
