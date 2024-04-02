package unittest.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import unittest.annotations.Ordered;
import unittest.annotations.Test;
import unittest.driver.TestDriver;
import unittest.listeners.GUITestListener;
import unittest.listeners.TestListener;
import unittest.results.TestClassResult;
import unittest.runners.FilteredTestRunner;
import unittest.runners.OrderedTestRunner;
import unittest.runners.ParameterizedTestRunner;
import unittest.runners.TestRunner;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.*;

public class TestGUI extends Application {
    private static TextArea outputArea;
    public Map<String,Throwable> fails = new HashMap<>();

    public Map<String,ArrayList<String>> d = new HashMap<>();


    // Assuming these are your test classes
    private List<Class<?>> getTestClasses(String cp) {
        List<Class<?>> classes = new ArrayList<>();
        // Dynamically adding classes would require a classpath scanning library
        try {
            // Mock-up example, replace with actual class names
            classes.add(Class.forName(cp));
            //classes.add(Class.forName("studenttest.test1"));
            //classes.add(Class.forName("studenttest.test2"));
            //classes.add(Class.forName("studenttest.test3"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classes;
    }
    public static void appendText(String text) {
        Platform.runLater(() -> {
            if (outputArea != null) {
                outputArea.appendText(text + "\n");
            } else {
                System.err.println("Attempted to append text to an uninitialized output area: " + text);
            }
        });
    }

    @Override
    public void start(Stage applicationStage) throws Exception {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 20, 20, 20));

// Classpath Field and Button
        TextField classpathField = new TextField();
        classpathField.setPromptText("Enter classpath...");
        GridPane.setConstraints(classpathField, 0, 0, 2, 1); // Column 0-1, Row 0
        gridPane.add(classpathField, 0, 0);

        Button cpButton = new Button("Add CP");
        GridPane.setConstraints(cpButton, 2, 0); // Column 2, Row 0
        gridPane.add(cpButton, 2, 0);

// Test Selection ListView
        ListView<CheckBox> testSelectionListView = new ListView<>();
        ObservableList<CheckBox> testItems = FXCollections.observableArrayList();
        testSelectionListView.setItems(testItems);
        GridPane.setConstraints(testSelectionListView, 0, 1, 3, 1); // Column 0-2, Row 1, spanning 3 columns
        gridPane.add(testSelectionListView, 0, 1);

// Buttons
        Button runTestsButton = new Button("Run Selected Tests");
        GridPane.setConstraints(runTestsButton, 0, 2); // Column 0, Row 2
        gridPane.add(runTestsButton, 0, 2);

        Button failed = new Button("View Details");
        GridPane.setConstraints(failed, 1, 2); // Column 1, Row 2
        gridPane.add(failed, 1, 2);

        Button reset = new Button("Reset");
        GridPane.setConstraints(reset, 2, 2); // Column 2, Row 2
        gridPane.add(reset, 2, 2);

// Output Area
        outputArea = new TextArea();
        outputArea.setEditable(false);
        GridPane.setConstraints(outputArea, 0, 3, 3, 1); // Column 0-2, Row 3, spanning 3 columns
        gridPane.add(outputArea, 0, 3);

// Adjust grid column widths and row heights as needed
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(33);
        gridPane.getColumnConstraints().addAll(column1, column1, column1); // Equal width columns

// Apply the grid to the scene
        Scene scene = new Scene(gridPane, 600, 400); // Adjusted size for better visibility
        applicationStage.setScene(scene);
        applicationStage.setTitle("Test Framework GUI");
        applicationStage.show();
        reset.setOnAction(e->{
            outputArea.clear();
            fails.clear();
            d.clear();
            testItems.clear();

        });
//        failed.setOnAction(e -> {
//            Stage ne = new Stage();
//                    for (String key : fails.keySet()) {
//                        showDetailsButton(key, fails.get(key));
//                    }
//                });
        failed.setOnAction(e -> {
            Stage failedTestsStage = new Stage();
            failedTestsStage.setTitle("Failed Tests");

            ListView<String> failedTestsListView = new ListView<>();
            ObservableList<String> failedTestsItems = FXCollections.observableArrayList();
            failedTestsItems.addAll(fails.keySet()); // Add the names of the failed tests
            failedTestsListView.setItems(failedTestsItems);

            failedTestsListView.setOnMouseClicked(event -> {
                String selectedTest = failedTestsListView.getSelectionModel().getSelectedItem();
                if (selectedTest != null) {
                    Throwable exception = fails.get(selectedTest);
                    if (exception != null) {
                        showDetailsButton(selectedTest, exception);
                    }
                }
            });

            VBox layout = new VBox(10);
            layout.getChildren().addAll(new Label("Select a test to view details:"), failedTestsListView);
            layout.setPadding(new Insets(10));

            Scene scenes = new Scene(layout, 400, 300);
            failedTestsStage.setScene(scenes);
            failedTestsStage.show();
        });

