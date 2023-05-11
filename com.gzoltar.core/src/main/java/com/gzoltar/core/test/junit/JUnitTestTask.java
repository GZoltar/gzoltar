/**
 * Copyright (C) 2020 GZoltar contributors.
 *
 * This file is part of GZoltar.
 *
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */

package com.gzoltar.core.test.junit;

import com.gzoltar.core.listeners.JUnitListener;
import com.gzoltar.core.listeners.junit5.Listener;
import com.gzoltar.core.test.TestMethod;
import com.gzoltar.core.test.TestTask;
import com.gzoltar.core.util.IsolatingClassLoader;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.DiscoveryFilter;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.vintage.engine.descriptor.VintageEngineDescriptor;

import java.net.URL;
import java.util.Set;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;

public class JUnitTestTask extends TestTask implements TestExecutionListener{
    
    public JUnitTestTask(final URL[] searchPathURLs, final boolean offline,
                         final boolean collectCoverage, final boolean initTestClass, final TestMethod testMethod) {
        super (searchPathURLs, offline, collectCoverage, initTestClass, testMethod);
    }

    /**
     * Callable method to run JUnit test and return result.
     * 
     * {@inheritDoc}
     */
    @Override
    public JUnitTestResult call() throws Exception {
        // Create a new isolated classloader with the same classpath as the current one
        IsolatingClassLoader classLoader = new IsolatingClassLoader(this.searchPathURLs,
        Thread.currentThread().getContextClassLoader());

        // Make the isolating classloader the thread's new classloader. This method is called in a
        // dedicated thread that ends right after this method returns, so there is no need to restore
        // the old/original classloader when it finishes.
        Thread.currentThread().setContextClassLoader(classLoader);

        Class<?> clazz = this.initTestClass ? Class.forName(this.testMethod.getTestClassName())
                : Class.forName(this.testMethod.getTestClassName(), false, classLoader);

        System.out.println(testMethod.getTestMethodName() + testMethod.getTestClassName());
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(
                    selectMethod(testMethod.getTestClassName() + "#" + testMethod.getTestMethodName())
                ).filters(EngineFilter.excludeEngines("junit-vintage"))
                .build();
        //requestBuilder.listeners(new JUnit5TextListener());

        Listener listener = new Listener();
        /*
        if (this.collectCoverage) {
            if (this.offline) {
                listener = this.initTestClass
                ? (Listener) Class.forName("com.gzoltar.core.listeners.Listener").newInstance()
                : (Listener) Class
                    .forName("com.gzoltar.core.listeners.Listener", false, classLoader)
                    .newInstance();
            } else {
                listener = new Listener();
            }
            //requestBuilder.listeners(listener);
        }
        */

        try (LauncherSession session = LauncherFactory.openSession()) {
            Launcher launcher = session.getLauncher();
            // Register a listener of your choice
            launcher.registerTestExecutionListeners(listener);
            // Discover tests and build a test plan
            TestPlan testPlan = launcher.discover(request);

            getTests(testPlan,testPlan.getRoots());
            // Execute test plan
            launcher.execute(testPlan);

        }

        TestExecutionSummary summary = listener.getSummary();
        JUnitTestResult result = new JUnitTestResult(summary);

        classLoader.close();
        return result;
    }

    public static void getTests(TestPlan testPlan, Set<TestIdentifier> roots){
        for (TestIdentifier test: roots){
            if (test.isTest()){
                System.out.println("\t" + test.getUniqueId());
            }else{
                System.out.println("\t" + test.getUniqueId());
                getTests(testPlan,testPlan.getChildren(test));
            }
        }
    }
}
