package unittest.runners;

import unittest.annotations.Test;
import unittest.assertions.AssertionException;
import unittest.listeners.TestListener;
import unittest.results.TestClassResult;
import unittest.results.TestMethodResult;

import java.lang.reflect.Method;

public class TestRunner {
    Class testClass;
    String className;
    //Object testInstance;


    public TestRunner(Class testClass, String className) {
        this.testClass = testClass;
        this.className = className;
       // this.testInstance = testInstance;
        // TODO: complete this constructor
    }

    public TestClassResult run() {
        TestClassResult classResult = new TestClassResult(className); //we will just use the test runners run
        try{
            Object testInstance = testClass.getDeclaredConstructor().newInstance();
            for (Method method : testClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    try {
                        method.invoke(testInstance);
                        classResult.addTestMethodResult(new TestMethodResult(method.getName(), true, null));
                    } catch (Exception e) {
                        AssertionException assertionException = new AssertionException(e.getMessage());
                        classResult.addTestMethodResult(new TestMethodResult(method.getName(), false, assertionException));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle errors related to class loading or instantiation
        }
        return classResult;
}

    public void addListener(TestListener listener) {
        // Do NOT implement this method for Assignment 4
        // You will implement this for Assignment 5
        // Do NOT remove this method
    }
}
