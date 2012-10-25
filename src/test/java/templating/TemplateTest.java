package templating;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class TemplateTest {
  @Test
  public void should_get_original_content() {
    String content = "<CONTENT WITHOUT TEMPLATING>";
    String result = new Template().apply(content, null);

    assertThat(result).isEqualTo(content);
  }

  @Test
  public void should_replace_variable() {
    String content = "BEFORE-$KEY$-VALUE";
    String result = new Template().apply(content, ImmutableMap.of("KEY", "VALUE"));

    assertThat(result).isEqualTo("BEFORE-VALUE-VALUE");
  }
}
