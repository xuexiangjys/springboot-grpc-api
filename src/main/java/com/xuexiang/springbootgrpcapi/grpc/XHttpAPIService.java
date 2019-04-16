package com.xuexiang.springbootgrpcapi.grpc;

import com.xuexiang.springbootgrpc.APIServiceGrpc;
import com.xuexiang.springbootgrpc.XHttpApi;
import com.xuexiang.springbootgrpcapi.LogInterceptor;
import com.xuexiang.springbootgrpcapi.exception.ApiException;
import com.xuexiang.springbootgrpcapi.model.User;
import com.xuexiang.springbootgrpcapi.service.UserService;
import com.xuexiang.springbootgrpcapi.utils.TokenUtils;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import static com.xuexiang.springbootgrpcapi.exception.ApiException.ERROR.COMMON_BUSINESS_ERROR;

/**
 *
 *
 * @author xuexiang
 * @since 2019/4/16 下午11:34
 */
@Slf4j
@GRpcService(interceptors = { LogInterceptor.class })
public class XHttpAPIService extends APIServiceGrpc.APIServiceImplBase {

    @Autowired
    private UserService userService;

    @Override
    public void login(XHttpApi.LoginRequest request, StreamObserver<XHttpApi.LoginReply> responseObserver){
        if (userService.findUserByAccount(request.getLoginName()) == null) {
            responseObserver.onError(new ApiException("账号不存在！", COMMON_BUSINESS_ERROR));
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
            responseObserver.onError(new ApiException("用户名或密码错误！", COMMON_BUSINESS_ERROR));
        }
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
