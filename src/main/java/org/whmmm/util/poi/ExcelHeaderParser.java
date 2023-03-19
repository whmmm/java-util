package org.whmmm.util.poi;

import lombok.Data;
import org.whmmm.util.poi.beans.ExcelHead;

import javax.annotation.Nullable;
import java.util.*;

/**
 * 专门用来解析 <br>
 * {@link org.whmmm.util.poi.beans.ExcelHead}
 * 这种不确定的树形数据
 * <p> -------------------------- </p>
 * <p> author: whmmm </p>
 * <p> date  : 2023/3/18 21:19 </p>
 *
 * @author whmmm
 */
public final class ExcelHeaderParser {
    private ExcelHeaderParser() {
    }

    /**
     * 解析跨了多少列, 不关心具体的是多少行列
     *
     * @param headList -
     * @return -
     */
    public static ParseHeadResult parseList(Collection<ExcelHead> headList) {
        ParseHeadResult result = new ParseHeadResult();
        parseRecursive(result, headList, 0, 0);

        // 计算要合并的列数据
        for (int i = 0; i < result.getRowLen(); i++) {
            for (int j = 0; j < result.getColLen(); j++) {
                // 分别向 右 下 探测, 探测到为 null 时, 代表可以合并
                ParseHeadInfo info = result.getInfo(i, j);
                if (info != null) {
                    updateMergeInfo(result, info, i, j);
                }
            }
        }

        return result;
    }

    /**
     * 更新合并数据
     *
     * @param info   -
     * @param curRow -
     * @param curCol -
     */
    private static void updateMergeInfo(ParseHeadResult result,
                                        ParseHeadInfo info,
                                        int curRow,
                                        int curCol) {
        int crossRow = 0;
        int crossCol = 0;

        for (int i = curRow + 1; i < result.getRowLen(); i++) {
            String position = getPosition(i, curCol);
            if (!result.validateCanMerge(position)) {
                // 有冲突
                break;
            }
            ParseHeadInfo temp = result.getInfo(position);
            if (temp == null) {
                crossRow++;
            } else {
                break;
            }
        }
        for (int i = curCol + 1; i < result.getColLen(); i++) {
            String position = getPosition(curRow, i);
            if (!result.validateCanMerge(position)) {
                // 有冲突
                break;
            }

            ParseHeadInfo temp = result.getInfo(position);
            if (temp == null) {
                crossCol++;
            } else {
                break;
            }
        }

        info.setCrossRow(crossRow);
        info.setCrossCol(crossCol);

    }

    private static int parseRecursive(ParseHeadResult result,
                                      Collection<ExcelHead> headList,
                                      int startRow,
                                      int startCol) {

        int index = startCol;
        int pureIndex = 0;
        for (ExcelHead head : headList) {

            Map<String, ParseHeadInfo> map = result.getInfoMap();
            ParseHeadInfo value = new ParseHeadInfo(head.getName(), head.hasNotChild());
            value.setHead(head);
            map.put(getPosition(startRow, index), value);

            if (head.hasChild()) {
                // 有子列时, 加的就不是 1 了
                // 而是最大子列数
                int recursive = parseRecursive(result, head.getChildHead(),
                                               startRow + 1,
                                               index
                );
                index += recursive;
                pureIndex += recursive;
            } else {
                // 应该不加 index ?
                index++;
                pureIndex++;
            }


            result.setRowLen(Math.max(result.getRowLen(), startRow + 1));
            result.setColLen(Math.max(result.getColLen(), index));

            // index 自增加 1
            // index++;

        }

        return pureIndex;
    }

    public static String getPosition(int row, int col) {
        return row + ":" + col;
    }


    @Data
    public static class ParseHeadResult {
        // 决定有多上行
        private int rowLen;
        // 决定有多少列
        private int colLen;
        /**
         * 存储 {@code Map<getPosition(row,col), ParseHeadInfo>} <br/>
         */
        private final Map<String, ParseHeadInfo> infoMap = new HashMap<>();

        private final Set<String> mergedPosition = new HashSet<>();

        @Nullable
        public ParseHeadInfo getInfo(int row,
                                     int col) {
            return infoMap.get(getPosition(row, col));
        }

        @Nullable
        public ParseHeadInfo getInfo(String position) {
            return infoMap.get(position);
        }

        public boolean validateCanMerge(String position) {
            boolean contains = this.mergedPosition.contains(position);
            if (contains) {
                return false;
            } else {
                this.mergedPosition.add(position);
                return true;
            }

        }
    }

    /**
     *
     */
    @Data
    public static class ParseHeadInfo {
        private int crossRow;
        private int crossCol;
        public boolean leaf; // 是否是叶子节点, 只有叶子节点才需要设置行数据
        private String label;

        private ExcelHead head;

        public ParseHeadInfo(String label,
                             boolean leaf) {
            this.label = label;
            this.leaf = leaf;
        }

        /**
         * 是否需要合并
         *
         * @return -
         */
        public boolean isNeedMerge() {
            return crossRow > 0 ||
                   crossCol > 0;
        }
    }
}
