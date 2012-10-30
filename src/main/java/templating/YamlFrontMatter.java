package templating;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class YamlFrontMatter {
  private static final String SEPARATOR = "---\n";

  public ContentWithVariables parse(String content) {
    if (StringUtils.countMatches(content, SEPARATOR) < 2) {
      return new ContentWithVariables(content, Maps.<String, String> newHashMap());
    }
    return new ContentWithVariables(stripHeader(content), parseVariables(content));
  }

  private static String stripHeader(String content) {
    return StringUtils.substringAfter(StringUtils.substringAfter(content, SEPARATOR), SEPARATOR);
  }

  private static Map<String, String> parseVariables(String content) {
    Map<String, String> variables = Maps.newHashMap();

    String header = StringUtils.substringBetween(content, SEPARATOR, SEPARATOR);
    for (String line : Splitter.on('\n').split(header)) {
      String key = StringUtils.substringBefore(line, ":").trim();
      String value = StringUtils.substringAfter(line, ":").trim();

      if (!key.startsWith("#")) {
        variables.put(key, value);
      }
    }

    return variables;
  }
}
