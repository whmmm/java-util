package org.whmmm.util.poi.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 本类的作用是作为 样式缓存 key 使用
 * <p> -------------------------- </p>
 * <p> *** author: whmmm | date: 2022/7/6 10:46*** </p>
 *
 * @author whmmm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CellStyleInfo implements Serializable {
    private Short bgColor;
    private Short color;
}
