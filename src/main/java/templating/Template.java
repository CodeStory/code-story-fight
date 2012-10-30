package templating;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.stringtemplate.v4.ST;

import java.io.IOException;
import java.util.Map;

public class Template {
  public String apply(String content, Map<String, String> variables) {
    return createTemplate(content, variables).render();
  }

  private ST createTemplate(String content, Map<String, String> variables) {
    ST template = new ST(content, '$', '$');

    if (null != variables) {
      for (Map.Entry<String, String> variable : variables.entrySet()) {
        template.add(variable.getKey(), variable.getValue());
      }
    }
    template.add("body", "$body$");
    template.add("version", "version-" + readGitHash());

    return template;
  }

  private static String readGitHash() {
    try {
      String hash = Resources.toString(Resources.getResource("version.txt"), Charsets.UTF_8);
      return hash.replace("$Format:%H$", "");
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read version.txt in the classpath");
    }
  }
}
