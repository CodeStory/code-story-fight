package auth;

public class AuthenticationException extends RuntimeException {
  public AuthenticationException(Throwable cause) {
    super(cause);
  }
}
