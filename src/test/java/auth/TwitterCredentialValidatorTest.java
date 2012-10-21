package auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterCredentialValidatorTest {

  @Mock
  private Twitter twitter;

  @Mock
  private User user;

  @Spy
  private TwitterCredentialValidator credentialValidator;

  @Before
  public void stubTwitterInCredentialValidator() {
    when(credentialValidator.createTwitterWithUser(user)).thenReturn(twitter);
  }

  @Test
  public void should_restore_user() {
    boolean isAuthenticated = credentialValidator.isAuthenticated(user);

    assertThat(isAuthenticated).isTrue();
  }

  @Test
  public void should_not_restore_user() throws TwitterException {
    when(twitter.verifyCredentials()).thenThrow(new TwitterException("", null, 401));

    boolean isAuthenticated = credentialValidator.isAuthenticated(user);

    assertThat(isAuthenticated).isFalse();
  }
}