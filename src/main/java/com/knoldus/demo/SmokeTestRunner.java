package com.knoldus.demo;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;
import static org.junit.platform.launcher.TagFilter.includeTags;

public class SmokeTestRunner {

    private final static Logger logger = LoggerFactory.getLogger(SmokeTestRunner.class);

    public static void main(String[] args) {

        String pkgName = SmokeTestRunner.class.getPackage().getName();
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                                                   .request()
                                                   .selectors(selectPackage(pkgName))
                                                   .filters(includeTags("smoke"))
                                                   .configurationParameter("junit.jupiter.execution.parallel.enabled",
                                                                           "true")
                                                   .configurationParameter(
                                                           "junit.jupiter.extensions.autodetection.enabled",
                                                           "true")
                                                   .build();

        Launcher launcher = LauncherFactory.create();

        launcher.discover(request);
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();

        logger.info("Found {} smoke tests: {} success, {} failures, {} aborts, {} skips",
                    summary.getTestsFoundCount(), summary.getTestsSucceededCount(), summary.getTestsFailedCount(),
                    summary.getTestsAbortedCount(), summary.getTestsAbortedCount());

        if (summary.getTestsFailedCount() > 0) {
            summary.printFailuresTo(new PrintWriter(System.out), 3);
            fail("Smoketest Failed !!");
        }
    }
}