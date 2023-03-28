package org.whmmm.util.poi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.whmmm.util.poi.beans.ExportRootForm;
import org.whmmm.util.poi.beans.ExportRootFormBuilder;
import org.whmmm.util.poi.beans.ExportRootFormBuilder.Header;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> -------------------------- </p>
 * <p> author: whmmm </p>
 * <p> date  : 2023/3/18 16:30 </p>
 *
 * @author whmmm
 */
public class PoiTest {
    private String josnStr = "{\"headers\":[{\"name\":\"序号\"},{\"name\":\"教师\"},{\"name\":\"年级\"},{\"name\":\"学科\"},{\"name\":\"上传资源\"},{\"name\":\"资源浏览(人次)\"},{\"name\":\"回复量\"},{\"name\":\"评论量\"},{\"name\":\"获得评论\"},{\"name\":\"送鲜花\"},{\"name\":\"获得鲜花\"},{\"name\":\"总活跃度\"}],\"title\":\"测试编辑分工-活动成员统计\",\"data\":[[{\"value\":1},{\"value\":\"邢甲\"},{\"value\":\"一年级,二年级,五年级,初一,高一年级,高二年级,高三年级\"},{\"value\":\"语文,数学,英语,物理,化学,生物\"},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0}],[{\"value\":2},{\"value\":\"翟虎坡\"},{\"value\":\"初一\"},{\"value\":\"语文\"},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0}],[{\"value\":3},{\"value\":\"张鹏浩\"},{\"value\":\"初一,高一年级\"},{\"value\":\"语文,数学,物理\"},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0}],[{\"value\":4},{\"value\":\"闫慧敏\"},{},{\"value\":\"语文\"},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0}],[{\"value\":5},{\"value\":\"王万荣\"},{\"value\":\"初一,高一年级\"},{\"value\":\"语文,数学,物理,化学\"},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0}],[{\"value\":6},{\"value\":\"季小倩T\"},{\"value\":\"初一,高一年级\"},{\"value\":\"语文,数学,英语,物理,化学,生物\"},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0}],[{\"value\":7},{\"value\":\"张静\"},{\"value\":\"初一,高一年级\"},{\"value\":\"语文,英语,化学\"},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0},{\"value\":0}]],\"exportStyle\":{\"headerRowHeight\":2,\"columnWidth\":2.3}}";

