package io.homework.topk.handle;

import io.homework.topk.model.Constants;
import io.homework.topk.model.UrlPool;
import lombok.Data;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * Created by jingdi on 2019/5/30
 */
@Data
public class Builder extends Thread {

    private static final Logger logger = Logger.getLogger("Builder");

    private HashSet<String> paths;

    private LinkedBlockingQueue<UrlPool> poolQueue;

    private UrlPool urlPool = new UrlPool();

    public Builder(HashSet<String> paths, LinkedBlockingQueue<UrlPool> poolQueue) {
        this.paths = paths;
        this.poolQueue = poolQueue;
    }

    /**
     * 并发处理小文件数据，为每个小文件创建最小堆
     */
    private void handle() {
        paths.forEach(path -> {
            SubFileHandler fileHandler = new SubFileHandler(path, poolQueue);
            if (Constants.CONCURRENT_HANDLE) {
                ThreadFactory.executor.execute(fileHandler);
            } else {
                fileHandler.run();
            }
        });
    }

    @Override
    public void run() {
        handle();
    }
}
