package auth;

import lombok.Data;

@Data
public class User {
  private final Long id;
  private final String screenName;
  private final String token;
  private final String secret;
}
