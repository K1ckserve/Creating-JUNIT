package unittest.runners;

import unittest.annotations.Test;
import unittest.listeners.TestListener;
import unittest.results.TestClassResult;
import unittest.results.TestMethodResult;

import java.lang.reflect.Method;

public class TestRunner {
    Class testClass;
    String className;

    public TestRunner(Class testClass, String className) {
        this.testClass = testClass;
        this.className = className;
        // TODO: complete this constructor
    }

    public TestClassResult run() {
        Object testInstance = testClass.getDeclaredConstructor().newInstance();
        TestClassResult classResult = new TestClassResult(className); //we will just use the test runners run
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                try {
                    method.invoke(testInstance);
                    classResult.addTestMethodResult(new TestMethodResult(method.getName(), true, null));
                } catch (Exception e) {
                    //classResult.addTestMethodResult(new TestMethodResult(method.getName(), false, e.getCause().getMessage()));
                }
            }
        }
        return classResult;
}

    public void addListener(TestListener listener) {
        // Do NOT implement this method for Assignment 4
        // You will implement this for Assignment 5
        // Do NOT remove this method
    }
}
