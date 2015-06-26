package pt.up.fe.aes.base;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.Modifier;
import pt.up.fe.aes.base.instrumentation.FilterPass;
import pt.up.fe.aes.base.instrumentation.InstrumentationPass;
import pt.up.fe.aes.base.instrumentation.Pass;
import pt.up.fe.aes.base.instrumentation.StackSizePass;
import pt.up.fe.aes.base.instrumentation.TestFilterPass;
import pt.up.fe.aes.base.instrumentation.granularity.GranularityFactory.GranularityLevel;
import pt.up.fe.aes.base.instrumentation.matchers.BlackList;
import pt.up.fe.aes.base.instrumentation.matchers.FieldNameMatcher;
import pt.up.fe.aes.base.instrumentation.matchers.Matcher;
import pt.up.fe.aes.base.instrumentation.matchers.ModifierMatcher;
import pt.up.fe.aes.base.instrumentation.matchers.OrMatcher;
import pt.up.fe.aes.base.instrumentation.matchers.PrefixMatcher;
import pt.up.fe.aes.base.messaging.Client;
import pt.up.fe.aes.base.runtime.Collector;

public class Agent implements ClassFileTransformer {

	private static List<Pass> instrumentationPasses = new ArrayList<Pass>();
	
	public static void premain(String agentArgs, Instrumentation inst) {

		int port = Integer.parseInt(agentArgs);

		System.out.println("Running premain. Agent args are: " + port);

		Collector.start(new Client(port));
		
		List<String> prefixes = new ArrayList<String> ();
        Collections.addAll(prefixes, "javax.", "java.", "sun.", "com.sun.", 
        		"junit.", "org.junit.", "org.apache.maven", "pt.up.fe.aes.");

        // Ignores classes in particular packages
        PrefixMatcher pMatcher = new PrefixMatcher(prefixes);

        Matcher mMatcher = new OrMatcher(new ModifierMatcher(Modifier.NATIVE),
                                         new ModifierMatcher(Modifier.INTERFACE));

        Matcher alreadyInstrumented = new FieldNameMatcher(InstrumentationPass.HIT_VECTOR_NAME);

        FilterPass fp = new FilterPass(new BlackList(mMatcher), 
        							   new BlackList(pMatcher),
        							   new BlackList(alreadyInstrumented));
		
		instrumentationPasses.add(fp);
		instrumentationPasses.add(new TestFilterPass());
		instrumentationPasses.add(new InstrumentationPass(GranularityLevel.LINE));
		instrumentationPasses.add(new StackSizePass());
		
		Agent a = new Agent();
		inst.addTransformer(a);
	}

	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {

		if (loader == null)
			return null;

		CtClass c = null;
		ClassPool cp = null;
		byte[] ret = null;

		try {
			cp = ClassPool.getDefault();
			c = cp.makeClass(new java.io.ByteArrayInputStream(classfileBuffer));
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		try {
			cp.importPackage("pt.up.fe.aes.base.runtime");

			for (Pass p : instrumentationPasses) {
				switch (p.transform(c)) {
				case CANCEL:
					c.detach();
					return null;
					
				case CONTINUE: continue;
				case FINISH:
				default: break;
				}
			}

			ret = c.toBytecode();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		c.detach();
		return ret;
	}

}
