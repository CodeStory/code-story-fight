package planning;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

public class PlanningTest {
  Planning planning;
  File root;

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Before
  public void setUp() throws IOException {
    root = temporaryFolder.newFolder();
    planning = new Planning(root);
  }

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

  @Test
  public void should_load_from_files() throws IOException {
    file(root, "dgageot", "+talk01", "+talk02", "+talk03", "-talk03");
    file(root, "elemerdy", "+talk05");

    planning = new Planning(root);

    assertThat(planning.stars("dgageot")).containsOnly("talk01", "talk02");
    assertThat(planning.stars("elemerdy")).containsOnly("talk05");
  }

  @Test
  public void should_save_stars_to_disk() throws IOException {
    planning.star("dgageot", "talk01");
    planning.star("dgageot", "talk02");
    planning.star("elemerdy", "talk01");

    Planning otherPlanning = new Planning(root);

    assertThat(otherPlanning.stars("dgageot")).containsOnly("talk01", "talk02");
    assertThat(otherPlanning.stars("elemerdy")).containsOnly("talk01");
  }

  @Test
  public void should_count_stars_per_talk() {
    planning.star("dgageot", "talk01");
    planning.star("dgageot", "talk02");
    planning.star("elemerdy", "talk01");

    Map<String, Long> countPerTalk = planning.countPerTalk();

    assertThat(countPerTalk).includes(entry("talk01", 2L), entry("talk02", 1L));
  }

  @Test
  public void should_count_stars_loaded_from_file() throws IOException {
    file(root, "dgageot", "+talk01", "+talk02", "+talk03", "-talk03");

    planning = new Planning(root);
    Map<String, Long> countPerTalk = planning.countPerTalk();

    assertThat(countPerTalk).includes(entry("talk01", 1L), entry("talk02", 1L));
  }

  @Test
  public void should_count_stars_for_empty_planning() {
    Map<String, Long> countPerTalk = planning.countPerTalk();

    assertThat(countPerTalk).isEmpty();
  }

  static void file(File root, String login, String... content) throws IOException {
    File userFile = new File(root, login);
    Files.write(Joiner.on("\n").join(content), userFile, Charsets.UTF_8);
  }
}
