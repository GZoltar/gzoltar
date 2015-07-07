package pt.up.fe.aes.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javassist.Modifier;
import pt.up.fe.aes.base.events.EventListener;
import pt.up.fe.aes.base.instrumentation.FilterPass;
import pt.up.fe.aes.base.instrumentation.InstrumentationPass;
import pt.up.fe.aes.base.instrumentation.Pass;
import pt.up.fe.aes.base.instrumentation.StackSizePass;
import pt.up.fe.aes.base.instrumentation.TestFilterPass;
import pt.up.fe.aes.base.instrumentation.granularity.GranularityFactory.GranularityLevel;
import pt.up.fe.aes.base.instrumentation.matchers.BlackList;
import pt.up.fe.aes.base.instrumentation.matchers.ClassNamesMatcher;
import pt.up.fe.aes.base.instrumentation.matchers.FieldNameMatcher;
import pt.up.fe.aes.base.instrumentation.matchers.Matcher;
import pt.up.fe.aes.base.instrumentation.matchers.ModifierMatcher;
import pt.up.fe.aes.base.instrumentation.matchers.NotMatcher;
import pt.up.fe.aes.base.instrumentation.matchers.OrMatcher;
import pt.up.fe.aes.base.instrumentation.matchers.PrefixMatcher;
import pt.up.fe.aes.base.instrumentation.matchers.SourceLocationMatcher;
import pt.up.fe.aes.base.messaging.Client;
import flexjson.JSON;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class AgentConfigs {

	public static final String BUILD_LOCATION_KEY = "AESbuildLocationKey";
	
	private int port = 1234;
	private GranularityLevel granularityLevel = GranularityLevel.method;
	private List<String> classesToInstrument = null;
	private List<String> prefixesToFilter = null;
	private boolean filterTargetLocation = false;

	public void setPort (int port) {
        this.port = port;
    }

    public int getPort () {
        return port;
    }
    
    public GranularityLevel getGranularityLevel() {
		return granularityLevel;
	}

	public void setGranularityLevel(GranularityLevel granularityLevel) {
		this.granularityLevel = granularityLevel;
	}
	
	public List<String> getClassesToInstrument() {
		return classesToInstrument;
	}

	public void setClassesToInstrument(List<String> classesToInstrument) {
		this.classesToInstrument = classesToInstrument;
	}
	
	public List<String> getPrefixesToFilter() {
		return prefixesToFilter;
	}

	public void setPrefixesToFilter(List<String> prefixesToFilter) {
		this.prefixesToFilter = prefixesToFilter;
	}
	
	public boolean getFilterTargetLocation() {
		return filterTargetLocation;
	}

	public void setFilterTargetLocation(boolean filterTargetLocation) {
		this.filterTargetLocation = filterTargetLocation;
	}

    @JSON(include = false)
	public List<Pass> getInstrumentationPasses() {
		List<Pass> instrumentationPasses = new ArrayList<Pass>();
		
		// Whitelist classes to instrument if they exist
		if (classesToInstrument != null && !classesToInstrument.isEmpty()) {
			ClassNamesMatcher cnm = new ClassNamesMatcher(classesToInstrument);
			FilterPass classesFilter = new FilterPass(new BlackList(new NotMatcher(cnm)));
			instrumentationPasses.add(classesFilter);
		}
		
		// Ignores classes in particular packages
		List<String> prefixes = new ArrayList<String> ();
        Collections.addAll(prefixes, "javax.", "java.", "sun.", "com.sun.", 
        		"org.apache.maven", "pt.up.fe.aes.");
        
        if (prefixesToFilter != null)
        	prefixes.addAll(prefixesToFilter);
        
        String location = System.getProperty(BUILD_LOCATION_KEY, null);
        
        if (filterTargetLocation && location != null) {
        	SourceLocationMatcher slm = new SourceLocationMatcher(location);
        	FilterPass locationFilter = new FilterPass(new BlackList(new NotMatcher(slm)));
        	instrumentationPasses.add(locationFilter);
        }
        else {
        	Collections.addAll(prefixes, "junit.", "org.junit.");
        }

        PrefixMatcher pMatcher = new PrefixMatcher(prefixes);

        Matcher mMatcher = new OrMatcher(new ModifierMatcher(Modifier.NATIVE),
                                         new ModifierMatcher(Modifier.INTERFACE));

        Matcher alreadyInstrumented = new FieldNameMatcher(InstrumentationPass.HIT_VECTOR_NAME);

        FilterPass fp = new FilterPass(new BlackList(mMatcher), 
        							   new BlackList(pMatcher),
        							   new BlackList(alreadyInstrumented));
		
		instrumentationPasses.add(fp);
		instrumentationPasses.add(new TestFilterPass());
		instrumentationPasses.add(new InstrumentationPass(granularityLevel));
		instrumentationPasses.add(new StackSizePass());
		
		return instrumentationPasses;
	}

    @JSON(include = false)
	public EventListener getEventListener() {
		return new Client(getPort());
	}
	
	public String serialize () {
        return new JSONSerializer().exclude("*.class").deepSerialize(this);
    }

    public static AgentConfigs deserialize (String str) {
        try {
            return new JSONDeserializer<AgentConfigs> ().deserialize(str, AgentConfigs.class);
        }
        catch (Throwable t) {
            return null;
        }
    }
}
