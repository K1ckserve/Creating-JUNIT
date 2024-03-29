package unittest.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import unittest.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestGUI extends Application {

    // Assuming these are your test classes
    private List<Class<?>> getTestClasses() {
        List<Class<?>> classes = new ArrayList<>();
        // Dynamically adding classes would require a classpath scanning library
        try {
            // Mock-up example, replace with actual class names
            classes.add(Class.forName("studenttest.test1"));
            classes.add(Class.forName("studenttest.test2"));
            classes.add(Class.forName("studenttest.test3"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classes;
    }

    @Override
    public void start(Stage applicationStage) throws Exception {
        applicationStage.setTitle("Test Framework GUI");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new javafx.geometry.Insets(20, 20, 20, 20));

        ListView<CheckBox> testSelectionListView = new ListView<>();
        ObservableList<CheckBox> testItems = FXCollections.observableArrayList();

        for (Class<?> testClass : getTestClasses()) {
            for (Method method : testClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Test.class)) { // Check if the method is annotated with @Test
                    CheckBox checkBox = new CheckBox(testClass.getSimpleName() + " - " + method.getName());
                    testItems.add(checkBox);
                }
            }
        }

        testSelectionListView.setItems(testItems);
        gridPane.add(testSelectionListView, 0, 0, 2, 1);

        Button runTestsButton = new Button("Run Selected Tests");
        runTestsButton.setOnAction(e -> {
            for (CheckBox checkBox : testItems) {
                if (checkBox.isSelected()) {
                    System.out.println("Selected: " + checkBox.getText());
                    // Place your logic to run the test here.
                }
            }
        });

        gridPane.add(runTestsButton, 0, 1, 2, 1);

        Scene scene = new Scene(gridPane, 500, 300);
        applicationStage.setScene(scene);
        applicationStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
