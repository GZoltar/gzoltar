package pt.up.fe.ddu.base.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.ddu.base.runtime.ProbeGroup.HitProbe;


class HitVector {
    private List<HitProbe> probes = new ArrayList<HitProbe> ();
    private Map<String, ProbeGroup> groups = new HashMap<String, ProbeGroup> ();


    public HitProbe registerProbe (String groupName,
                                   int nodeId) {
        ProbeGroup pg = groups.get(groupName);


        if (pg == null) {
            pg = new ProbeGroup();
            groups.put(groupName, pg);
        }

        HitProbe probe = pg.register(probes.size(), nodeId);
        probes.add(probe);

        return probe;
    }

    public final boolean exists (String groupName) {
        return groups.containsKey(groupName);
    }

    public final boolean[] get (String groupName) {
    	if (groups.get(groupName) == null) {
    		// registerProbe has not been called before for this groupName
    		// so we can return null
    		return null;
    	}
        return groups.get(groupName).get();
    }

    public final boolean[] get () {
        boolean[] ret = new boolean[probes.size()];
        int i = 0;

        for (HitProbe p : probes) {
            ret[i++] = p.getActivation();
        }

        return ret;
    }

    public final void hit (int globalId) {
        HitProbe p = probes.get(globalId);


        p.hit();
    }

    public final void reset () {
        for (Map.Entry<String, ProbeGroup> e : groups.entrySet()) {
            e.getValue().reset();
        }
    }

	public boolean existsHitVector(String groupName) {
		if (exists(groupName)) {
			ProbeGroup pg = groups.get(groupName);
			return pg.existsHitVector();
		}
		return false;
	}
}