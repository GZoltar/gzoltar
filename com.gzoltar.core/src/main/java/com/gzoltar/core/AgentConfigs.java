package com.gzoltar.core;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javassist.Modifier;
import com.gzoltar.core.events.EventListener;
import com.gzoltar.core.instrumentation.FilterPass;
import com.gzoltar.core.instrumentation.InstrumentationPass;
import com.gzoltar.core.instrumentation.Pass;
import com.gzoltar.core.instrumentation.StackSizePass;
import com.gzoltar.core.instrumentation.TestFilterPass;
import com.gzoltar.core.instrumentation.granularity.GranularityFactory.GranularityLevel;
import com.gzoltar.core.instrumentation.matchers.BlackList;
import com.gzoltar.core.instrumentation.matchers.DuplicateCollectorReferenceMatcher;
import com.gzoltar.core.instrumentation.matchers.FieldNameMatcher;
import com.gzoltar.core.instrumentation.matchers.Matcher;
import com.gzoltar.core.instrumentation.matchers.ModifierMatcher;
import com.gzoltar.core.instrumentation.matchers.NotMatcher;
import com.gzoltar.core.instrumentation.matchers.OrMatcher;
import com.gzoltar.core.instrumentation.matchers.PrefixMatcher;
import com.gzoltar.core.instrumentation.matchers.SourceLocationMatcher;
import com.gzoltar.core.messaging.Client;
import com.gzoltar.core.model.Node.Type;
import flexjson.JSON;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class AgentConfigs {

  public static final String BUILD_LOCATION_KEY = "GZbuildLocationKey";

  private int port = -1;
  private GranularityLevel granularityLevel = GranularityLevel.method;
  private List<String> prefixesToFilter = null;
  private boolean filterTargetLocation = false;
  private boolean filterModifier = false;

  public void setPort(int port) {
    this.port = port;
  }

  public int getPort() {
    return port;
  }

  public GranularityLevel getGranularityLevel() {
    return granularityLevel;
  }

  public void setGranularityLevel(GranularityLevel granularityLevel) {
    this.granularityLevel = granularityLevel;
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

  public boolean getFilterModifier() {
    return filterModifier;
  }

  public void setFilterModifier(boolean filterModifier) {
    this.filterModifier = filterModifier;
  }

  @JSON(include = false)
  public List<Pass> getInstrumentationPasses() {
    List<Pass> instrumentationPasses = new ArrayList<Pass>();

    // Ignores classes in particular packages
    List<String> prefixes = new ArrayList<String>();
    Collections.addAll(prefixes, "javax.", "java.", "sun.", "com.sun.", "org.apache.maven",
        "com.gzoltar.");

    if (prefixesToFilter != null)
      prefixes.addAll(prefixesToFilter);

    String location = System.getProperty(BUILD_LOCATION_KEY, null);
    if (location != null) {
      try {
        File f = new File(location);
        location = f.toURI().toURL().getPath();
      } catch (MalformedURLException e) {
      }
    }

    if (filterTargetLocation && location != null) {
      SourceLocationMatcher slm = new SourceLocationMatcher(location);
      FilterPass locationFilter = new FilterPass(new BlackList(new NotMatcher(slm)));
      instrumentationPasses.add(locationFilter);
    } else {
      Collections.addAll(prefixes, "junit.", "org.junit.");
    }

    PrefixMatcher pMatcher = new PrefixMatcher(prefixes);

    Matcher mMatcher = new OrMatcher(new ModifierMatcher(Modifier.NATIVE),
        new ModifierMatcher(Modifier.INTERFACE));

    Matcher alreadyInstrumented =
        new OrMatcher(new FieldNameMatcher(InstrumentationPass.HIT_VECTOR_NAME),
            new DuplicateCollectorReferenceMatcher());

    FilterPass fp = new FilterPass(new BlackList(mMatcher), new BlackList(pMatcher),
        new BlackList(alreadyInstrumented));

    instrumentationPasses.add(fp);
    instrumentationPasses.add(new TestFilterPass());
    instrumentationPasses.add(new InstrumentationPass(granularityLevel, filterModifier));
    instrumentationPasses.add(new StackSizePass());

    return instrumentationPasses;
  }

  @JSON(include = false)
  public EventListener getEventListener() {
    if (getPort() != -1) {
      return new Client(getPort());
    } else {
      return new EventListener() {

        @Override
        public void endTransaction(String transactionName, boolean[] activity, int hashCode,
            boolean isError) {}

        @Override
        public void endTransaction(String transactionName, boolean[] activity, boolean isError) {}

        @Override
        public void endSession() {}

        @Override
        public void addProbe(int id, int nodeId) {}

        @Override
        public void addNode(int id, String name, Type type, int parentId) {}
      };
    }
  }

  public String serialize() {
    return new JSONSerializer().exclude("*.class").deepSerialize(this);
  }

  public static AgentConfigs deserialize(String str) {
    try {
      return new JSONDeserializer<AgentConfigs>().deserialize(str, AgentConfigs.class);
    } catch (Throwable t) {
      return null;
    }
  }
}
