package unittest.runners;

import unittest.annotations.Test;
import unittest.assertions.AssertionException;
import unittest.results.TestClassResult;
import unittest.results.TestMethodResult;

import java.lang.reflect.Method;
import java.util.ArrayList;
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
        ArrayList<String> dup = new ArrayList<>();
        TestClassResult classResult = new TestClassResult(className); //we will just use the test runners run
        try {
            for(String c: testMethods){
                if(!dup.contains(c)) {
                    dup.add(c);
                    Object testInstance = testClass.getDeclaredConstructor().newInstance();
                    for (Method method : testClass.getDeclaredMethods()) {
                        if (c.equals(method.getName())) {
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
                }
            }
//            Object testInstance = testClass.getDeclaredConstructor().newInstance();
//            for (Method method : testClass.getDeclaredMethods()) {
//                if (testMethods.contains(method.getName())) {
//                    if (method.isAnnotationPresent(Test.class)) {
//                        try {
//                            method.invoke(testInstance);
//                            classResult.addTestMethodResult(new TestMethodResult(method.getName(), true, null));
//                        } catch (Exception e) {
//                            Throwable cause = e.getCause();
//                            classResult.addTestMethodResult(new TestMethodResult(method.getName(), false, (AssertionException) cause));
//                        }
//                    }
//                }
//            }
            } catch(Exception e){
                e.printStackTrace(); // Handle errors related to class loading or instantiation
            }
        return classResult;
    }

    // TODO: Finish implementing this class
}
