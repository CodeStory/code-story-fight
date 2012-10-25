package templating;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class LayoutTest {
  @Test
  public void should_apply_layout() {
    Layout layout = new Layout("header/$body$/footer");

    String content = layout.apply("content");

    assertThat(content).isEqualTo("header/content/footer");
  }
}
