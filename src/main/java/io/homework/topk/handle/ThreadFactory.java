package io.homework.topk.handle;

import io.homework.topk.model.Constants;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jingdi on 2019/5/29
 */
public class ThreadFactory {

    public static ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<>(Constants.FILE_NUM));

}
