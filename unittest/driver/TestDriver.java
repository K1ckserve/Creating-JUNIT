package unittest.driver;

import sampletest.TestA;
import unittest.results.TestClassResult;
import unittest.results.TestMethodResult;
import unittest.runners.FilteredTestRunner;
import unittest.runners.OrderedTestRunner;
import unittest.runners.ParameterizedTestRunner;
import unittest.runners.TestRunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestDriver {

    /**
     * Execute the specified test classes, returning the results of running
     * each test class, in the order given.
     */
    public static List<TestClassResult> runTests(String[] testclasses) {
        // TODO: complete this method
        // We will call this method from our JUnit test cases.
        ArrayList<TestClassResult> results = new ArrayList<TestClassResult>();
        for (String className : testclasses) {

            try {
                Class<?> clazz; // put all of these are becasue the scop was fucked
                Object testInstance;
                FilteredTestRunner FTR;
                OrderedTestRunner ORT;
                ParameterizedTestRunner PTR;
                TestRunner TR;
                if(className.contains("#")){ // this will correctly return all of the tests for the filitred test method
                    ArrayList<String> mthds = new ArrayList<>();
                    String[] methodsToRun = null;
                    String[] parts = className.split("#");
                    className = parts[0];
                    methodsToRun = parts[1].split(",");
                    Collections.addAll(mthds, methodsToRun);
                    clazz = Class.forName(className);
                    testInstance = clazz.getDeclaredConstructor().newInstance();
                    FTR = new FilteredTestRunner(clazz, mthds);
                }else{ // if nothing else then we will run basic TR
                    clazz = Class.forName(className);
                    testInstance = clazz.getDeclaredConstructor().newInstance(); // cut it right here and put it in test runner
                    TR = new TestRunner(clazz, className);
                }
//                Class<?> clazz = Class.forName(className);
//                Object testInstance = clazz.getDeclaredConstructor().newInstance(); // cut it right here and put it in test runner
//                TestClassResult classResult = new TestClassResult(className); //we will just use the test runners run


//                for (Method method : clazz.getDeclaredMethods()) {
//                    if (method.isAnnotationPresent(unittest.annotations.Test.class)) {
//                        try {
//                            method.invoke(testInstance);
//                            classResult.addTestMethodResult(new TestMethodResult(method.getName(), true, null));
//                        } catch (Exception e) {
//                            //classResult.addTestMethodResult(new TestMethodResult(method.getName(), false, e.getCause().getMessage()));
//                        }
//                    }
//                }
//
//                results.add(classResult);
//            } catch (Exception e) {
//                e.printStackTrace(); // Handle errors related to class loading or instantiation
//            }
        }

        return results;



    }

    public static void main(String[] args) {
        // Use this for your testing.  We will not be calling this method.
        String[] testClasses = {"sampletest.TestA#test1"};
        runTests(testClasses);
    }
}
