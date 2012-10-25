package templating;

import com.google.common.io.Resources;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

public class YamlFrontMatterTest {
  YamlFrontMatter yamlFrontMatter = new YamlFrontMatter();

  @Test
  public void should_read_empty_header_variables() throws IOException {
    Map<String, String> variables = yamlFrontMatter.parse(file("without_header.html")).getVariables();

    assertThat(variables).isEmpty();
  }

  @Test
  public void should_read_header_variables() throws IOException {
    Map<String, String> variables = yamlFrontMatter.parse(file("with_header.html")).getVariables();

    assertThat(variables).includes(
        entry("layout", "standard"),
        entry("title", "CodeStory - Devoxx Fight"));
  }

  @Test
  public void should_read_empty_file_content() throws IOException {
    String content = yamlFrontMatter.parse(file("empty.html")).getContent();

    assertThat(content).isEmpty();
  }

  @Test
  public void should_read_standard_file_content() throws IOException {
    String content = yamlFrontMatter.parse(file("without_header.html")).getContent();

    assertThat(content).isEqualTo("CONTENT");
  }

  @Test
  public void should_read_file_content() throws IOException {
    String content = yamlFrontMatter.parse(file("with_header.html")).getContent();

    assertThat(content).isEqualTo("CONTENT");
  }

  static File file(String path) {
    return new File(Resources.getResource(path).getFile());
  }
}