        cpButton.setOnAction(e->{
            clear();
            if(classpathField.getText()!=null) {
                String classpath = classpathField.getText().trim();
                for (Class<?> testClass : getTestClasses(classpath)) {
                    d.put(classpath, new ArrayList<>());
                    ArrayList<String> alph = new ArrayList<>();
                    ArrayList<Method> ord = new ArrayList<>();
                    for(Method meth : testClass.getDeclaredMethods()){
                        alph.add(meth.getName());
                        Collections.sort(alph);
                    }
                    for(String s: alph){
                        for(Method meth : testClass.getDeclaredMethods()){
                            if(s.equals(meth.getName())){
                                ord.add(meth);
                            }
                        }
                    }
                    for (Method method : ord) {
                        if (method.isAnnotationPresent(Test.class)) { // Check if the method is annotated with @Test
                            CheckBox checkBox = new CheckBox(classpath + "-" + method.getName());
                            testItems.add(checkBox);
                        }
                    }
                }
                testSelectionListView.setItems(testItems);
                runTestsButton.setOnAction(s -> {
                    clear();
                    for (CheckBox checkBox : testItems) {
                        if (checkBox.isSelected()) {
                            System.out.println("Selected: " + checkBox.getText());


                            String[] theClass = checkBox.getText().split("-");
                           // String[] theClass1 = theClass[1].split("-");
                            d.get(theClass[0]).add(theClass[1]);
                        }
                    }
                    int count = 0;
                    String[] parts = classpath.split("\\.");
                    ArrayList<String> testDriverSend = new ArrayList<>();
                    for (String key : d.keySet()) {
                        testDriverSend.add(key+"#");
                        for (String l : d.get(key)) {
                            testDriverSend.set(count, testDriverSend.get(count) + l + ",");
                        }
                        testDriverSend.set(count, testDriverSend.get(count).substring(0, testDriverSend.get(count).length() - 1));
                        count++;
                    }
                    ArrayList<String> theOne = new ArrayList<>();
                    for (int i = 0; i < testDriverSend.size(); i++) {
                        if (testDriverSend.get(i).contains("#")) {
                            theOne.add(testDriverSend.get(i));
                        }
                    }
                    Collections.sort(theOne);
                    String[] testMethods = new String[theOne.size()];
                    for (int i = 0; i < theOne.size(); i++) {
                        testMethods[i] = theOne.get(i);
                    }

                    runTests(testMethods);
                });
            }
        });



        applicationStage.setScene(scene);
        applicationStage.show();
    }

    public void clear(){
        fails.clear();
        outputArea.clear();
        for (Map.Entry<String, ArrayList<String>> entry : d.entrySet()) {
            entry.getValue().clear();
            // Process the key and value as needed
        }
    }
    public List<TestClassResult> runTests(String[] testclasses) {
        int flags=0;
        // TODO: complete this method
        // We will call this method from our JUnit test cases.
        ArrayList<TestClassResult> results = new ArrayList<TestClassResult>();
        for (String className : testclasses) {

            try {
                Class<?> clazz; // put all of these are becasue the scop was fucked
                FilteredTestRunner FTR;
                OrderedTestRunner ORT;
                ParameterizedTestRunner PTR;
                TestRunner TR;
                if (className.contains("#")) { // this will correctly return all of the tests for the filitred test method
                    flags =1;
                    ArrayList<String> mthds = new ArrayList<>();
                    String[] methodsToRun = null;
                    String[] parts = className.split("#");
                    className = parts[0];
                    methodsToRun = parts[1].split(",");
                    Collections.addAll(mthds, methodsToRun);
                    clazz = Class.forName(className);
                    TestListener guiListener = new GUITestListener();
                    FTR = new FilteredTestRunner(clazz, mthds, className, this, guiListener);
                    results.add(FTR.run());
                } else { // if nothing else then we will run basic TR
                    clazz = Class.forName(className);

                    if (clazz.isAnnotationPresent(Ordered.class)) {
                        flags=1;
                        TR = new OrderedTestRunner(clazz, className);
                    } else {
                        TR = new TestRunner(clazz, className);
                      //  TR.addListener(guilist);
                    }

                    results.add(TR.run());
                }
            } catch (Exception e) {
                e.printStackTrace(); // Handle errors related to class loading or instantiation
            }
        }
        //printOrderedFiltered(results);


        return results;
    }
    public static void showDetailsButton(String testName, Throwable exception) {
        // This method assumes you're showing an Alert with the exception details.

        Platform.runLater(() -> {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            String stackTrace = sw.toString();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failure details for " + testName + ": \n" + stackTrace, ButtonType.CLOSE);
            alert.showAndWait();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
