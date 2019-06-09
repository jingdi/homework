package io.homework.topk;

import com.alibaba.fastjson.JSON;
import io.homework.topk.handle.Builder;
import io.homework.topk.handle.Reader;
import io.homework.topk.model.Constants;
import io.homework.topk.model.UrlPool;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * Created by jingdi on 2019/5/29
 */
public class ServiceStart {

    private static final Logger logger = Logger.getLogger("Service");

    /**
     * 充当一个线程同步的信道，当工作线程完成小文件构建后向该队列发送通知消息
     */
    private static LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        /**
         *根据uri hash值将大文件拆分为200个小文件
         */
        File file = new File(Constants.RESOURCE_FILE);
        Reader reader = new Reader(file, messageQueue);
        try {
            reader.startReader();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * 阻塞等待小文件构建完成
         */
        try {
            messageQueue.take();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         *计算每个小文件的top k，最后汇总
         */
        Long count = 0L;
        UrlPool niubilityPool = new UrlPool();
        LinkedBlockingQueue<UrlPool> poolQueue = new LinkedBlockingQueue<>();
        HashSet<String> paths = reader.getProcessor().getWriter().getFilePaths();
        try {
            /**
             * 将小文件计算放到线程池，计算结果放到共享的poolQueue
             */
            Builder builder = new Builder(paths, poolQueue);
            builder.start();

            /**
             * 计算结果汇总，得到全局top k
             */
            while (true) {
                UrlPool pool = poolQueue.take();
                pool.getQueue().forEach(niubilityPool::add);
                count++;
                if (count == paths.size() && poolQueue.size() == 0) {
                    FileWriter writer = new FileWriter(Constants.ROOT_PATH + "result.txt");
                    writer.flush();
                    while (!niubilityPool.getQueue().isEmpty()) {
                        writer.write(JSON.toJSONString(niubilityPool.getQueue().poll()));
                        writer.write("\n");
                    }
                    writer.close();
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
