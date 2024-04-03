package unittest.listeners;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import unittest.gui.TestGUI;
import unittest.results.TestMethodResult;

public class GUITestListener implements TestListener {

    @Override
    public void testStarted(String className, String testMethod) {

            //Send these texts to the gui using the append test method
            TestGUI.appendText("Test Started: " + className + " "+testMethod);

    }

    @Override
    public void testSucceeded(String className, TestMethodResult testMethodResult) {
            TestGUI.appendText("Test Succeeded: " +className+" " +testMethodResult.getName());
    }

    @Override
    public void testFailed(String className, TestMethodResult testMethodResult,TestGUI gui) {

            TestGUI.appendText("Test Failed: " + className +" "+testMethodResult.getName());
            // Assuming TestGUI can handle a request to show details
            gui.fails.put(className+"-"+ testMethodResult.getName(),testMethodResult.getException());

    }
}
