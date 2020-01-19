package sll.plugin.helper.generator.mybatis.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * Created by LSL on 2020/1/16 12:06
 */
abstract class GeneratorHelper {

    /**
     * 创建文件并写入相关内容
     *
     * @param filePath 文件
     * @param content  内容
     */
    void createFile(String filePath, String content) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        boolean ok = true;
        try {
            // 获取文件生成名称和位置
            File file = new File(filePath);
            File parentFile = file.getParentFile();
            // 判断文件是否存在
            boolean fileExists = file.exists();
            // 判断文件目录是否存在，不存在则创建新目录
            if (!parentFile.exists()) {
                ok = parentFile.mkdirs();
            }
            if (ok) {
                // 创建文件输出流
                fos = new FileOutputStream(file);
                osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                // 写入内容
                osw.write(content);
                osw.flush();
                if (fileExists) {
                    System.out.println("[WARN] " + filePath + " [Overwrite existing files]");
                } else {
                    System.out.println("[INFO] " + filePath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取指定换行
     *
     * @param n 换行数
     * @return 换行符
     */
    String getLine(Integer n) {
        StringBuilder result = new StringBuilder();
        result.append("\r");
        for (Integer i = 0; i < n; i++) {
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * 获取指定退格数
     *
     * @param n 数
     * @return 字符串
     */
    String getTab(Integer n) {
        StringBuilder result = new StringBuilder();
        for (Integer i = 0; i < n; i++) {
            result.append("\t");
        }
        return result.toString();
    }

    String get1Line2Tab() {
        return "\r\n\t\t";
    }

    String getLineTab() {
        return "\r\n\t";
    }
}
