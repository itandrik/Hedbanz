package com.brainus.hedbanz;

import org.junit.Assert;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw("1111", salt);
        Assert.assertTrue(BCrypt.checkpw("1111", hashedPassword));
        System.out.println("Salt : " + salt + "\nHASH : " + hashedPassword);
    }
}