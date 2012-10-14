package auth;

import lombok.Data;

@Data
public class User {
  final Long id;
  final String screenName;
  final String token;
  final String secret;
}
