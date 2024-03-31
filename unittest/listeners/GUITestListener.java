package unittest.listeners;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import unittest.gui.TestGUI;
import unittest.results.TestMethodResult;

public class GUITestListener implements TestListener {

    @Override
    public void testStarted(String testMethod) {
        Platform.runLater(() -> {
            // Assuming TestGUI has a static method to append text to a TextArea or equivalent
            TestGUI.appendText("Test Started: " + testMethod);
        });
    }

    @Override
    public void testSucceeded(TestMethodResult testMethodResult) {
        Platform.runLater(() -> {
            TestGUI.appendText("Test Succeeded: " + testMethodResult.getName());
        });
    }

    @Override
    public void testFailed(TestMethodResult testMethodResult) {
        Platform.runLater(() -> {
            TestGUI.appendText("Test Failed: " + testMethodResult.getName() + ". Click 'View Details' for more info.");
            // Assuming TestGUI can handle a request to show details
            TestGUI.showDetailsButton(testMethodResult.getName(), testMethodResult.getException());
        });
    }
}
