import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class TopFightsTest {
	private TopFights topFights;

	@Before
	public void init() {
		topFights = new TopFights(false);
	}

	@Test
	public void should_increment_keyword_counters() throws Exception {
		topFights.log("AngularJS", "Jabba The Hutt");
		TopFight topFight = topFights.first();

		assertThat(topFight.getLeft()).isEqualTo("AngularJS");
		assertThat(topFight.getRight()).isEqualTo("Jabba The Hutt");
	}

	@Test
	public void should_increment_twice_and_return_the_right_topFight() throws Exception {
		topFights.log("Polka", "Rock N Roll");
		topFights.log("JavaFX", "Hibernate");
		topFights.log("AngularJS", "Jabba The Hutt");
		topFights.log("AngularJS", "Jabba The Hutt");
		topFights.log("AngularJS", "Jabba The Hutt");// <-- we did that 3 times, right.
		topFights.log("JavaFX", "Hibernate");

		TopFight topFight = topFights.first();

		assertThat(topFight.getLeft()).isEqualTo("AngularJS");
		assertThat(topFight.getRight()).isEqualTo("Jabba The Hutt");
	}

	@Test
	public void should_save_and_read() throws Exception {
		topFights.log("Polka", "Rock N Roll");
		topFights.saveOnDisk();

		TopFights topFights2 = new TopFights();

		TopFight topFight = topFights2.first();

		assertThat(topFight.getLeft()).isEqualTo("Polka");
		assertThat(topFight.getRight()).isEqualTo("Rock N Roll");

	}
}
