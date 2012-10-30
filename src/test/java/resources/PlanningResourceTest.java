package resources;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import planning.Planning;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlanningResourceTest {
  @Mock
  private Planning planning;

  @InjectMocks
  private PlanningResource resource;

  @Test
  public void should_register_user_for_talk() {
    resource.star("user", "talkId");

    verify(planning).star("user", "talkId");
  }

  @Test
  public void should_unregister_user_for_talk() {
    resource.unstar("user", "talkId");

    verify(planning).unstar("user", "talkId");
  }

  @Test
  public void should_get_my_registrations() {
    when(planning.stars("user")).thenReturn(ImmutableSet.of("talkId1", "talkId2"));

    Iterable<String> talkIds = resource.stars("user");

    assertThat(talkIds).containsOnly("talkId1", "talkId2");
  }
}