    private String multiHeader = "{\"headers\":[{\"name\":\"班级\",\"childHead\":[]},{\"name\":\"总分\",\"childHead\":[{\"name\":\"平均分\"},{\"name\":\"平均得分率\"}]},{\"name\":\"语文\",\"childHead\":[{\"name\":\"平均分\"},{\"name\":\"平均得分率\"}]},{\"name\":\"数学\",\"childHead\":[{\"name\":\"平均分\"},{\"name\":\"平均得分率\"}]},{\"name\":\"英语\",\"childHead\":[{\"name\":\"平均分\"},{\"name\":\"平均得分率\"}]},{\"name\":\"生物\",\"childHead\":[{\"name\":\"平均分\"},{\"name\":\"平均得分率\"}]},{\"name\":\"历史\",\"childHead\":[{\"name\":\"平均分\"},{\"name\":\"平均得分率\"}]},{\"name\":\"地理\",\"childHead\":[{\"name\":\"平均分\"},{\"name\":\"平均得分率\"}]},{\"name\":\"道德与法治\",\"childHead\":[{\"name\":\"平均分\"},{\"name\":\"平均得分率\"}]}],\"data\":[[{\"value\":\"全部\"},{\"value\":475.83},{\"value\":\"0.79%\"},{\"value\":65.01},{\"value\":\"60.31%\"},{\"value\":65.92},{\"value\":\"72.36%\"},{\"value\":81.27},{\"value\":\"85.42%\"},{\"value\":62.59},{\"value\":\"59.48%\"},{\"value\":62.79},{\"value\":\"72.06%\"},{\"value\":68.36},{\"value\":\"67.18%\"},{\"value\":73.13},{\"value\":\"84.01%\"}],[{\"value\":\"七年级1班\"},{\"value\":467.57},{\"value\":\"0.78%\"},{\"value\":66.17},{\"value\":\"61.03%\"},{\"value\":65.59},{\"value\":\"71.96%\"},{\"value\":81.6},{\"value\":\"85.56%\"},{\"value\":60.02},{\"value\":\"56.61%\"},{\"value\":59.44},{\"value\":\"69.72%\"},{\"value\":67.11},{\"value\":\"66.15%\"},{\"value\":71.89},{\"value\":\"83.1%\"}],[{\"value\":\"七年级2班\"},{\"value\":485.38},{\"value\":\"0.81%\"},{\"value\":66.05},{\"value\":\"62.08%\"},{\"value\":67.98},{\"value\":\"74.04%\"},{\"value\":84.07},{\"value\":\"88.09%\"},{\"value\":65.66},{\"value\":\"63.27%\"},{\"value\":61.79},{\"value\":\"71.77%\"},{\"value\":70.02},{\"value\":\"67.85%\"},{\"value\":74.57},{\"value\":\"84.89%\"}],[{\"value\":\"七年级3班\"},{\"value\":471.58},{\"value\":\"0.79%\"},{\"value\":61.83},{\"value\":\"56.23%\"},{\"value\":66.86},{\"value\":\"73.48%\"},{\"value\":76.24},{\"value\":\"81.17%\"},{\"value\":59.44},{\"value\":\"54.6%\"},{\"value\":61.77},{\"value\":\"71.32%\"},{\"value\":67.21},{\"value\":\"65.95%\"},{\"value\":78.23},{\"value\":\"87.06%\"}],[{\"value\":\"七年级4班\"},{\"value\":487.28},{\"value\":\"0.81%\"},{\"value\":63.01},{\"value\":\"58.34%\"},{\"value\":67.13},{\"value\":\"73.52%\"},{\"value\":78.73},{\"value\":\"83.43%\"},{\"value\":64.24},{\"value\":\"60.27%\"},{\"value\":68.15},{\"value\":\"76.58%\"},{\"value\":69.96},{\"value\":\"68.05%\"},{\"value\":80.28},{\"value\":\"88.23%\"}],[{\"value\":\"七年级5班\"},{\"value\":454.95},{\"value\":\"0.76%\"},{\"value\":65.65},{\"value\":\"60.54%\"},{\"value\":65.68},{\"value\":\"72.7%\"},{\"value\":76.64},{\"value\":\"81.28%\"},{\"value\":63.6},{\"value\":\"60.35%\"},{\"value\":58.75},{\"value\":\"68.42%\"},{\"value\":68.75},{\"value\":\"67.72%\"},{\"value\":70.53},{\"value\":\"82.47%\"}],[{\"value\":\"七年级6班\"},{\"value\":468.88},{\"value\":\"0.78%\"},{\"value\":61.46},{\"value\":\"57.38%\"},{\"value\":65.33},{\"value\":\"71.57%\"},{\"value\":80.17},{\"value\":\"84.03%\"},{\"value\":60.7},{\"value\":\"58.43%\"},{\"value\":60.98},{\"value\":\"69.39%\"},{\"value\":69.36},{\"value\":\"68.63%\"},{\"value\":72.28},{\"value\":\"83.17%\"}],[{\"value\":\"七年级7班\"},{\"value\":479.18},{\"value\":\"0.8%\"},{\"value\":63.38},{\"value\":\"58.27%\"},{\"value\":67.4},{\"value\":\"74.01%\"},{\"value\":83.46},{\"value\":\"87.24%\"},{\"value\":61.62},{\"value\":\"60.04%\"},{\"value\":60.98},{\"value\":\"69.51%\"},{\"value\":68.67},{\"value\":\"67.74%\"},{\"value\":73.67},{\"value\":\"85.21%\"}],[{\"value\":\"七年级8班\"},{\"value\":473.73},{\"value\":\"0.79%\"},{\"value\":66.21},{\"value\":\"61.44%\"},{\"value\":63.87},{\"value\":\"69.59%\"},{\"value\":84.24},{\"value\":\"88.29%\"},{\"value\":62.33},{\"value\":\"59.89%\"},{\"value\":63.15},{\"value\":\"72%\"},{\"value\":67.8},{\"value\":\"67.68%\"},{\"value\":70.3},{\"value\":\"82.11%\"}],[{\"value\":\"七年级9班\"},{\"value\":487.71},{\"value\":\"0.81%\"},{\"value\":69.14},{\"value\":\"64.83%\"},{\"value\":64.13},{\"value\":\"70.83%\"},{\"value\":85.03},{\"value\":\"88.84%\"},{\"value\":63.98},{\"value\":\"59.88%\"},{\"value\":66.22},{\"value\":\"76.04%\"},{\"value\":67.67},{\"value\":\"65.69%\"},{\"value\":71.53},{\"value\":\"83.21%\"}],[{\"value\":\"七年级10班\"},{\"value\":481.04},{\"value\":\"0.8%\"},{\"value\":67.18},{\"value\":\"62.99%\"},{\"value\":65.19},{\"value\":\"71.91%\"},{\"value\":82.55},{\"value\":\"86.23%\"},{\"value\":64.31},{\"value\":\"61.5%\"},{\"value\":66.71},{\"value\":\"75.9%\"},{\"value\":67.04},{\"value\":\"66.32%\"},{\"value\":68.06},{\"value\":\"80.68%\"}]],\"title\":\"校级分析报告-学科考试基本表现\",\"exportStyle\":{\"headerRowHeight\":2,\"columnWidth\":2.3}}";

