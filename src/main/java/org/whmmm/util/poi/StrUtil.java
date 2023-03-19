package org.whmmm.util.poi;

/**
 * <p> -------------------------- </p>
 * <p> author: whmmm </p>
 * <p> date  : 2023/3/18 16:29 </p>
 *
 * @author whmmm
 */
final class StrUtil {
    public static boolean isBlank(CharSequence str) {
        return str == null || "".equalsIgnoreCase(str.toString());
    }
}
