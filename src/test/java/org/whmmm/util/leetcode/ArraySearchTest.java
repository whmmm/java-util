package org.whmmm.util.leetcode;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p><b> -------------------------- </b></p>
 * <p><b>  author: whmmm </b></p>
 * <p><b>  date  : 2023/11/7 14:50 </b></p>
 *
 * @author whmmm
 */
public class ArraySearchTest {
    @Test
    public void test() {
        int[] arr = {1, 2, 4, 5, 6, 8};
        List<Integer> list = Arrays.stream(arr).boxed().collect(Collectors.toList());
        int index = halfSearch(list, 4);

        System.out.println(index);
    }

    public int halfSearch(List<? extends Number> arr, Number searchValue) {
        int left = 0;
        int right = arr.size() - 1;

        final double search = searchValue.doubleValue();

        while (left <= right) {
            Double leftValue = arr.get(left).doubleValue();

            int i = leftValue.compareTo(search);
            if (i == 0) {
                return left;
            } else if (i > 0) {
                left = right / 2;

            } else {
                // i < 0

            }

        }

        return -1;
    }
}
