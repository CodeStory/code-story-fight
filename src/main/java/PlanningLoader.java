import com.google.gson.Gson;
import com.google.inject.Singleton;

import java.util.List;

@Singleton
public class PlanningLoader {
  public void createTalks(Planning planning, String json) {
    Sessions sessions = new Gson().fromJson(json, Sessions.class);
    for (Day day : sessions.days) {
      for (Slot slot : day.slots) {
        for (Talk talk : slot.talks) {
          planning.createTalk(talk.id, day.name + "-" + slot.start);
        }
      }
    }
  }

  public static class Sessions {
    public List<Day> days;
  }

  public static class Day {
    public String name;
    public List<Slot> slots;
  }

  public static class Slot {
    public String start;
    public List<Talk> talks;
  }

  public static class Talk {
    public String id;
  }
}
