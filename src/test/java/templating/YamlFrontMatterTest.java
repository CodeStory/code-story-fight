package templating;

import com.google.common.base.Joiner;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

public class YamlFrontMatterTest {
  YamlFrontMatter yamlFrontMatter = new YamlFrontMatter();

  @Test
  public void should_read_empty_file() {
    String content = fileContent("");

    ContentWithVariables parsed = yamlFrontMatter.parse(content);

    assertThat(parsed.getVariables()).isEmpty();
    assertThat(parsed.getContent()).isEmpty();
  }

  @Test
  public void should_read_file_without_headers() {
    String content = fileContent("CONTENT");

    ContentWithVariables parsed = yamlFrontMatter.parse(content);

    assertThat(parsed.getVariables()).isEmpty();
    assertThat(parsed.getContent()).isEqualTo("CONTENT");
  }

  @Test
  public void should_read_header_variables() {
    String content = fileContent(
        "---",
        "layout: standard",
        "title: CodeStory - Devoxx Fight",
        "---",
        "CONTENT");

    ContentWithVariables parsed = yamlFrontMatter.parse(content);

    assertThat(parsed.getVariables()).includes(
        entry("layout", "standard"),
        entry("title", "CodeStory - Devoxx Fight"));
    assertThat(parsed.getContent()).isEqualTo("CONTENT");
  }

  @Test
  public void should_ignore_commented_variable() {
    String content = fileContent(
        "---",
        "#layout: standard",
        "title: CodeStory - Devoxx Fight",
        "---",
        "CONTENT");

    ContentWithVariables parsed = yamlFrontMatter.parse(content);

    assertThat(parsed.getVariables())
        .excludes(entry("layout", "standard"))
        .excludes(entry("#layout", "standard"))
        .includes(entry("title", "CodeStory - Devoxx Fight"));
  }

  static String fileContent(String... lines) {
    return Joiner.on("\n").join(lines);
  }
}
