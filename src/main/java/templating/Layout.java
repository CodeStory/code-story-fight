package templating;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Layout {
  private final String layout;

  public String apply(String body) {
    return layout.replace("$body$", body);
  }
}
