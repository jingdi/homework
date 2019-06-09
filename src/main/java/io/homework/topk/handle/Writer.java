package io.homework.topk.handle;

import io.homework.topk.model.Constants;
import lombok.Data;

import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * Created by jingdi on 2019/5/29
 */
@Data
public class Writer {
    private static final Logger logger = Logger.getLogger("Writer");

    private HashSet<String> filePaths = new HashSet<>();

    /**
     * 创建小文件，并将数据写入相应的小文件
     * 文件名根据数据hash值定义，保证相同数据写入同一文件
     * @param uri
     */
    public void write(String uri) {
        FileWriter fileWriter = null;
        String path = Constants.ROOT_PATH + Constants.FILE_HEADER + Math.abs(uri.hashCode() % Constants.FILE_NUM) + ".txt";
        filePaths.add(path);
        File file = new File(path);
        try {
            if (!file.exists()) {
                boolean rst = file.createNewFile();
                if (!rst) {
                    throw new Exception();
                }
            }
            fileWriter = new FileWriter(file, true);
            fileWriter.write(uri);
            fileWriter.write("\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            file.deleteOnExit();
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
