import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CodeStoryResourceTest {

  @Mock
  private Planning planning;

  @Mock
  private Users users;

  @InjectMocks
  private CodeStoryResource resource;

  @Test
  public void should_register_user_for_talk() {
    resource.register("user", "talkId");

    verify(planning).register("user", "talkId");
  }

  @Test
  public void should_unregister_user_for_talk() {
    resource.unregister("user", "talkId");

    verify(planning).unregister("user", "talkId");
  }

  @Test
  public void should_get_my_registrations() {
    when(planning.registrations("user")).thenReturn(Arrays.asList("talkId1", "talkId2"));

    Iterable<String> talkIds = resource.myRegistrations("user");

    assertThat(talkIds).containsOnly("talkId1", "talkId2");
  }
}
