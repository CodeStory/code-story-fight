import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import com.google.inject.Singleton;
import lombok.Data;

import javax.annotation.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static net.gageot.listmaker.ListMaker.whereEquals;

@Singleton
public class Planning {
  private final SetMultimap<String, Talk> talksPerUser = HashMultimap.create();
  private final Map<String, Talk> talkPerId = Maps.newHashMap();

  public Iterable<String> registrations(String login) {
    List<String> talkdIds = Lists.newArrayList();
    for (Talk talk : talksPerUser.get(login)) {
      talkdIds.add(talk.getId());
    }
    return talkdIds;
  }

  public void register(String login, String talkId) {
    Talk talk = talkPerId.get(talkId);
    if (null == talk) {
      return;
    }

    Iterator<Talk> talks = talksPerUser.get(login).iterator();
    while (talks.hasNext()) {
      if (talk.intersects(talks.next())) {
        talks.remove();
      }
    }

    talksPerUser.put(login, talk);
  }

  public void unregister(String login, String talkId) {
    Iterables.removeIf(talksPerUser.get(login), whereEquals(Talk.TO_ID, talkId));
  }

  public void createTalk(String talkId, String slot) {
    talkPerId.put(talkId, new Talk(talkId, slot));
  }

  @Data
  static class Talk {
    final String id;
    final String slot;

    boolean intersects(Talk other) {
      return slot.equals(other.slot);
    }

    static Function<Talk, String> TO_ID = new Function<Talk, String>() {
      @Override
      public String apply(@Nullable Talk talk) {
        return talk.getId();
      }
    };
  }
}
