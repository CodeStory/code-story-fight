package auth;

public class User {
  private final Long id;
  private final String screenName;
  private final String token;
  private final String secret;

  public User(Long id, String screenName, String token, String secret) {
    this.id = id;
    this.screenName = screenName;
    this.token = token;
    this.secret = secret;
  }

  public Long getId() {
    return id;
  }

  public String getScreenName() {
    return screenName;
  }

  public String getToken() {
    return token;
  }

  public String getSecret() {
    return secret;
  }
}
