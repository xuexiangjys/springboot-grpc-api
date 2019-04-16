package com.xuexiang.springbootgrpcapi;
import com.xuexiang.springbootgrpcapi.helloworld.GreeterGrpc;
import com.xuexiang.springbootgrpcapi.helloworld.HelloReply;
import com.xuexiang.springbootgrpcapi.helloworld.HelloRequest;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService(interceptors = { LogInterceptor.class })
public class GreeterService extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        String message = "Hello " + request.getName();
        final HelloReply.Builder replyBuilder = HelloReply.newBuilder().setMessage(message);
        responseObserver.onNext(replyBuilder.build());
        responseObserver.onCompleted();
        log.info("Returning " +message);
    }
}