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

package com.gzoltar.core.test.junit5;

import java.net.URL;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;
import org.junit.platform.engine.TestExecutionResult;

import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;


import com.gzoltar.core.test.TestMethod;
import com.gzoltar.core.test.TestTask;
import com.gzoltar.core.util.IsolatingClassLoader;
import com.gzoltar.core.listeners.junit5.Listener;




public class JUnit5TestTask extends TestTask {
    
    public JUnit5TestTask(final URL[] searchPathURLs, final boolean offline,
    final boolean collectCoverage, final boolean initTestClass, final TestMethod testMethod) {
        super (searchPathURLs, offline, collectCoverage, initTestClass, testMethod);
    }

    /**
     * Callable method to run JUnit test and return result.
     * 
     * {@inheritDoc}
     */
    @Override
    public JUnit5TestResult call() throws Exception {
        // Create a new isolated classloader with the same classpath as the current one
        IsolatingClassLoader classLoader = new IsolatingClassLoader(this.searchPathURLs,
        Thread.currentThread().getContextClassLoader());

        // Make the isolating classloader the thread's new classloader. This method is called in a
        // dedicated thread that ends right after this method returns, so there is no need to restore
        // the old/original classloader when it finishes.
        Thread.currentThread().setContextClassLoader(classLoader);

        Class<?> clazz = this.initTestClass ? Class.forName(this.testMethod.getTestClassName())
        : Class.forName(this.testMethod.getTestClassName(), false, classLoader);

        LauncherDiscoveryRequestBuilder requestBuilder = LauncherDiscoveryRequestBuilder.request();
        
        requestBuilder.selectors(selectMethod(clazz, this.testMethod.getTestMethodName()));

        // text listener - TBD
        requestBuilder.listeners(new JUnit5TextListener());

        Listener listener;
        if (this.collectCoverage) {
            if (this.offline) {
                listener = this.initTestClass
                ? (Listener) Class.forName("com.gzoltar.core.listeners.junit5.Listener").newInstance()
                : (Listener) Class
                    .forName("com.gzoltar.core.listeners.junit5.Listener", false, classLoader)
                    .newInstance();
            } else {
                listener = new Listener();
            }
            requestBuilder.listeners(listener);
        }

        final LauncherDiscoveryRequest request = requestBuilder.build();
        final Launcher launcher = LauncherFactory.create();
        // dp we need this?
        //launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        // use TestExecutionResult or create a new JUnit5TestResult class?
        JUnit5TestResult result = new JUnit5TestResult(launcher.execute(request));
        classLoader.close();
        return result;
    }
}
