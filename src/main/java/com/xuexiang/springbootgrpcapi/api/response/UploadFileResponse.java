package com.xuexiang.springbootgrpcapi.api.response;


import lombok.Getter;
import lombok.Setter;

/**
 * 文件上传返回结果
 *
 * @author xuexiang
 * @since 2018/7/18 下午3:55
 */
@Getter
@Setter
public class UploadFileResponse {

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public UploadFileResponse(String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }

}
