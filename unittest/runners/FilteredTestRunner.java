package unittest.runners;

import unittest.annotations.Test;
import unittest.assertions.AssertionException;
import unittest.results.TestClassResult;
import unittest.results.TestMethodResult;

import java.lang.reflect.Method;
import java.util.List;

public class FilteredTestRunner extends TestRunner {
    List<String> testMethods;
    public FilteredTestRunner(Class testClass, List<String> testMethods, String className) {
        super(testClass,className);
        this.testMethods= testMethods;
        // TODO: complete this constructor
    }
    @Override
    public TestClassResult run(){
        TestClassResult classResult = new TestClassResult(className); //we will just use the test runners run
        try {
            Object testInstance = testClass.getDeclaredConstructor().newInstance();
            for (Method method : testClass.getDeclaredMethods()) {
                if (testMethods.contains(method.getName())) {
                    if (method.isAnnotationPresent(Test.class)) {
                        try {
                            method.invoke(testInstance);
                            classResult.addTestMethodResult(new TestMethodResult(method.getName(), true, null));
                        } catch (Exception e) {
                            Throwable cause = e.getCause();
                            classResult.addTestMethodResult(new TestMethodResult(method.getName(), false, (AssertionException) cause));
                        }
                    }
                }
            }
            } catch(Exception e){
                e.printStackTrace(); // Handle errors related to class loading or instantiation
            }
        return classResult;
    }

    // TODO: Finish implementing this class
}
