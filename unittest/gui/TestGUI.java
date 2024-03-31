package unittest.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import unittest.annotations.Ordered;
import unittest.annotations.Test;
import unittest.driver.TestDriver;
import unittest.listeners.TestListener;
import unittest.results.TestClassResult;
import unittest.runners.FilteredTestRunner;
import unittest.runners.OrderedTestRunner;
import unittest.runners.ParameterizedTestRunner;
import unittest.runners.TestRunner;

import java.lang.reflect.Method;
import java.util.*;

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
        Map<String,ArrayList<String>> d = new HashMap<>();
        d.put("test1",new ArrayList<>());
        d.put("test2",new ArrayList<>());
        d.put("test3",new ArrayList<>());
        applicationStage.setTitle("Test Framework GUI");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 20, 20, 20));

        ListView<CheckBox> testSelectionListView = new ListView<>();
        ObservableList<CheckBox> testItems = FXCollections.observableArrayList();

        for (Class<?> testClass : getTestClasses()) {
            for (Method method : testClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Test.class)) { // Check if the method is annotated with @Test
                    CheckBox checkBox = new CheckBox(testClass.getSimpleName() + "-" + method.getName());
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


                    String[] theClass = checkBox.getText().split("-");
                    d.get(theClass[0]).add(theClass[1]);
//                    try{
//                        String[] testD;
//                        Class<?> cls = Class.forName("studenttest." +theClass[0]+"."+theClass[1]);
////                        String name = theClass[1];
// //                       List<String> testsToTest = new ArrayList<>();
////                        testsToTest.add(name);
////                        TestRunner filt = new FilteredTestRunner(cls,testsToTest,theClass[0]);
//                    }catch(ClassNotFoundException w){
//
//                    }
                    // Place your logic to run the test here.
                }
            }
            int count =0;
            ArrayList<String> testDriverSend = new ArrayList<>();
            for(String key : d.keySet()){
                testDriverSend.add("studenttest."+key+"#");
                for(String l : d.get(key)){
                    testDriverSend.set(count, testDriverSend.get(count) +l+",");
                }
                testDriverSend.set(count, testDriverSend.get(count).substring(0, testDriverSend.get(count).length()-1));
                count++;
            }
            String[] testMethods = new String[testDriverSend.size()];
            for (int i = 0; i < testDriverSend.size(); i++) {
                if(testDriverSend.get(i).contains("#")){
                    testMethods[i] = testDriverSend.get(i);
                }

            }

            runTests(testMethods);
        });

        gridPane.add(runTestsButton, 0, 1, 2, 1);

        Scene scene = new Scene(gridPane, 500, 300);
        applicationStage.setScene(scene);
        applicationStage.show();
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
                    FTR = new FilteredTestRunner(clazz, mthds, className);
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

    public static void main(String[] args) {
        launch(args);
    }
}
