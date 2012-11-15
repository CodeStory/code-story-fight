import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class TopFightsTest {


	@Test
	public void should_increment_keyword_counters() throws Exception {
		TopFights topFights = new TopFights();

		topFights.log("AngularJS", "Jabba The Hutt");
		TopFight topFight = topFights.get();

		assertThat(topFight.getLeft()).isEqualTo("AngularJS");
		assertThat(topFight.getRight()).isEqualTo("Jabba The Hutt");
	}

	@Test
	public void should_increment_twice_and_return_the_right_topFight() throws Exception {
		TopFights topFights = new TopFights();
		topFights.log("Polka", "Rock N Roll");
		topFights.log("JavaFX", "Hibernate");
		topFights.log("AngularJS", "Jabba The Hutt");
		topFights.log("AngularJS", "Jabba The Hutt");
		topFights.log("AngularJS", "Jabba The Hutt");// <-- we did that 3 times, right.
		topFights.log("JavaFX", "Hibernate");

		TopFight topFight = topFights.get();

		assertThat(topFight.getLeft()).isEqualTo("AngularJS");
		assertThat(topFight.getRight()).isEqualTo("Jabba The Hutt");
	}
}
