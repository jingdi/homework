package io.homework.topk.handle;

import lombok.Data;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * Created by jingdi on 2019/5/29
 */
@Data
public class Processor extends Thread {

    private static final Logger logger = Logger.getLogger("Processor");

    private LinkedBlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(1000);

    private Writer writer = new Writer();

    /**
     * 消费队列消息
     */
    private void process() {
        String uri;
        try {
            while ((uri = blockingQueue.take()) != null) {
                writer.write(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        process();
    }

    public void put(String uri) {
        try {
            blockingQueue.put(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}