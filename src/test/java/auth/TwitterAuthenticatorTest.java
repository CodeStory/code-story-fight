package auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest( { TwitterFactory.class, RequestToken.class })
public class TwitterAuthenticatorTest {

  @Mock
  TwitterFactory twitterFactory;

  @Mock
  Twitter twitter;

  @InjectMocks
  TwitterAuthenticator authenticator;

  @Test
  public void should_generate_an_authentication_URL() throws MalformedURLException, AuthenticationException, TwitterException {
    RequestToken requestToken = mock(RequestToken.class);
    when(twitterFactory.getInstance()).thenReturn(twitter);
    when(twitter.getOAuthRequestToken(anyString())).thenReturn(requestToken);
    when(requestToken.getAuthenticationURL()).thenReturn("http://api.twitter.com/oauth/authenticate?oauth_token=fake");

    URL authenticateURL = authenticator.getAuthenticateURL();

    assertThat(authenticateURL).isEqualTo(URI.create("http://api.twitter.com/oauth/authenticate?oauth_token=fake").toURL());
  }

  @Test(expected = AuthenticationException.class)
  public void should_raise_exception_when_authentication_fails() throws MalformedURLException, TwitterException {
    when(twitterFactory.getInstance()).thenReturn(twitter);
    when(twitter.getOAuthAccessToken(any(RequestToken.class), eq("bar"))).thenThrow(new TwitterException("", null, 401));

    authenticator.authenticate("", "bar");
  }

  @Test
  public void should_authenticate() throws MalformedURLException, TwitterException {
    AccessToken accessToken = mock(AccessToken.class);
    when(twitterFactory.getInstance()).thenReturn(twitter);
    when(twitter.getOAuthAccessToken(any(RequestToken.class), eq("foo"))).thenReturn(accessToken);

    User user = authenticator.authenticate("", "foo");

    assertThat(user).isNotNull();
    verify(accessToken).getUserId();
    verify(accessToken).getScreenName();
    verify(accessToken).getToken();
    verify(accessToken).getTokenSecret();
  }

}
