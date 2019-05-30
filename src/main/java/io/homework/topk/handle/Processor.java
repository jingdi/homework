package io.homework.topk.handle;

import io.homework.topk.model.Constants;
import lombok.Data;

import java.io.File;
import java.io.FileWriter;
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

    public void offer(String uri) {
        try {
            blockingQueue.put(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}