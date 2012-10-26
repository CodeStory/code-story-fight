package templating;

public class Layout {
  private final String layout;

  public Layout(String layout) {
    this.layout = layout;
  }

  public String apply(String body) {
    return layout.replace("$body$", body);
  }
}