    @Test
    public void testExport() throws Exception {
        Gson gson = new Gson();

        ExportRootForm param = gson.fromJson(josnStr, ExportRootForm.class);

        String path = "单行表头-动态数据导出.xls";
        PoiExportService service = new PoiExportService();
        service.dynamicExport(param, new FileOutputStream(path));
    }

    @Test
    public void testConvert() {

        String s = PoiConvertUtil.toExcelCol(26);
        System.out.println(s);

        int num = PoiConvertUtil.toPoiColNum("C");
        System.out.println(num);
    }

    @Test
    public void testMultiHeader() throws Exception {
        PoiExportService service = new PoiExportService();
        Gson gson = new Gson();
        ExportRootForm form = gson.fromJson(multiHeader, ExportRootForm.class);


        service.dynamicExport(form, new FileOutputStream("多级表头.xlsx"));


    }


    /**
     * 导出下面的列表 (三级表头)
     * <table border="1" style="text-align:center">
     *     <tr>
     *         <th>班级</th>
     *         <th>学生</th>
     *         <th colspan="3">语文</th>
     *         <th>数学</th>
     *         <th>英语</th>
     *     </tr>
     *     <tr>
     *         <th></th>
     *         <th></th>
     *         <th colspan="2">平均分</th>
     *         <th>平均得分率</th>
     *         <th></th>
     *         <th></th>
     *     </tr>
     *     <tr>
     *         <th></th>
     *         <th></th>
     *         <th>平均分(原始分)</th>
     *         <th>平均分(赋分)</th>
     *         <th>平均得分率</th>
     *         <th></th>
     *         <th></th>
     *     </tr>
     * </table>
     */
    @Test
    public void testJavaExport() throws Exception {
        Gson gson = new Gson();
        TypeToken<List<StuScore>> token = new TypeToken<List<StuScore>>() {
        };

        String jsonStr = "[{\"className\":\"初一1班\",\"stuName\":\"A\",\"scoreList\":[{\"id\":1,\"scoreAvg\":100,\"scoreRankAvg\":80,\"scoreRateAvg\":10},{\"id\":2,\"scoreAvg\":100,\"scoreRankAvg\":80,\"scoreRateAvg\":10},{\"id\":3,\"scoreAvg\":100,\"scoreRankAvg\":80,\"scoreRateAvg\":10}]},{\"className\":\"初一1班\",\"stuName\":\"B\",\"scoreList\":[{\"id\":1,\"scoreAvg\":100,\"scoreRankAvg\":80,\"scoreRateAvg\":10},{\"id\":2,\"scoreAvg\":100,\"scoreRankAvg\":80,\"scoreRateAvg\":10},{\"id\":3,\"scoreAvg\":100,\"scoreRankAvg\":80,\"scoreRateAvg\":10}]},{\"className\":\"初一2班\",\"stuName\":\"C\",\"scoreList\":[{\"id\":1,\"scoreAvg\":100,\"scoreRankAvg\":80,\"scoreRateAvg\":10},{\"id\":2,\"scoreAvg\":100,\"scoreRankAvg\":80,\"scoreRateAvg\":10},{\"id\":3,\"scoreAvg\":100,\"scoreRankAvg\":80,\"scoreRateAvg\":10}]},{\"className\":\"初一3班\",\"stuName\":\"D\",\"scoreList\":[{\"id\":1,\"scoreAvg\":100,\"scoreRankAvg\":80,\"scoreRateAvg\":10},{\"id\":2,\"scoreAvg\":100,\"scoreRankAvg\":80,\"scoreRateAvg\":10},{\"id\":3,\"scoreAvg\":100,\"scoreRankAvg\":80,\"scoreRateAvg\":10}]},{\"className\":\"初一1班\",\"stuName\":\"E\",\"scoreList\":[{\"id\":1,\"scoreAvg\":69,\"scoreRankAvg\":12,\"scoreRateAvg\":23},{\"id\":3,\"scoreAvg\":45,\"scoreRankAvg\":36,\"scoreRateAvg\":97}]}]";

        List<StuScore> list = gson.fromJson(jsonStr, token);

        ExportRootFormBuilder<StuScore> builder = ExportRootFormBuilder
            .<StuScore>newInstance()
            .dataList(list)
            .title("测试3级表头")
            .header("班级", it -> it.getData().getClassName())
            .header("学生", it -> it.getData().getStuName())
            .header("语文", this.buildMultiHeader(1))
            .header("数学", this.buildMultiHeader(2))
            .header("英语", this.buildMultiHeader(3));

        ExportRootForm param = builder.build();

        ExcelHeaderParser.ParseHeadResult result = ExcelHeaderParser.parseList(param.getHeaders());

        PoiExportService service = new PoiExportService();
        service.dynamicExport(param, new FileOutputStream("三级表头-java导出.xls"));
    }

