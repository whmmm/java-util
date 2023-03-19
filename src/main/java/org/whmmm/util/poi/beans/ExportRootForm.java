package org.whmmm.util.poi.beans;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p> -------------------------- </p>
 * <p> *** author: whmmm | date: 2022/6/30 9:43*** </p>
 *
 * @author whmmm
 */
@Data
public class ExportRootForm implements Serializable {

    /**
     * 请求参数集合
     */
    private List<ExcelHead> headers;

    /**
     * 数据集合, 以第一列作为 头填充
     */
    private List<List<DynamicExportColumn>> data;


    private ExportStyle exportStyle = new ExportStyle();

    /**
     * 值为 null 时回显的值
     */
    private String nullCallback = "--";

    /**
     * 导出文件名字
     */
    private String title;

    /**
     * 空字符串值时填充的值
     */
    private String emptyCallback = "--";


    public String getNullCallback() {
        if (nullCallback == null) {
            return "";
        }
        return nullCallback;
    }

    public String nonNullEmptyCallback() {
        if (emptyCallback == null) {
            return "";
        }
        return emptyCallback;
    }

    /**
     * 是否固定表头宽度, 默认为 false
     *
     * @return ~
     */
    public boolean isFixWidth() {
        if (this.getExportStyle() == null) {
            return false;
        }

        return this.getExportStyle().isFixWidth();
    }
}
