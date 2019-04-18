package com.xuexiang.springbootgrpcapi.grpc;

import com.alibaba.druid.util.StringUtils;
import com.xuexiang.springbootgrpc.APIServiceGrpc;
import com.xuexiang.springbootgrpc.XHttpApi;
import com.xuexiang.springbootgrpcapi.LogInterceptor;
import com.xuexiang.springbootgrpcapi.exception.ApiException;
import com.xuexiang.springbootgrpcapi.model.User;
import com.xuexiang.springbootgrpcapi.service.FileStorageService;
import com.xuexiang.springbootgrpcapi.service.UserService;
import com.xuexiang.springbootgrpcapi.utils.TokenUtils;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

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
                    .setUser(toApi(user))
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
            builder.addUsers(toApi(user));
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void uploadFile(XHttpApi.FilePackage request, StreamObserver<XHttpApi.UploadFileReply> responseObserver) {
        XHttpApi.UploadFileReply.Builder replyBuilder = XHttpApi.UploadFileReply.newBuilder();
        for (XHttpApi.FileInfo fileInfo : request.getFileList()) {
            String fileName = null;
            try {
                fileName = fileService.storeFile(fileInfo.getFileName(), fileInfo.getSize(), fileInfo.getData().toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
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

    private XHttpApi.User toApi(User user) {
        return XHttpApi.User.newBuilder()
                .setUserId(user.getUserId())
                .setLoginName(user.getLoginName())
                .setPassword(user.getName())
                .setName(user.getName())
                .setAge(user.getAge())
                .setGender(user.getGender())
                .setPhone(user.getPhone())
                .build();
    }


}
