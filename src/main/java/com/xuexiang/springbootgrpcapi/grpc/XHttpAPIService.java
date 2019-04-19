package com.xuexiang.springbootgrpcapi.grpc;

import com.alibaba.druid.util.StringUtils;
import com.google.protobuf.ByteString;
import com.xuexiang.springbootgrpc.APIServiceGrpc;
import com.xuexiang.springbootgrpc.XHttpApi;
import com.xuexiang.springbootgrpcapi.LogInterceptor;
import com.xuexiang.springbootgrpcapi.exception.ApiException;
import com.xuexiang.springbootgrpcapi.model.User;
import com.xuexiang.springbootgrpcapi.service.FileStorageService;
import com.xuexiang.springbootgrpcapi.service.UserService;
import com.xuexiang.springbootgrpcapi.utils.APIUtils;
import com.xuexiang.springbootgrpcapi.utils.FileIOUtils;
import com.xuexiang.springbootgrpcapi.utils.TokenUtils;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.xuexiang.springbootgrpcapi.exception.ApiException.ERROR.COMMON_BUSINESS_ERROR;
import static com.xuexiang.springbootgrpcapi.exception.ApiException.ERROR.FILE_STORE_ERROR;

/**
 * @author xuexiang
 * @since 2019/4/16 下午11:34
 */
@Slf4j
@GRpcService(interceptors = {LogInterceptor.class})
public class XHttpAPIService extends APIServiceGrpc.APIServiceImplBase {

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileService;

    @Override
    public void login(XHttpApi.LoginRequest request, StreamObserver<XHttpApi.LoginReply> responseObserver) {
        if (userService.findUserByAccount(request.getLoginName()) == null) {
            responseObserver.onError(ApiException.error("账号不存在！", COMMON_BUSINESS_ERROR));
        }

        User user = userService.login(request.getLoginName(), request.getPassword());

        if (user != null) {
            XHttpApi.LoginReply loginReply = XHttpApi.LoginReply.newBuilder()
                    .setUser(APIUtils.toApi(user))
                    .setToken(TokenUtils.createJwtToken(user.getLoginName()))
                    .build();
            responseObserver.onNext(loginReply);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(ApiException.error("用户名或密码错误！", COMMON_BUSINESS_ERROR));
        }
    }

    @Override
    public void getAllUser(XHttpApi.Empty request, StreamObserver<XHttpApi.UserList> responseObserver) {
        List<User> users = userService.findAllUser();
        XHttpApi.UserList.Builder builder = XHttpApi.UserList.newBuilder();
        for (User user : users) {
            builder.addUsers(APIUtils.toApi(user));
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void uploadFile(XHttpApi.FilePackage request, StreamObserver<XHttpApi.FilePathPackage> responseObserver) {
        XHttpApi.FilePathPackage.Builder replyBuilder = XHttpApi.FilePathPackage.newBuilder();
        for (XHttpApi.FileInfo fileInfo : request.getFileList()) {
            String fileName = null;
            try {
                fileName = fileService.storeFile(fileInfo.getFileName(), fileInfo.getSize(), fileInfo.getData().toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
                responseObserver.onError(ApiException.error("文件存储失败！", FILE_STORE_ERROR));
                return;
            }
            if (!StringUtils.isEmpty(fileName)) {
                replyBuilder.addPath(fileName);
            }
        }
        responseObserver.onNext(replyBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<XHttpApi.FileInfo> uploadFileStream(StreamObserver<XHttpApi.FilePath> responseObserver) {
        return new StreamObserver<XHttpApi.FileInfo>() {
            @Override
            public void onNext(XHttpApi.FileInfo value) {
                String fileName = null;
                try {
                    fileName = fileService.storeFile(value.getFileName(), value.getSize(), value.getData().toByteArray());
                } catch (Exception e) {
                    e.printStackTrace();
                    responseObserver.onError(ApiException.error("文件存储失败！", FILE_STORE_ERROR));
                }
                if (fileName != null) {
                    responseObserver.onNext(XHttpApi.FilePath.newBuilder().setPath(fileName).build());
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void download(XHttpApi.FilePathPackage request, StreamObserver<XHttpApi.FilePackage> responseObserver) {
        XHttpApi.FilePackage.Builder replyBuilder = XHttpApi.FilePackage.newBuilder();
        for (int i = 0; i < request.getPathCount(); i++) {
            String path = request.getPath(i);
            Resource resource = fileService.loadFileAsResource(path);
            try {
                XHttpApi.FileInfo fileInfo = XHttpApi.FileInfo.newBuilder()
                        .setFileName(resource.getFilename())
                        .setData(ByteString.copyFrom(FileIOUtils.readFile2BytesByChannel(resource.getFile())))
                        .setSize(resource.contentLength())
                        .build();
                replyBuilder.addFile(fileInfo);
            } catch (IOException e) {
                e.printStackTrace();
                responseObserver.onError(ApiException.error("文件获取失败！", FILE_STORE_ERROR));
                return;
            }
        }
        responseObserver.onNext(replyBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<XHttpApi.FilePath> downloadStream(StreamObserver<XHttpApi.FileInfo> responseObserver) {
        return new StreamObserver<XHttpApi.FilePath>() {
            @Override
            public void onNext(XHttpApi.FilePath value) {
                Resource resource = fileService.loadFileAsResource(value.getPath());
                try {
                    XHttpApi.FileInfo fileInfo = XHttpApi.FileInfo.newBuilder()
                            .setFileName(resource.getFilename())
                            .setData(ByteString.copyFrom(FileIOUtils.readFile2BytesByChannel(resource.getFile())))
                            .setSize(resource.contentLength())
                            .build();
                    responseObserver.onNext(fileInfo);
                } catch (IOException e) {
                    e.printStackTrace();
                    responseObserver.onError(ApiException.error("文件获取失败！", FILE_STORE_ERROR));
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
