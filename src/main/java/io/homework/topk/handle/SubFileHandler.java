package io.homework.topk.handle;

import io.homework.topk.model.Url;
import io.homework.topk.model.UrlPool;
import lombok.Data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * Created by jingdi on 2019/5/30
 */
@Data
public class SubFileHandler implements Runnable {

    private static final Logger logger = Logger.getLogger("SubFileHandler");

    private String path;

    private LinkedBlockingQueue<UrlPool> poolQueue;

    /**
     * 这里用Url对象作为value是考虑方便后续构建最小堆
     */
    private HashMap<String, Url> count = new HashMap<>();

    private UrlPool urlPool = new UrlPool();

    public SubFileHandler(String path, LinkedBlockingQueue<UrlPool> poolQueue) {
        this.path = path;
        this.poolQueue = poolQueue;
    }

    @Override
    public void run() {
        read(path);
        buildUrlPool(count);
    }

    /**
     * 读取文件数据，构建 数据:出现次数 的映射
     *
     * 可能出现的极端情况：
     * 1. 某个数据出现几十亿次，影响不大，记录次数的是long型结构（重写的比较方法之前会有越界隐患，现已优化）
     * 2. 每个数据只出现几次，这样理论上会导致我们每个小文件维护的映射关系会比较大，可能会OOM，这个情况暂时没想到很好的办法，只能尽量增加小文件的数量以及将该过程改为单线程处理，同一时刻只维护一个小文件的映射关系
     * @param file
     */
    private void read(String file) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String uri;
            while ((uri = bufferedReader.readLine()) != null) {
                handle(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将数据存入Map
     * @param uri
     */
    private void handle(String uri) {
        if (count.containsKey(uri)) {
            Url url = count.get(uri);
            url.increase();
        } else {
            Url url = new Url(uri, 1L);
            count.put(uri, url);
        }
    }

    /**
     * 构建小文件的最小堆，并将该最小堆放入全局的堆队列中
     * @param urls
     */
    private void buildUrlPool(HashMap<String, Url> urls) {
        urls.forEach((uri, url) -> {
            urlPool.add(url);
        });
        try {
            poolQueue.put(urlPool);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            count.clear();
        }
    }
}
