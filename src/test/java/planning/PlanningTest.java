package planning;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class PlanningTest {
  Planning planning = new Planning();

  @Test
  public void should_read_stars_for_empty_planning() {
    assertThat(planning.stars("dgageot")).isEmpty();
  }

  @Test
  public void should_star() {
    planning.star("dgageot", "talk01");

    assertThat(planning.stars("dgageot")).containsOnly("talk01");
  }

  @Test
  public void should_star_multiple_users() {
    planning.star("dgageot", "talk01");
    planning.star("dgageot", "talk02");
    planning.star("elemerdy", "talk01");

    assertThat(planning.stars("dgageot")).containsOnly("talk01", "talk02");
    assertThat(planning.stars("elemerdy")).containsOnly("talk01");
  }

  @Test
  public void should_unstar() {
    planning.star("dgageot", "talk01");
    planning.unstar("dgageot", "talk01");

    assertThat(planning.stars("dgageot")).isEmpty();
  }
}
