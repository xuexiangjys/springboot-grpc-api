package com.xuexiang.springbootgrpcdemo;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 日志拦截
 *
 * @author xuexiang
 * @since 2019/4/16 上午9:14
 */
@Slf4j
@Component
public class LogInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
                                                                 ServerCallHandler<ReqT, RespT> next) {
        System.out.println("日志拦截方法:" + call.getMethodDescriptor().getFullMethodName());
        log.info(call.getMethodDescriptor().getFullMethodName());
        return next.startCall(call, headers);
    }
}
