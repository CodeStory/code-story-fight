import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PlanningLoaderTest {
  PlanningLoader loader = new PlanningLoader();

  @Mock
  Planning planning;

  @Test
  public void should_create_talks() {
    String json = "{\"days\":[{\"name\":\"DAY\",\"slots\":[{\"id\":\"0\",\"start\":\"SLOT\",\"talks\":[{\"id\":\"TALK\"}]}]}]}";

    loader.createTalks(planning, json);

    verify(planning).createTalk("TALK", "DAY-SLOT");
  }
}
