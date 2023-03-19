package org.whmmm.util.poi.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> -------------------------- </p>
 * <p> *** author: whmmm | date: 2022/6/30 13:07*** </p>
 *
 * @author whmmm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeItem<T> {
    private int deep;
    private T data;
}
