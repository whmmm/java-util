package org.whmmm.util.leetcode;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * <p> -------------------------- </p>
 * <p> *** author: whmmm | date: 2023/3/12 18:47*** </p>
 *
 * @author whmmm
 */
public class SortTest {

    @Test
    public void test() {
        Integer[] arr = {1, 3, 2, 5, 4};
        SortUtil.bubbleSort(SortStrategy.DESC,
                            arr);

        System.out.println(Arrays.toString(arr));
    }
}
