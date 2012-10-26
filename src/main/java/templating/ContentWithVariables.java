package templating;

import lombok.Data;

import java.util.Map;

@Data
public class ContentWithVariables {
  private final String content;
  private final Map<String, String> variables;
}
