package org.whmmm.util.leetcode;

/**
 * <p> -------------------------- </p>
 * <p> *** author: whmmm | date: 2023/3/12 18:45*** </p>
 *
 * @author whmmm
 */
final class SortUtil {
    private SortUtil() {
    }

    public static <T extends Comparable<T>> void bubbleSort(SortStrategy strategy,
                                                            T[] arr) {
        final int arrLen = arr.length;
        for (int i = 0; i < arrLen - 1; i++) {
            for (int j = 0; j < arrLen - 1 - i; j++) {
                int compare = arr[j].compareTo(arr[j + 1]);

                if (strategy == SortStrategy.DESC) {
                    compare *= -1;
                }

                if (compare >= 0) {
                    // 交换
                    T temp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    /**
     * 选择排序
     *
     * @param strategy -
     * @param arr      -
     * @param <T>      -
     */
    public static <T extends Comparable<T>> void selectSort(SortStrategy strategy,
                                                            T[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                T vI = arr[i];
                T vJ = arr[j];

                int result = vI.compareTo(vJ);

                if (strategy == SortStrategy.DESC) {
                    result *= -1;
                }

                if (result >= 0) {
                    T temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }

    }
}
