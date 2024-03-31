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
    String[] test;
    int flag = 0;
    public TestDriver(String[] test, int flag){
        this.test =test;
        this.flag = flag;
    }


    /**
     * Execute the specified test classes, returning the results of running
     * each test class, in the order given.
     */
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
        System.out.println("==========");
        System.out.println("Tests run: "+ counter+", Failures: "+print.size());
    }

    public void main(String[] args) {
        // Use this for your testing.  We will not be calling this method.
        String[] testClasses = {"studenttest.test1#testB,testA","studenttest.test2"};
        runTests(testClasses);
    }
}

