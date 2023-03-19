package org.whmmm.util.poi.beans;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p> -------------------------- </p>
 * <p> *** author: whmmm | date: 2022/6/30 10:55*** </p>
 *
 * @author whmmm
 */
@Data
@NoArgsConstructor
public class ExcelHead {
    public ExcelHead(String name) {
        this.name = name;
    }

    /**
     * 排序
     */
    private int order;

    /**
     * 列 id, 唯一值即可, 无其他限制, 如果没有传递, 则自动累加
     */
    private Integer columnId;

    /**
     * 表头名称
     */
    private String name;

    /**
     * 单表头分割线名称 例如 难度\题目 这种同一个表头含有两个名称
     */
    private List<String> sepName;

    /**
     * 子列
     */
    private List<ExcelHead> childHead;


    /**
     * 宽度, 0-255 之间的数值, 固定值
     */
    private int width;

    /**
     * 深度 map
     */
    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private Map<Integer, List<ExcelHead>> deepHeadMap = new HashMap<>();


    public boolean hasChild() {
        return childHead != null && childHead.size() > 0;
    }

    public boolean hasNotChild() {
        return !this.hasChild();
    }

    /**
     * 是否含有分割表头信息
     *
     * @return
     */
    public boolean hasSepName() {
        return this.getSepName() != null && this.getSepName().size() > 0;
    }

    /**
     * 获取格式化后的 分割 表头字符串
     *
     * @return
     */
    public String gainSepNameString() {
        if (!this.hasSepName()) {
            return "";
        }
        return this.getSepName().stream().collect(Collectors.joining("   "));
    }
}
