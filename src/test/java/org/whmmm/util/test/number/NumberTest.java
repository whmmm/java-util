package org.whmmm.util.test.number;

import org.junit.jupiter.api.Test;

import javax.sound.midi.Soundbank;
import java.util.Objects;

/**
 * <p><b> -------------------------- </b></p>
 * <p><b>  author: whmmm </b></p>
 * <p><b>  date  : 2023/7/27 16:57 </b></p>
 *
 * @author whmmm
 */
public class NumberTest {
    @Test
    public void test() {
        Integer a = 1000;
        Integer b = 1000;

        System.out.println(a == b);
        System.out.println(a.equals(b));
    }

    @Test
    public void test2() {
        Integer a = null;
        int b = 0;
        if (Objects.equals(a, b)) {
            System.out.println("dfasd");
        }
        System.out.println();
    }
}
