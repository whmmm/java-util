package org.whmmm.util.httpclient;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

/**
 * 重试 (only support ApacheHttpClient)
 * <a href="https://blog.csdn.net/zzhongcy/article/details/104555647">csdn</a>
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/3/13 16:39 </b></p>
 *
 * @author whmmm
 */
class CustomRetryHandler extends DefaultHttpRequestRetryHandler {

    protected static final Collection<Class<? extends IOException>> ignoredExceptions =
        Arrays.asList(
            ConnectTimeoutException.class,
            UnknownHostException.class
        );

    public CustomRetryHandler(int retryCount) {
        this(retryCount, true, ignoredExceptions);
    }

    public CustomRetryHandler(int retryCount,
                              boolean requestSentRetryEnabled,
                              Collection<Class<? extends IOException>> clazzes) {
        super(retryCount, requestSentRetryEnabled, clazzes);
    }
}
