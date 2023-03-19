package org.whmmm.util.poi;

import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p> -------------------------- </p>
 * <p> author: whmmm </p>
 * <p> date  : 2023/3/18 19:46 </p>
 *
 * @author whmmm
 */
@Data
public class StuScore {

    private String className;
    private String stuName;

    private List<Score> scoreList = Collections.emptyList();


    public Score getSubjectScore(int subjectId) {
        return this.getScoreList()
                   .stream()
                   .filter(it -> Objects.equals(it.getId(), subjectId))
                   .findFirst()
                   .orElse(new Score());
    }

    @Data
    public static class Score {
        // 学科 id
        private Integer id;
        // 平均分
        private Double scoreAvg;
        // 等级赋分平均分
        private Double scoreRankAvg;
        // 平均得分率
        private Double scoreRateAvg;
    }

}
