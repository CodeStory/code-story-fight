package templating;

import org.stringtemplate.v4.ST;

import java.util.Map;

public class Template {
  public String apply(String content, Map<String, String> variables) {
    if (null == variables) {
      return content;
    }

    return createTemplate(content, variables).render();
  }

  private ST createTemplate(String content, Map<String, String> variables) {
    ST template = new ST(content, '$', '$');

    for (Map.Entry<String, String> variable : variables.entrySet()) {
      template.add(variable.getKey(), variable.getValue());
    }
    template.add("body", "$body$");

    return template;
  }
}
