package org.whmmm.util.poi;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.whmmm.util.poi.ExcelHeaderParser.ParseHeadResult;
import org.whmmm.util.poi.beans.*;

import javax.annotation.Nullable;
import java.io.OutputStream;
import java.util.*;

/**
 * poi 动态导出 service 基于 <br/>
 * {@code apache poi }
 * <p> -------------------------- </p>
 * <p> author: whmmm          </p>
 * <p> date  : 2023/3/18 15:49 </p>
 *
 * @author whmmm
 */
@Slf4j
public class PoiExportService {
    /**
     * 最大列宽度, 超过此宽度一律设为该宽度 <br/>
     * {@code (0 ~ 255))}
     */
    public static final int maxColumnWidth = 80;

    /**
     * 动态导出 excel,
     * <p color=red>注意: 导出的时 xls 格式</p>,
     * 使用的 api 是 {@link HSSFWorkbook}
     *
     * @param param        请求参数, see {@link ExportRootFormBuilder}, {@link ExportRootForm}
     * @param outputStream 输出流
     */
    public ExportResult dynamicExport(@Nullable ExportRootForm param,
                                      @Nullable OutputStream outputStream) throws Exception {
        if (param == null) {
            return null;
        }

        List<List<DynamicExportColumn>> data = param.getData();

        @Cleanup HSSFWorkbook workbook = new HSSFWorkbook();
        @Cleanup OutputStream ref = outputStream;

        workbook.createSheet();
        HSSFSheet sheet = workbook.getSheetAt(0);


        List<ExcelHead> headLists = param.getHeaders();
        if (headLists == null) {
            List<DynamicExportColumn> firstElement = data.get(0);
            headLists = this.getHeadList(firstElement);
        }

        HeaderInfoDto infoDto = this.writeHead(sheet, headLists, workbook, param);

        this.writeData(workbook, sheet, infoDto, param);

        this.writeDecorateHeader(workbook, sheet, infoDto, param);

        if (outputStream != null) {
            workbook.write(outputStream);
            return null;
        } else {
            // 保存文件到本地
        }

        return null;
    }

    /**
     * 写入数据
     *
     * @param workbook
     * @param sheet
     * @param headerInfo
     * @param param
     */
    private void writeData(HSSFWorkbook workbook,
                           HSSFSheet sheet,
                           HeaderInfoDto headerInfo,
                           ExportRootForm param) {
        List<List<DynamicExportColumn>> data = param.getData();
        if (data == null) {
            return;
        }

        int firstRow = headerInfo.getDataColumn();
        List<Integer> columnIndexList = headerInfo.computeColumnIndexList();

        Map<CellStyleInfo, HSSFCellStyle> styleMap = this.buildCellStyleCacheMap(workbook, data);

        for (List<DynamicExportColumn> item : data) {
            if (item == null) {
                continue;
            }
            this.checkColumnId(item);

            HSSFRow row = sheet.createRow(firstRow);
            param.getExportStyle().updateHeight(row);

            for (Integer columnIndex : columnIndexList) {
                HSSFCell cell = row.createCell(columnIndex);

                DynamicExportColumn it = item.get(columnIndex);
                if (it == null) {
                    continue;
                }

                Object value = it.getValue();

                if (value == null) {
                    value = param.getNullCallback();
                } else if (StrUtil.isBlank(value.toString())) {
                    value = param.nonNullEmptyCallback();
                }


                cell.setCellValue(value.toString());

                HSSFCellStyle style = styleMap.get(
                    new CellStyleInfo(it.getBgColor(), it.getColor())
                );
                if (style != null) {
                    cell.setCellStyle(style);
                }
            }

            firstRow++;
        }

        this.autoSetColumnWidth(sheet, param, columnIndexList, headerInfo);
    }

