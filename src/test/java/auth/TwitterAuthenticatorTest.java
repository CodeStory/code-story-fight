package auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterAuthenticatorTest {

  @Mock
  Twitter twitter;

  @InjectMocks
  TwitterAuthenticator authenticator;

  @Test
  public void should_generate_an_authentication_URL() throws MalformedURLException, AuthenticationException, TwitterException {
    when(twitter.getOAuthRequestToken()).thenReturn(new RequestToken("fake", "secret"));

    URL authenticateURL = authenticator.getAuthenticateURL();

    assertThat(authenticateURL).isEqualTo(URI.create("http://api.twitter.com/oauth/authenticate?oauth_token=fake").toURL());
  }

  @Test(expected = AuthenticationException.class)
  public void should_raise_exception_when_authentication_fails() throws MalformedURLException, TwitterException {
    when(twitter.getOAuthAccessToken("bar")).thenThrow(new TwitterException("", null, 401));

    authenticator.authenticate("bar");
  }

  @Test
  public void should_authenticate() throws MalformedURLException, TwitterException {
    AccessToken accessToken = mock(AccessToken.class);
    when(twitter.getOAuthAccessToken("foo")).thenReturn(accessToken);

    User user = authenticator.authenticate("foo");

    assertThat(user).isNotNull();
    verify(accessToken).getUserId();
    verify(accessToken).getScreenName();
    verify(accessToken).getToken();
    verify(accessToken).getTokenSecret();
  }

}
