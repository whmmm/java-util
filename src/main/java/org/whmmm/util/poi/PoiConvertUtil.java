package org.whmmm.util.poi;

/**
 * <p> -------------------------- </p>
 * <p> author: whmmm </p>
 * <p> date  : 2023/3/18 17:12 </p>
 *
 * @author whmmm
 */
public final class PoiConvertUtil {
    private PoiConvertUtil() {
    }


    /**
     * 转行 poi 坐标 为 excel 的行列选择地址
     * <pre>{@code
     * example:
     *  getExcelAddr(1, 0, 3) ->  "A2:D2"
     *
     * }</pre>
     *
     * @param rowNumber poi 的行号 , 从 0 开始
     * @param colStart  开始的列, 从 0 开始
     * @param colEnd    结束的列, 从 0 开始
     * @return -
     */
    public static String toExcelAddr(int rowNumber,
                                     int colStart,
                                     int colEnd) {
        int row = rowNumber + 1;

        return String.format(
            "%s%s:%s%s",
            toExcelCol(colStart), row,
            toExcelCol(colEnd), row
        );
    }

    /**
     * 参考 https://codereview.stackexchange.com/questions/44545/excel-column-string-to-row-number-and-vice-versa
     * <pre>{@code
     * 0  ->  A
     * 1  ->  B
     * 25 ->  Z
     * 26 ->  AA
     * }</pre>
     *
     * @param col apache poi 的列, 从 0 开始
     * @return -
     */
    public static String toExcelCol(int col) {
        col += 1;
        StringBuilder sb = new StringBuilder();
        while (col-- > 0) {
            sb.append((char) ('A' + (col % 26)));
            col /= 26;
        }
        return sb.reverse().toString();
    }

    /**
     * 转换为 poi 的索引 (从 0 开始)
     *
     * @param name - 转换为 poi 的索引 (从 0 开始)
     * @return -
     */
    public static int toPoiColNum(String name) {
        int number = 0;
        for (int i = 0; i < name.length(); i++) {
            number = number * 26 + (name.charAt(i) - ('A' - 1));
        }
        return number - 1;
    }
}