    protected List<Header<StuScore>> buildMultiHeader(int subjectId) {
        List<Header<StuScore>> list = new ArrayList<>();

        Header<StuScore> avgScore = new Header<>("平均分");
        List<Header<StuScore>> scoreChild = new ArrayList<>();
        scoreChild.add(
            new Header<>("平均分(原始分)",
                         it -> {
                return it.getData().getSubjectScore(subjectId).getScoreAvg();
                         }));
        scoreChild.add(
            new Header<>("平均分(赋分)",
                         it -> {
                return it.getData().getSubjectScore(subjectId).getScoreRankAvg();
                         }
            ));
        avgScore.setChildList(scoreChild);


        Header<StuScore> scoreRate = new Header<>("平均得分率");
        scoreRate.setValueGetter(
            it -> {
                return it.getData().getSubjectScore(subjectId).getScoreRateAvg();
            }
        );

        list.add(avgScore);
        list.add(scoreRate);

        return list;
    }

    @Test
    public void testError() throws Exception{
        String str = "{\"headers\":[{\"name\":\"姓名\",\"childHead\":[]},{\"name\":\"排名\",\"childHead\":[]},{\"name\":\"班级\",\"childHead\":[]},{\"name\":\"总分\",\"childHead\":[]},{\"name\":\"数学\",\"childHead\":[]},{\"name\":\"物理\",\"childHead\":[]},{\"name\":\"生物\",\"childHead\":[]}],\"data\":[[{\"value\":\"学生01\"},{\"value\":1},{\"value\":\"精准教学测试（1）班\"},{\"value\":\"12\"},{\"value\":12}],[{\"value\":\"学生03\"},{\"value\":1},{\"value\":\"精准教学测试（1）班\"},{\"value\":\"12\"},{\"value\":12}],[{\"value\":\"学生07\"},{\"value\":3},{\"value\":\"精准教学测试（1）班\"},{\"value\":\"10\"},{\"value\":10}],[{\"value\":\"学生09\"},{\"value\":3},{\"value\":\"精准教学测试（1）班\"},{\"value\":\"10\"},{\"value\":10}],[{\"value\":\"学生02\"},{\"value\":3},{\"value\":\"精准教学测试（1）班\"},{\"value\":\"10\"},{\"value\":10}],[{\"value\":\"学生06\"},{\"value\":6},{\"value\":\"精准教学测试（1）班\"},{\"value\":\"4\"},{\"value\":4}],[{\"value\":\"学生08\"},{\"value\":6},{\"value\":\"精准教学测试（1）班\"},{\"value\":\"4\"},{\"value\":4}],[{\"value\":\"学生05\"},{\"value\":8},{\"value\":\"精准教学测试（1）班\"},{\"value\":\"2\"},{\"value\":2}]],\"title\":\"校级分析报告-重点学生信息前十\",\"exportStyle\":{\"headerRowHeight\":2,\"columnWidth\":2.3}}";
        Gson gson = new Gson();

        ExportRootForm form = gson.fromJson(str, ExportRootForm.class);

        PoiExportService service = new PoiExportService();

        service.dynamicExport(form, new FileOutputStream("报错.xls"));
    }
}
