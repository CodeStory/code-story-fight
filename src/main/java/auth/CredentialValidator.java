package auth;

public interface CredentialValidator {
  boolean isAuthenticated(User user);
}