    private void autoSetColumnWidth(HSSFSheet sheet,
                                    ExportRootForm param,
                                    List<Integer> columnIndexList,
                                    HeaderInfoDto headerInfo) {
        if (param.isFixWidth()) {
            for (Integer columnIndex : columnIndexList) {
                sheet.setColumnWidth(columnIndex,
                                     headerInfo.getHeaderByExcelColumn(columnIndex).getWidth() * 256
                );
            }

            return;
        }

        for (Integer columnIndex : columnIndexList) {
            // 先设置下自动宽度, 防止宽度过小
            sheet.autoSizeColumn(columnIndex, true);

            ExcelHead header = headerInfo.getHeaderByExcelColumn(columnIndex);
            int headerNameLen = header.getName().length() * 2 + 4;

            int width = (int) ((sheet.getColumnWidth(columnIndex) / 256) * param.getExportStyle().getColumnWidth());
            if (width >= maxColumnWidth) {
                int firstRowNum = sheet.getFirstRowNum();
                int lastRowNum = sheet.getLastRowNum();
                while (firstRowNum <= lastRowNum) {
                    HSSFRow row = sheet.getRow(firstRowNum);

                    if (row != null) {
                        HSSFCell cell = row.getCell(columnIndex);
                        if (cell != null) {
                            HSSFCellStyle style = cell.getCellStyle();
                            style.setWrapText(true);
                        }
                    }

                    firstRowNum++;
                }

                width = maxColumnWidth;
            } else if ((width < headerNameLen)) {
                // 处理标题最小宽度的情况, 宽度不能小于标题
                width = headerNameLen;
            }

            sheet.setColumnWidth(columnIndex,
                                 width * 256
            );
        }
    }

    /**
     * 构建样式 map , 防止样式过多导致的错误
     * <p> author: whmmm | date: 2022-07-06 10:50 </p>
     *
     * @param workbook
     * @param data
     * @return
     */
    private Map<CellStyleInfo, HSSFCellStyle> buildCellStyleCacheMap(HSSFWorkbook workbook, List<List<DynamicExportColumn>> data) {
        Map<CellStyleInfo, HSSFCellStyle> result = new HashMap<>();
        for (List<DynamicExportColumn> datum : data) {
            for (DynamicExportColumn it : datum) {

                CellStyleInfo info = new CellStyleInfo();
                info.setBgColor(it.getBgColor());
                info.setColor(it.getColor());

                if (result.containsKey(info)) {
                    continue;
                }

                HSSFCellStyle style = workbook.createCellStyle();
                style.setVerticalAlignment(VerticalAlignment.CENTER);
                style.setAlignment(HorizontalAlignment.CENTER);
                HSSFFont font = workbook.createFont();
                style.setFont(font);

                // 背景颜色填充
                if (it.getBgColor() != null) {
                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    style.setFillForegroundColor(it.getBgColor());
                } else {
                    style.setFillPattern(FillPatternType.NO_FILL);
                    style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
                }

                // 颜色填充
                if (it.getColor() != null) {
                    font.setColor(it.getColor());
                } else {
                    font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
                }

                result.put(info, style);
            }
        }
        return result;
    }

    /**
     * 检查 columnId 是否合法, 如果为 null 则自动设置一个
     *
     * @param item
     */
    private void checkColumnId(List<DynamicExportColumn> item) {
        int autoId = 0;
        for (DynamicExportColumn exportColumn : item) {
            if (exportColumn.getColumnId() == null) {
                exportColumn.setColumnId(++autoId);
            }
        }
    }


    private HeaderInfoDto writeHead(HSSFSheet sheet,
                                    List<ExcelHead> headLists,
                                    HSSFWorkbook workbook,
                                    ExportRootForm form) {
        ExportStyle exportStyle = form.getExportStyle();

        ParseHeadResult headResult = ExcelHeaderParser.parseList(headLists);

        int maxDeep = headResult.getRowLen();

        int startRow = 0;
        if (form.getTitle() != null &&
            !"".equalsIgnoreCase(form.getTitle())) {
            // 表头不为空, 则创建表头
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(form.getTitle());
            startRow = 1;
        }

        HeaderInfoDto result = new HeaderInfoDto();
        final int headColumn = maxDeep + startRow;
        result.setDataColumn(headColumn);

        boolean needMerge = maxDeep > 1;

        // 创建 行
        for (int i = startRow; i < headColumn; i++) {
            sheet.createRow(i);
        }

        int columnIndex = headResult.getColLen();

        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();

        font.setFontHeightInPoints(exportStyle.getHeaderFontSize());
        font.setBold(exportStyle.isHeaderFontBold());

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);

