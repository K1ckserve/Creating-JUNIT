package unittest.driver;




import unittest.annotations.Order;
import unittest.annotations.Ordered;
import unittest.assertions.AssertionException;
import unittest.results.TestClassResult;
import unittest.results.TestMethodResult;
import unittest.runners.FilteredTestRunner;
import unittest.runners.OrderedTestRunner;
import unittest.runners.ParameterizedTestRunner;
import unittest.runners.TestRunner;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class TestDriver {


    /**
     * Execute the specified test classes, returning the results of running
     * each test class, in the order given.
     */
    public static List<TestClassResult> runTests(String[] testclasses) {
        int flag=0;
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
                    flag =1;
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
                        flag=1;
                        TR = new OrderedTestRunner(clazz, className);
                    } else {
                        TR = new TestRunner(clazz, className);
                    }

                    results.add(TR.run());
                }
            } catch (Exception e) {
                e.printStackTrace(); // Handle errors related to class loading or instantiation
            }
        }
        printOrderedFiltered(results);


        return results;



    }

    public static void printOrderedFiltered(ArrayList<TestClassResult> results){
        ArrayList<TestMethodResult> key = new ArrayList<>();
        int counter=0;
        Map<TestMethodResult, TestClassResult> print = new HashMap<>();
        for(TestClassResult a : results){
            String plug;
            for (TestMethodResult b: a.getTestMethodResults()){
                if(b.isPass()){
                    plug= "PASS";
                }else{
                    plug= "FAIL";
                    key.add(b);
                    print.put(b,a);
                }
                counter++;
                System.out.println(a.getTestClassName()+ "." +b.getName()+ " : " + plug);
            }
        }
        System.out.println("==========");
        System.out.println("FAILURES:");
        for(TestMethodResult s : key){
            System.out.println(print.get(s).getTestClassName()+"."+s.getName()+":");
            s.getException().printStackTrace();
        }
//        for(Map.Entry<TestMethodResult, TestClassResult> iter: print.entrySet()){
//            TestMethodResult key = iter.getKey();
//            TestClassResult value = iter.getValue();
//            System.out.println(value.getTestClassName()+"."+key.getName()+":");
//            key.getException().printStackTrace();
//        }
        System.out.println("==========");
        System.out.println("Tests run: "+ counter+", Failures: "+print.size());
    }

//    public static void printNormal(ArrayList<TestClassResult> results){
//        int counter = 0;
//        Map<TestMethodResult, TestClassResult> print = new LinkedHashMap<>(); // Use LinkedHashMap to preserve insertion order after sorting
//
//        for (TestClassResult a : results) {
//            // Sort test method results by method name
//            List<TestMethodResult> sortedMethodResults = a.getTestMethodResults().stream()
//                    .sorted(Comparator.comparing(TestMethodResult::getName))
//                    .collect(Collectors.toList());
//
//            for (TestMethodResult b : sortedMethodResults) {
//                String outcome = b.isPass() ? "PASS" : "FAIL";
//                if (!b.isPass()) {
//                    print.put(b, a); // Assuming failure details are to be printed later
//                }
//                System.out.println(a.getTestClassName() + "." + b.getName() + " : " + outcome);
//                counter++;
//            }
//        }
//
//        System.out.println("==========");
//        System.out.println("FAILURES:");
//        for (Map.Entry<TestMethodResult, TestClassResult> entry : print.entrySet()) {
//            TestMethodResult testMethodResult = entry.getKey();
//            TestClassResult testClassResult = entry.getValue();
//
//            // Print the class and method name for the failing test
//            System.out.println(testClassResult.getTestClassName() + "." + testMethodResult.getName() + ":");
//
//            // Assuming your TestMethodResult has a getException() method that returns an AssertionException
//            AssertionException exception = testMethodResult.getException();
//
//            // Check if there's an exception and it's an instance of AssertionException
//            if (exception != null) {
//                // Print the assertion error message and custom stack trace
//                // Print the message
//                exception.printStackTrace(); // Print the custom stack trace
//            }
//        }
//        System.out.println("==========");
//        System.out.println("Tests run: " + counter + ", Failures: " + print.size());
//    }
//ff
    public static void main(String[] args) {
        // Use this for your testing.  We will not be calling this method.
        String[] testClasses = {"studenttest.test1#testB,testA","studenttest.test2"};
        runTests(testClasses);
    }
}

