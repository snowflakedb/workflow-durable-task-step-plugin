package io.jenkins.plugins;

import org.junit.*;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.RunWith;


import java.util.*;

@RunWith(Parameterized.class)
public class MaskingOutputStreamWithOffsetTest {
    String input;
    String search;
    int indexOf;
    int off;
    int len;
    int start;

    MaskingOutputStream os;

    @Before
    public void initialize() {
        List<String> masks = new ArrayList<String>();
         os = new MaskingOutputStream(System.out, masks);
    }

    public MaskingOutputStreamWithOffsetTest(String input, String search, int indexOf, int off, int len, int start) {
        this.input = input;
        this.search = search;
        this.indexOf = indexOf;
        this.off = off;
        this.len = len;
        this.start = start;
    }

    @Parameterized.Parameters
    public static Collection data() {
       return Arrays.asList(new Object[][] {
        {"This is an input string","input", 11, 0, 23, 0},
        {"This is an input string","input", 11, 5, 23, 0},
        {"This is an input string","input", -1, 12, 23, 0},
        {"This is an input string","input", -1, 0, 23, 12},
        {"This is an input string","input", -1, 6, 23, 6},
        {"This is an input string","input", -1, 0, 14, 0},
       });
    }

    @Test
    public void testIndexOfNotFound() {
        int target = os.indexOf(input.getBytes(), off, len, start, search.getBytes());
        Assert.assertEquals(indexOf, target);
    }
}