        // 写入表头, 循环行列  只为创建 cell
        for (int i = 0; i < headResult.getRowLen(); i++) {
            HSSFRow row = sheet.getRow(startRow + i);
            for (int j = 0; j < headResult.getColLen(); j++) {
                HSSFCell cell = row.createCell(j);
                cell.setCellStyle(style);
            }
        }
        // 第二次循环执行合并, 因为第一次只是创建 cell , 并未设置内容
        for (int i = 0; i < headResult.getRowLen(); i++) {
            int rowIndex = startRow + i;
            HSSFRow row = sheet.getRow(rowIndex);
            for (int j = 0; j < headResult.getColLen(); j++) {
                HSSFCell cell = row.getCell(j);
                ExcelHeaderParser.ParseHeadInfo info = headResult.getInfo(i, j);
                if (info != null) {
                    cell.setCellValue(info.getLabel());
                    if (info.isLeaf()) {
                        ExcelHead head = info.getHead();
                        result.putMapping(j, head.getColumnId());
                        result.getExcelColumnHeadMap().put(j, head);
                    }

                    if (info.isNeedMerge()) {
                        CellRangeAddress address = new CellRangeAddress(
                            rowIndex,
                            rowIndex + info.getCrossRow(),
                            j,
                            j + info.getCrossCol()
                        );

                        sheet.addMergedRegion(address);
                    }
                }
            }
        }


        // 更新行高
        for (int i = 0; i < maxDeep; i++) {
            exportStyle.updateHeight(sheet.getRow(i));
        }
        if (form.getTitle() != null &&
            !"".equalsIgnoreCase(form.getTitle())) {
            // 合并表头
            sheet.addMergedRegion(
                new CellRangeAddress(0, 0, 0, columnIndex - 1)
            );

            HSSFCell titleCell = sheet.getRow(0).getCell(0);
            HSSFCellStyle cellStyle = titleCell.getCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            HSSFFont hssfFont = workbook.createFont();
            hssfFont.setBold(true);
            hssfFont.setFontHeightInPoints((short) 16);
            cellStyle.setFont(hssfFont);

            titleCell.setCellStyle(cellStyle);
        }
        result.setAutoColumnId(headResult.getColLen());

        return result;
    }

    /**
     * 获取头部
     * <p> author: whmmm | date: 2022-06-30 10:57 </p>
     *
     * @param columns
     * @return
     */
    private List<ExcelHead> getHeadList(List<DynamicExportColumn> columns) {
        if (columns == null || columns.isEmpty()) {
            return Collections.emptyList();
        }

        List<ExcelHead> list = new ArrayList<>();
        for (DynamicExportColumn column : columns) {
            ExcelHead head = new ExcelHead();
            head.setOrder(column.getOrder());
            head.setName(column.getName());
            head.setChildHead(this.getHeadList(column.getSubColumns()));
            list.add(head);
        }

        return list;
    }


    /**
     * 装饰表头, 比如单表头分割
     *
     * @param workbook   xls 对象
     * @param sheet      sheet 对象
     * @param headerInfo 表头相关信息
     * @param param      生成 excel 的相关参数
     */
    private void writeDecorateHeader(HSSFWorkbook workbook,
                                     HSSFSheet sheet,
                                     HeaderInfoDto headerInfo,
                                     ExportRootForm param) {
        List<HeaderInfoDto.HeadMeta> metaList = headerInfo.getHeadMetaList();

        ExportStyle style = param.getExportStyle();
        if (style != null) {
            int firstDataColumn = headerInfo.getDataColumn();
            int lastHeaderRow = firstDataColumn - 1;
            if (style.isFreezeHeaderRow()) {
                sheet.createFreezePane(0,
                                       firstDataColumn,
                                       0,
                                       firstDataColumn);
            }
            if (style.isAddHeaderFilter()) {
                String addr = PoiConvertUtil.toExcelAddr(lastHeaderRow, 0, headerInfo.getMaxColumn());
                sheet.setAutoFilter(CellRangeAddress.valueOf(addr));
            }
        }

        HSSFCreationHelper helper = workbook.getCreationHelper();
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

        for (HeaderInfoDto.HeadMeta meta : metaList) {
            ExcelHead head = meta.getHead();
            if (head.hasSepName()) {
                HSSFClientAnchor anchor = helper.createClientAnchor();
                // 设置斜线的开始位置
                anchor.setCol1(meta.getStartColumn());
                anchor.setRow1(meta.getStartRow());
                // 设置斜线的结束位置
                anchor.setCol2(meta.getEndColumn());
                anchor.setRow2(meta.getEndRow());

                HSSFSimpleShape shape = patriarch.createSimpleShape(anchor);
                // 设置形状类型为线型
                shape.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
                shape.setLineStyle(HSSFSimpleShape.LINESTYLE_SOLID);
                // 设置线宽
                shape.setLineWidth(1);
                // 设置线的风格
                shape.setLineStyle(0);
                // 设置线的颜色
                shape.setLineStyleColor(0, 0, 0);

                meta.getCell().setCellValue(head.gainSepNameString());
            }
        }


    }
}
