package unittest.listeners;

import unittest.results.TestMethodResult;

public class GUITestListener implements TestListener {

    // Call this method right before the test method starts running
    @Override
    public void testStarted(String testMethod) {
    }

    // Call this method right after the test method finished running successfully
    @Override
    public void testSucceeded(TestMethodResult testMethodResult) {
    }

    // Call this method right after the test method finished running and failed
    @Override
    public void testFailed(TestMethodResult testMethodResult) {
    }
}
