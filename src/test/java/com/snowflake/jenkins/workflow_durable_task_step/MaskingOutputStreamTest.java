package com.snowflake.jenkins.workflow_durable_task_step;

import org.junit.*;
import org.junit.runners.Parameterized;
import org.junit.runner.RunWith;


import java.util.*;

@RunWith(Parameterized.class)
public class MaskingOutputStreamTest {
    String input;
    String search;
    int indexOf;
    MaskingOutputStream os;

    @Before
    public void initialize() {
        List<String> masks = new ArrayList<String>();
         os = new MaskingOutputStream(System.out, masks);
    }

    public MaskingOutputStreamTest(String input, String search, int indexOf) {
        this.input = input;
        this.search = search;
        this.indexOf = indexOf;
    }

    @Parameterized.Parameters
    public static Collection data() {
       return Arrays.asList(new Object[][] {
        {"This is an input string","not to find", -1},
        {"This is an input string","not to find a very long string", -1},
        {"This is an input string","Input", -1}, // search is case sensitive
        {"This is an input string","strings", -1},
        {"This is an input string","This is an input strings", -1},
        {"This is an input string","This", 0},
        {"This is an input string","input", 11},
       });
    }

    @Test
    public void testIndexOfNotFound() {
        int target = os.indexOf(input.getBytes(), 0, input.getBytes().length, 0, search.getBytes());
        Assert.assertEquals(indexOf, target);
    }
}
