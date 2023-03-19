package org.whmmm.util.poi.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p> -------------------------- </p>
 * <p> *** author: whmmm | date: 2022/6/30 9:49*** </p>
 *
 * @author whmmm
 */
@Data
@NoArgsConstructor
public class DynamicExportColumn implements Serializable {
    /**
     * 列名
     */
    private String name;

    /**
     * 列 id, 唯一值即可, 无其他限制, 如果没有传递, 则自动累加
     */
    private Integer columnId;

    /**
     * 值
     */
    private Object value;

    /**
     * 文字颜色, 10 进制
     */
    private Short color;

    /**
     * 背景颜色, 10 进制
     */
    private Short bgColor;

    /**
     * 列排序
     */
    private int order;

    /**
     * 子列, 如果传递了 {@link ExcelHead} 表头参数,
     * 则可以不传这个
     */
    private List<DynamicExportColumn> subColumns;

    public DynamicExportColumn(Object value) {
        this.value = value;
    }
}
