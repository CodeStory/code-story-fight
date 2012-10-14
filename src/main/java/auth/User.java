package auth;

public class User {

  final Long id;
  final String screenName;
  final String token;
  final String secret;

  User(final Long id, final String screenName, final String token, final String secret) {
    this.id = id;
    this.screenName = screenName;
    this.token = token;
    this.secret = secret;
  }
}
