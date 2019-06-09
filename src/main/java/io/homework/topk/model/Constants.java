package io.homework.topk.model;

/**
 * Created by jingdi on 2019/5/30
 */
public class Constants {

    public static final String ROOT_PATH = "/Users/jingdi/work/topK/homework/src/main/resources/";

    public static final String FILE_HEADER = "LFC-Top-One";

    public static final String RESOURCE_FILE = "/Users/jingdi/work/topK/homework/src/main/resources/urls.txt";

    public static final int TOP_SIZE = 100;

    /**
     * 分割的小文件数量，可以根据需要调大，降低OOM的风险
     */
    public static final int FILE_NUM = 200;

    /**
     * 是否并发构建小文件的最小堆（为了尽量节省内存，默认为false）
     */
    public static final boolean CONCURRENT_HANDLE = false;
}
