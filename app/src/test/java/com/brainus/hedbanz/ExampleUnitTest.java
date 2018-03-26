package com.brainus.hedbanz;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Object obj = new int[]{1, 2, 3};
        int[] someAr = (int[]) obj;
        for (int i : someAr) {
            System.out.printf(i + " ");
        }
        if(null instanceof Object){
            System.out.println("true");
        }
        int[] x = {1,2,3};
        int y[] = {4,5,6};
    }
}