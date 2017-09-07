package com.gzoltar.core.runtime;

public final class ProbeGroup {
	
    public final class HitProbe {
        private final int id;
        private final int localId;
        private final int nodeId;

        private HitProbe (int globalId,
                          int localId,
                          int nodeId) {
            this.id = globalId;
            this.localId = localId;
            this.nodeId = nodeId;
        }

        public int getId () {
            return id;
        }

        public int getNodeId () {
            return nodeId;
        }

        public boolean getActivation () {
            if (hitVector == null)
                return false;

            return hitVector[localId];
        }

        public int getLocalId () {
            return localId;
        }

        public void hit () {
            assert hitVector != null;
            hitVector[localId] = true;
        }
    }

    private int size = 0;
    private boolean[] hitVector = null;

    ProbeGroup () {}

    public HitProbe register (int globalId,
                              int nodeId) {
        assert hitVector == null;
        return new HitProbe(globalId, size++, nodeId);
    }

    public boolean[] get () {
        if (hitVector == null)
            hitVector = new boolean[size];

        return hitVector;
    }

    public void reset () {
        if (hitVector == null)
            return;

        for (int j = 0; j < hitVector.length; j++) {
            hitVector[j] = false;
        }
    }

	public boolean existsHitVector() {
		return hitVector != null;
	}
}
