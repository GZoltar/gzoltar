package com.gzoltar.core.runtime;

public class ProbeGroup {

  public final class HitProbe {

    private final int id;

    private final int localId;

    private final int nodeId;

    private HitProbe(final int globalId, final int localId, final int nodeId) {
      this.id = globalId;
      this.localId = localId;
      this.nodeId = nodeId;
    }

    public int getId() {
      return this.id;
    }

    public int getNodeId() {
      return this.nodeId;
    }

    public boolean getActivation() {
      if (hitVector == null) {
        return false;
      }

      return hitVector[this.localId];
    }

    public int getLocalId() {
      return this.localId;
    }

    public void hit() {
      assert hitVector != null;
      hitVector[this.localId] = true;
    }
  }

  private int size = 0;

  private boolean[] hitVector = null;

  public HitProbe register(final int globalId, final int nodeId) {
    assert this.hitVector == null;
    return new HitProbe(globalId, this.size++, nodeId);
  }

  public boolean[] get() {
    if (this.hitVector == null) {
      this.hitVector = new boolean[size];
    }
    return this.hitVector;
  }

  public void reset() {
    if (this.hitVector == null) {
      return;
    }

    for (int j = 0; j < this.hitVector.length; j++) {
      this.hitVector[j] = false;
    }
  }

  public boolean existsHitVector() {
    return this.hitVector != null;
  }

}
