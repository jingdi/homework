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

    private LinkedBlockingQueue<String> messageQueue;

    public Reader(File file, LinkedBlockingQueue<String> messageQueue) {
        this.file = file;
        this.processor = new Processor();
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        read();
    }

    /**
     * 1. 从大文件中读取数据，放入processor的阻塞队列供写线程消费，队列容量为1000；当写文件的线程处理较慢时，读线程会阻塞
     * 2. 当第一阶段读写完成后，向主线程共享的阻塞队列发送通知消息，开始下一阶段的处理
     */
    private void read() {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String uri;
            while ((uri = bufferedReader.readLine()) != null) {
                processor.put(uri);
            }
            while (!processor.getBlockingQueue().isEmpty()) {
                logger.info("waiting for writing sub files...");
            }
            messageQueue.offer("start builder...");
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
