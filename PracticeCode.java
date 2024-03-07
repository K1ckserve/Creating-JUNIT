//import java.lang.reflect.Method;
//import java.util.Arrays;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class TestRunner {
//
//    public static List<TestClassResult> runTests(String[] testClasses) {
//        List<TestClassResult> results = new ArrayList<>();
//
//        for (String testInput : testClasses) {
//            try {
//                String className;
//                String[] methodsToRun = null;
//                if (testInput.contains("#")) {
//                    // This is a filtered test
//                    String[] parts = testInput.split("#");
//                    className = parts[0];
//                    methodsToRun = parts[1].split(",");
//                } else {
//                    className = testInput;
//                }
//
//                Class<?> clazz = Class.forName(className);
//                Object testInstance = clazz.getDeclaredConstructor().newInstance();
//                TestClassResult classResult = new TestClassResult(className);
//
//                // Get methods in defined order if OrderedTest, else just get all declared methods
//                List<Method> methods = getMethodsInOrder(clazz, methodsToRun);
//
//                for (Method method : methods) {
//                    boolean methodPassed = true;
//                    String failureMessage = null;
//                    try {
//                        method.invoke(testInstance);
//                    } catch (Exception e) {
//                        methodPassed = false;
//                        failureMessage = e.getCause().getMessage();
//                    }
//                    classResult.addTestMethodResult(new TestMethodResult(method.getName(), methodPassed, failureMessage));
//                }
//
//                results.add(classResult);
//            } catch (Exception e) {
//                e.printStackTrace(); // Handle errors related to class loading or instantiation
//            }
//        }
//
//        return results;
//    }
//
//    private static List<Method> getMethodsInOrder(Class<?> clazz, String[] filteredMethods) {
//        // This is a simplified example. You'd need to implement logic for ordering and filtering
//        return Arrays.stream(clazz.getDeclaredMethods())
//                .filter(method -> method.isAnnotationPresent(unittest.annotations.Test.class))
//                .filter(method -> {
//                    if (filteredMethods == null) return true; // No filtering applied
//                    return Arrays.asList(filteredMethods).contains(method.getName());
//                })
//                .collect(Collectors.toList());
//        // For ordered tests, you would need to sort this list based on the ordering criteria
//    }
//
//    public static void main(String[] args) {
//        // Example test class names. Replace with actual fully qualified names.
//        String[] testClasses = {"sampletest.TestA", "sampletest.TestB#testMethod1,testMethod3"};
//        List<TestClassResult> testResults = runTests(testClasses);
//
//        // Example output
//        for (TestClassResult classResult : testResults) {
//            System.out.println("Results for: " + classResult.className);
//            for (TestMethodResult methodResult : classResult.methodResults) {
//                System.out.println(methodResult.methodName + ": " + (methodResult.passed ? "PASS" : "FAIL - " + methodResult.failureMessage));
//            }
//        }
//    }
//}
