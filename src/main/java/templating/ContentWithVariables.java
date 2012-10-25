package templating;

import java.util.Map;

public class ContentWithVariables {
  private final String content;
  private final Map<String, String> variables;

  public ContentWithVariables(String content, Map<String, String> variables) {
    this.content = content;
    this.variables = variables;
  }

  public String getContent() {
    return content;
  }

  public Map<String, String> getVariables() {
    return variables;
  }
}
