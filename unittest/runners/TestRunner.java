package unittest.runners;

import unittest.annotations.Test;
import unittest.assertions.AssertionException;
import unittest.listeners.TestListener;
import unittest.results.TestClassResult;
import unittest.results.TestMethodResult;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

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
        ArrayList<String> alph = new ArrayList<>();
        ArrayList<Method> ord = new ArrayList<>();
        TestClassResult classResult = new TestClassResult(className); //we will just use the test runners run
        try{
            Object testInstance = testClass.getDeclaredConstructor().newInstance();
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
        } catch (Exception e) {
            e.printStackTrace(); // Handle errors related to class loading or instantiation
        }
        return classResult;
}

    public void addListener(TestListener listener) {

    }
}
