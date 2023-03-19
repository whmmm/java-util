package org.whmmm.util.poi.beans;

import lombok.Data;
import lombok.Getter;
import org.apache.poi.hssf.usermodel.HSSFCell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p> -------------------------- </p>
 * <p> *** author: whmmm | date: 2022/6/30 14:04*** </p>
 *
 * @author whmmm
 */
@Data
public class HeaderInfoDto {

    private int autoColumnId = 0;

    /**
     * {@code Map{在 excel 中的索引, 列 id} }
     */
    private final Map<Integer, Integer> columnIndexIdMapping = new HashMap<>();

    /**
     * 数据列开始列
     */
    private int dataColumn;


    private final List<HeadMeta> headMetaList = new ArrayList<>();

    /**
     * {@code Map<excel column 列索引, ExcelHead 表头参数信息 >}
     */
    @Getter
    private final Map<Integer, ExcelHead> excelColumnHeadMap = new HashMap<>();

    /**
     * 设置索引 列
     *
     * @param columnId    列 id
     * @param columnIndex 列在 excel 中的索引
     */
    public void putMapping(int columnIndex, Integer columnId) {
        if (columnId == null) {
            columnId = ++autoColumnId;
        }

        this.columnIndexIdMapping.put(columnIndex, columnId);
    }

    public int getColumnId(int columnIndex) {

        Integer index = this.columnIndexIdMapping.get(columnIndex);
        if (index == null) {
            index = -1;
        }

        return index;
    }

    /**
     * 获取 excel 数据列索引集合, 升序排序
     *
     * @return
     */
    public List<Integer> computeColumnIndexList() {
        return this.columnIndexIdMapping.keySet()
                                        .stream().sorted()
                                        .collect(Collectors.toList());
    }


    public void addHeadMeta(int startColumn, int endColumn, int startRow, int endRow, ExcelHead head, HSSFCell cell) {
        HeadMeta meta = new HeadMeta();
        meta.setStartColumn(startColumn);
        meta.setEndColumn(endColumn);
        meta.setStartRow(startRow);
        meta.setEndRow(endRow);
        meta.setHead(head);
        meta.setCell(cell);

        this.getHeadMetaList().add(meta);
    }

    public ExcelHead getHeaderByExcelColumn(Integer columnIndex) {
        return excelColumnHeadMap.get(columnIndex);
    }

    /**
     * 获取最大列数 (从 0 开始)
     * @return -
     */
    public int getMaxColumn() {
        return this.autoColumnId - 1;
    }

    /**
     * 区间关系 [startColumn, endColumn ) <br/>
     * [startRow,  endRow)
     */
    @Data
    public static class HeadMeta {
        private int startColumn;
        private int endColumn;

        private int startRow;
        private int endRow;
        private ExcelHead head;
        private HSSFCell cell;
    }
}
