package com.gzoltar.core.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.gzoltar.core.runtime.ProbeGroup.HitProbe;

public class HitVector {

  private final List<HitProbe> probes = new ArrayList<HitProbe>();

  private final Map<String, ProbeGroup> groups = new HashMap<String, ProbeGroup>();

  public HitProbe registerProbe(final String groupName, final int nodeId) {
    ProbeGroup pg = this.groups.get(groupName);

    if (pg == null) {
      pg = new ProbeGroup();
      this.groups.put(groupName, pg);
    }

    HitProbe probe = pg.register(this.probes.size(), nodeId);
    this.probes.add(probe);

    return probe;
  }

  public final boolean exists(final String groupName) {
    return this.groups.containsKey(groupName);
  }

  public final boolean[] get(final String groupName) {
    if (this.groups.get(groupName) == null) {
      // registerProbe has not been called before for this groupName
      // so we can return null
      return null;
    }
    return this.groups.get(groupName).get();
  }

  public final boolean[] get() {
    boolean[] ret = new boolean[this.probes.size()];
    int i = 0;

    for (HitProbe p : this.probes) {
      ret[i++] = p.getActivation();
    }

    return ret;
  }

  public final void hit(final int globalId) {
    HitProbe p = this.probes.get(globalId);
    p.hit();
  }

  public final void reset() {
    for (Map.Entry<String, ProbeGroup> e : this.groups.entrySet()) {
      e.getValue().reset();
    }
  }

  public boolean existsHitVector(final String groupName) {
    if (exists(groupName)) {
      ProbeGroup pg = this.groups.get(groupName);
      return pg.existsHitVector();
    }
    return false;
  }

}
