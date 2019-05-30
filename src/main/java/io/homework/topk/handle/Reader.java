package io.homework.topk.handle;

import lombok.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * Created by jingdi on 2019/5/29
 */
@Data
public class Reader extends Thread {

    private static final Logger logger = Logger.getLogger("Reader");

    private File file;

    private Processor processor;

    private LinkedBlockingQueue<String> blockingQueue;

    public Reader(File file, LinkedBlockingQueue<String> blockingQueue) {
        this.file = file;
        this.processor = new Processor();
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        read();
    }

    private void read() {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String uri;
            while ((uri = bufferedReader.readLine()) != null) {
                processor.offer(uri);
            }
            while (true) {
                if (processor.getBlockingQueue().size() == 0) {
                    blockingQueue.offer("start builder...");
                    break;
                }
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

    public void startReader() {
        this.start();
        this.processor.start();
    }
}
