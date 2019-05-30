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

    private void handle(String uri) {
        if (count.containsKey(uri)) {
            Url url = count.get(uri);
            url.increase();
        } else {
            Url url = new Url(uri, 1L);
            count.put(uri, url);
        }
//        logger.info("url count : " + JSON.toJSONString(count.get(uri)));
    }

    private void buildUrlPool(HashMap<String, Url> urls) {
        urls.forEach((uri, url) -> {
//            logger.info("url : " + JSON.toJSONString(url));
            urlPool.add(url);
        });
        try {
//            logger.info("sub pool : " + JSON.toJSONString(urlPool.getQueue()));
            poolQueue.put(urlPool);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
