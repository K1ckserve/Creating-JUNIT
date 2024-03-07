package unittest.driver;

import sampletest.TestA;
import unittest.results.TestClassResult;

import java.util.ArrayList;
import java.util.List;

public class TestDriver {

    /**
     * Execute the specified test classes, returning the results of running
     * each test class, in the order given.
     */
    public static List<TestClassResult> runTests(String[] testclasses) {
        // TODO: complete this method
        // We will call this method from our JUnit test cases.
<<<<<<< Updated upstream
        ArrayList<TestClassResult> results = new ArrayList<TestClassResult>();
        for(int i = 0; i< testclasses.length; i++){
            results.add(new TestClassResult(testclasses[i]));
        }
        for(TestClassResult i: results){
            System.out.println(i.getTestClassName());
        }
        return results;
=======

>>>>>>> Stashed changes
    }

    public static void main(String[] args) {
        // Use this for your testing.  We will not be calling this method.
        runTests(TestA);
    }
}
