package org.whmmm.util.test.test.threadlocal;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/6/14 15:23 </b></p>
 *
 * @author whmmm
 */
public class Stackoverflow {

    /**
     * <pre>{@code
     * # 限制内存测试
     * java.exe -ea -Xmx20m
     * # 断点
     *
     * }</pre>
     *
     * @throws Exception
     */
    @Test
    public void testOverflow() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        while (true) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    ThreadLocalClass clazz = new ThreadLocalClass();
                    // clazz.remove();
                }
            });
            Thread.sleep(1 * 1000);
        }
    }
}

class ThreadLocalClass {
    public static final ThreadLocal<byte[]> THREAD_LOCAL = new ThreadLocal<>();

    public ThreadLocalClass() {
        byte[] bytes = new byte[1024 * 1024];
        THREAD_LOCAL.set(bytes);
    }

    public void remove() {
        THREAD_LOCAL.remove();
    }
}