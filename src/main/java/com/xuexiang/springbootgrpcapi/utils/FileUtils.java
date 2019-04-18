package com.xuexiang.springbootgrpcapi.utils;

import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author xuexiang
 * @since 2018/7/18 下午4:30
 */
public final class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 生成随机的文件名
     *
     * @return
     */
    public static String randomFileName(String filePath) {
        String fileExtension = getFileExtension(filePath);
        return RandomGUID.getRandomGUID() + "." + fileExtension;
    }


    /**
     * 获取全路径中的文件拓展名
     *
     * @param filePath 文件路径
     * @return 文件拓展名
     */
    public static String getFileExtension(final String filePath) {
        if (StringUtils.isEmpty(filePath)) return filePath;
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastPoi == -1 || lastSep >= lastPoi) return "";
        return filePath.substring(lastPoi + 1);
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsFile(final File file) {
        if (file == null) return false;
        // 如果存在，是文件则返回 true，是目录则返回 false
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(final File file) {
        // 如果存在，是目录则返回 true，是文件则返回 false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }
}
