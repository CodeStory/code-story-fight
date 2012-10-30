package templating;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

public class TemplateTest {
  Template template;

  @Before
  public void setUp() {
    template = new Template();
  }

  @Test
  public void should_get_original_content() {
    String content = "<CONTENT WITHOUT TEMPLATING>";
    String result = template.apply(content, null);

    assertThat(result).isEqualTo(content);
  }

  @Test
  public void should_replace_variable() {
    String content = "BEFORE-$KEY$-VALUE";
    String result = template.apply(content, ImmutableMap.of("KEY", "VALUE"));

    assertThat(result).isEqualTo("BEFORE-VALUE-VALUE");
  }

  @Test
  public void should_include_version() throws IOException {
    String content = "$version$";
    String result = template.apply(content, null);

    assertThat(result).isEqualTo("GIT_HASH");
  }
}
