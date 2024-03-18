package unittest.runners;

import unittest.annotations.Order;
import unittest.annotations.Test;
import unittest.assertions.AssertionException;
import unittest.results.TestClassResult;
import unittest.results.TestMethodResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderedTestRunner extends TestRunner {

    public OrderedTestRunner(Class testClass, String className) {
        super(testClass,className);
        // TODO: complete this constructor
    }

    @Override
    public TestClassResult run() {
        TestClassResult classResult = new TestClassResult(className);
        try {
            Object testInstance = testClass.getDeclaredConstructor().newInstance();

            // Filter and sort test methods based on @Order, if present
            List<Method> orderedMethods = Arrays.stream(testClass.getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(Test.class))
                    .sorted(Comparator.comparingInt(m ->
                            m.isAnnotationPresent(Order.class) ? m.getAnnotation(Order.class).value() : Integer.MAX_VALUE))
                    .collect(Collectors.toList());

            for (Method method : orderedMethods) {
                try {
                    method.setAccessible(true); // in case method is not public
                    method.invoke(testInstance);
                    classResult.addTestMethodResult(new TestMethodResult(method.getName(), true, null));
                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    String message = (cause != null && cause.getMessage() != null) ? cause.getMessage() : "Test failed without a specific message.";
                    classResult.addTestMethodResult(new TestMethodResult(method.getName(), false, new AssertionException(message)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle errors related to class loading or instantiation
        }
        return classResult;
    }
}
