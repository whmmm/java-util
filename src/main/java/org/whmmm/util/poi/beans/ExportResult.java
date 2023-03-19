package org.whmmm.util.poi.beans;

import lombok.Data;

import java.io.Serializable;

/**
 * 导出结果 dto
 * <p> -------------------------- </p>
 * <p> *** author: whmmm | date: 2022/6/30 9:40*** </p>
 *
 * @author whmmm
 */
@Data
public class ExportResult implements Serializable {
    /**
     * http 访问路径
     */
    private String url;

    /**
     * 相对路径
     */
    private String relativePath;
}
