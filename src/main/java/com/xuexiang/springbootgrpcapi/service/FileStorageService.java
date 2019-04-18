package com.xuexiang.springbootgrpcapi.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储服务
 *
 * @author xuexiang
 * @since 2018/7/18 下午3:36
 */
public interface FileStorageService {

    /**
     * 存储文件
     *
     * @param fileName 文件名
     * @param fileSize 文件大小
     * @param data 数据
     * @return
     * @throws Exception
     */
    String storeFile(String fileName, long fileSize, byte[] data) throws Exception;


    /**
     * 读取文件
     *
     * @param fileName
     * @return
     */
    Resource loadFileAsResource(String fileName);
}
