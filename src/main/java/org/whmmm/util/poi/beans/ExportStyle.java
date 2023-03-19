package org.whmmm.util.poi.beans;

import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 * <p> -------------------------- </p>
 * <p> *** author: whmmm | date: 2022/6/30 12:31*** </p>
 *
 * @author whmmm
 */
@Data
public class ExportStyle {

    /**
     * 表头是否加粗, 默认为 true
     */
    private boolean headerFontBold = true;
    /**
     * 表头文字大小, 默认为 12
     */
    private short headerFontSize = 12;

    /**
     * 行高倍率, 默认 1.8
     */
    private double headerRowHeight = 1.8;

    /**
     * 列宽倍率, 默认 1.8
     */
    private double columnWidth = 1.8;

    /**
     * 是否冻结头部, 默认为 true
     */
    private boolean freezeHeaderRow = true;

    /**
     * 是否添加头部筛选, 默认 true
     */
    private boolean addHeaderFilter = true;

    /**
     * 是否固定宽度
     */
    private boolean fixWidth = false;

    public void updateHeight(HSSFRow row) {
        row.setHeight(
            (short) (row.getHeight() * this.getHeaderRowHeight())
        );
    }

}
