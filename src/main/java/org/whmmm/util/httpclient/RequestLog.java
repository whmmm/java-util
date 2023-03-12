package org.whmmm.util.httpclient;

import com.google.gson.Gson;
import lombok.Data;

import javax.annotation.Nullable;

/**
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/2/13 12:48 </b></p>
 *
 * @author whmmm
 */
@Data
public final class RequestLog {

    public static final Gson GSON = DeclareClientFactory.gson();

    private String requestId;

    private String url;

    /**
     * GET | POST
     */
    private String type;

    /**
     * {@code} PostForm 或者 get 时 的参数
     */
    private String param;

    /**
     * {@code post json} 时的参数
     * {@link org.springframework.web.bind.annotation.RequestBody}
     * <br/>
     * 中绑定的对象, <br/>
     * <span color=red>这里的值不一定等于前端传递的值. </span>
     * <br/>
     */
    @Nullable
    private Object body;
    /**
     * 是否限制打印日志, 默认为 false
     */
    private boolean logLimitUsable = false;
    private int maxBodyLen = 800;

    /**
     * 响应值
     */
    @Nullable
    private String result;

    public String dumpToLogStr(StringBuilder sb) {
        sb.append("### ").append("req_id:").append(requestId);
        String lineSeparator = System.lineSeparator();
        sb.append("-- http log --").append(lineSeparator);
        sb.append(type).append("  ").append(url);
        if (body == null) {
            sb.append(lineSeparator);
            sb.append(lineSeparator);
            sb.append(param);
        } else {
            sb.append(lineSeparator);
            sb.append("Content-Type: application/json");
            sb.append(lineSeparator);
            sb.append(lineSeparator);
            String str = null;
            if (body instanceof String) {
                str = body.toString();
            } else {
                str = GSON.toJson(body);
            }

            if (this.logLimitUsable && str.length() >= maxBodyLen) {
                // 最多打印 800 个字符, 太多会影响性能！！
                str = str.substring(0, maxBodyLen) +
                      "......(超出限制:" + maxBodyLen + ")";
            }
            sb.append(str);
        }
        sb.append(lineSeparator);
        if (result != null) {
            sb.append(lineSeparator);
            if (this.logLimitUsable && result.length() >= maxBodyLen) {
                // 最多打印 800 个字符, 太多会影响性能！！
                result = result.substring(0, maxBodyLen) +
                         "......(超出限制:" + maxBodyLen + ")";
            }
            sb.append("响应的结果为: ").append(result);
            sb.append(lineSeparator);
        }

        return sb.toString();
    }


    public String dumpToLogStr() {
        return this.dumpToLogStr(new StringBuilder());
    }
}
