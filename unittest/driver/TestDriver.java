package unittest.driver;

import sampletest.TestA;
import unittest.annotations.Order;
import unittest.results.TestClassResult;
import unittest.results.TestMethodResult;
import unittest.runners.FilteredTestRunner;
import unittest.runners.OrderedTestRunner;
import unittest.runners.ParameterizedTestRunner;
import unittest.runners.TestRunner;

import java.lang.reflect.Method;
import java.util.*;

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
                    FTR = new FilteredTestRunner(clazz, mthds, className);
                    results.add(FTR.run());
                }else{ // if nothing else then we will run basic TR
                    clazz = Class.forName(className);
                    TR = new TestRunner(clazz, className);
                    if(clazz.isAnnotationPresent(Order.class)){
                        TR = new OrderedTestRunner(clazz, className);
                    }
                    results.add(TR.run());
                }
            } catch (Exception e) {
                e.printStackTrace(); // Handle errors related to class loading or instantiation
            }
        }
        int counter=0;
        Map<TestMethodResult, TestClassResult> print = new HashMap<>();
        for(TestClassResult a : results){
            String plug;
            for (TestMethodResult b: a.getTestMethodResults()){
                if(b.isPass()){
                    plug= "PASS";
                }else{
                    plug= "FAIL";
                    print.put(b,a);
                }
                counter++;
                System.out.println(a.getTestClassName()+ "." +b.getName()+ " : " + plug);
            }
        }
        System.out.println("==========");
        System.out.println("FAILURES:");
        for(Map.Entry<TestMethodResult, TestClassResult> iter: print.entrySet()){
            TestMethodResult key = iter.getKey();
            TestClassResult value = iter.getValue();
            System.out.println(value.getTestClassName()+"."+key.getName()+":");
            key.getException().printStackTrace();
        }
        System.out.println("==========");
        System.out.println("Tests run: "+ counter+", Failures: "+print.size());

        return results;



    }
//ff
    public static void main(String[] args) {
        // Use this for your testing.  We will not be calling this method.
        //String[] testClasses = {"sampletest.TestC#test4,test5"};
        String[] testClasses = {"sampletest.TestC#test4,test5"};
        runTests(testClasses);
    }
}
