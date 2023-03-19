package org.whmmm.util.poi.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p> -------------------------- </p>
 * <p> author: whmmm </p>
 * <p> date  : 2023/3/18 18:44 </p>
 *
 * @author whmmm
 */
public final class ExportRootFormBuilder<T> {
    /**
     * 要导出的原数据集合
     */
    private Collection<T> dataList = Collections.emptyList();
    /**
     * 要导出的样式和其他相关设置
     */
    private ExportStyle style = new ExportStyle();

    private String title;

    private List<Header<T>> headerList = new ArrayList<>();

    /**
     * <pre>{@code
     * // 通过下面的方法, 创建一个带泛型的 builder
     * ExportRootFormBuilder.<String>newInstance()
     * }</pre>
     *
     * @param <T>
     * @return
     */
    public static <T> ExportRootFormBuilder<T> newInstance() {
        return new ExportRootFormBuilder<>();
    }

    /**
     * 添加导出表头
     *
     * @param name   名字
     * @param getter 取值方法
     * @return -
     */
    public ExportRootFormBuilder<T> header(String name,
                                           Function<ValueGetterContext<T>, Object> getter) {

        Header<T> header = new Header<>(name, getter);
        this.headerList.add(header);
        return this;
    }

    /**
     * 添加表头
     *
     * @param name        名字
     * @param childHeader 子表头信息
     * @return -
     */
    public ExportRootFormBuilder<T> header(String name,
                                           List<Header<T>> childHeader) {

        Header<T> header = new Header<>(name);
        header.setChildList(childHeader);

        this.headerList.add(header);
        return this;
    }

    public ExportRootFormBuilder<T> dataList(Collection<T> dataList) {
        this.dataList = dataList;
        return this;
    }

    public ExportRootFormBuilder<T> title(String title) {
        this.title = title;
        return this;
    }

    public ExportRootForm build() {
        ExportRootForm form = new ExportRootForm();

        form.setHeaders(this.headerList.stream()
                                       .map(Header::getExcelHead)
                                       .collect(Collectors.toList())
        );
        List<List<DynamicExportColumn>> rowList = new ArrayList<>();
        form.setData(rowList);

        List<Header<T>> spreadList = new ArrayList<>();
        this.getSpreadHeader(spreadList, this.headerList);

        int dataCursor = 0;
        for (T t : this.dataList) {
            List<DynamicExportColumn> row = new ArrayList<>();
            rowList.add(row);

            for (Header<T> header : spreadList) {
                Function<ValueGetterContext<T>, Object> getter = header.getValueGetter();
                Object value = "";
                if (getter != null) {
                    value = getter.apply(new ValueGetterContext<>(dataCursor, t));
                }
                row.add(new DynamicExportColumn(value));
            }
            dataCursor++;
        }

        if (null != this.title &&
            !"".equalsIgnoreCase(this.title)) {
            form.setTitle(this.title);
        }

        return form;
    }


    private void getSpreadHeader(List<Header<T>> container,
                                 List<Header<T>> treeList) {
        for (Header<T> header : treeList) {
            List<Header<T>> childList = header.getChildList();
            if (childList != null &&
                !childList.isEmpty()) {
                getSpreadHeader(container, childList);
            } else {
                container.add(header);
            }
        }

    }

    /**
     * 关联 {@link ExportRootFormBuilder}
     *
     * @param <T>
     */
    @Data
    @NoArgsConstructor
    public static class Header<T> {
        public Header(String name) {

            this.excelHead = new ExcelHead(name);
        }

        public Header(String name,
                      Function<ValueGetterContext<T>, Object> getter) {
            this(name);
            this.setValueGetter(getter);
        }

        private ExcelHead excelHead;
        @Nullable
        private Function<ValueGetterContext<T>, Object> valueGetter;

        private List<Header<T>> childList;

        /**
         *
         */
        public void setChildList(List<Header<T>> childList) {
            if (childList == null || childList.isEmpty()) {
                return;
            }

            ExcelHead excelHead = this.getExcelHead();

            excelHead.setChildHead(
                childList.stream()
                         .map(Header::getExcelHead)
                         .collect(Collectors.toList())
            );

            this.childList = childList;
        }
    }

    @Data
    public static class ValueGetterContext<T> {
        /**
         * 从 0 开始
         */
        private int index;
        private T data;

        public ValueGetterContext(int index, T data) {
            this.index = index;
            this.data = data;
        }
    }
}
