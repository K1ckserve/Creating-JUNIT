package unittest.listeners;

import unittest.gui.TestGUI;
import unittest.results.TestMethodResult;

public interface TestListener {

    // Call this method right before the test method starts running
    public void testStarted(String className, String testMethod);

    // Call this method right after the test method finished running successfully
    public void testSucceeded(String className, TestMethodResult testMethodResult);

    // Call this method right after the test method finished running and failed
    public void testFailed(String className, TestMethodResult testMethodResult, TestGUI gui);
}
