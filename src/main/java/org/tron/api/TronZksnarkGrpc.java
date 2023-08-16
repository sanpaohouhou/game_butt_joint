package org.tron.api;

import io.grpc.stub.ClientCalls;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.9.0)",
    comments = "Source: api/zksnark.proto")
public final class TronZksnarkGrpc {

  private TronZksnarkGrpc() {}

  public static final String SERVICE_NAME = "protocol.TronZksnark";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @Deprecated // Use {@link #getCheckZksnarkProofMethod()} instead.
  public static final io.grpc.MethodDescriptor<org.tron.api.ZksnarkGrpcAPI.ZksnarkRequest,
      org.tron.api.ZksnarkGrpcAPI.ZksnarkResponse> METHOD_CHECK_ZKSNARK_PROOF = getCheckZksnarkProofMethod();

  private static volatile io.grpc.MethodDescriptor<org.tron.api.ZksnarkGrpcAPI.ZksnarkRequest,
      org.tron.api.ZksnarkGrpcAPI.ZksnarkResponse> getCheckZksnarkProofMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<org.tron.api.ZksnarkGrpcAPI.ZksnarkRequest,
      org.tron.api.ZksnarkGrpcAPI.ZksnarkResponse> getCheckZksnarkProofMethod() {
    io.grpc.MethodDescriptor<org.tron.api.ZksnarkGrpcAPI.ZksnarkRequest, org.tron.api.ZksnarkGrpcAPI.ZksnarkResponse> getCheckZksnarkProofMethod;
    if ((getCheckZksnarkProofMethod = TronZksnarkGrpc.getCheckZksnarkProofMethod) == null) {
      synchronized (TronZksnarkGrpc.class) {
        if ((getCheckZksnarkProofMethod = TronZksnarkGrpc.getCheckZksnarkProofMethod) == null) {
          TronZksnarkGrpc.getCheckZksnarkProofMethod = getCheckZksnarkProofMethod =
              io.grpc.MethodDescriptor.<org.tron.api.ZksnarkGrpcAPI.ZksnarkRequest, org.tron.api.ZksnarkGrpcAPI.ZksnarkResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protocol.TronZksnark", "CheckZksnarkProof"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tron.api.ZksnarkGrpcAPI.ZksnarkRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.tron.api.ZksnarkGrpcAPI.ZksnarkResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TronZksnarkMethodDescriptorSupplier("CheckZksnarkProof"))
                  .build();
          }
        }
     }
     return getCheckZksnarkProofMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TronZksnarkStub newStub(io.grpc.Channel channel) {
    return new TronZksnarkStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TronZksnarkBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TronZksnarkBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TronZksnarkFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TronZksnarkFutureStub(channel);
  }

  /**
   */
  public static abstract class TronZksnarkImplBase implements io.grpc.BindableService {

    /**
     */
    public void checkZksnarkProof(org.tron.api.ZksnarkGrpcAPI.ZksnarkRequest request,
        io.grpc.stub.StreamObserver<org.tron.api.ZksnarkGrpcAPI.ZksnarkResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCheckZksnarkProofMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCheckZksnarkProofMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.tron.api.ZksnarkGrpcAPI.ZksnarkRequest,
                org.tron.api.ZksnarkGrpcAPI.ZksnarkResponse>(
                  this, METHODID_CHECK_ZKSNARK_PROOF)))
          .build();
    }
  }

  /**
   */
  public static final class TronZksnarkStub extends io.grpc.stub.AbstractStub<TronZksnarkStub> {
    private TronZksnarkStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TronZksnarkStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected TronZksnarkStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TronZksnarkStub(channel, callOptions);
    }

    /**
     */
    public void checkZksnarkProof(org.tron.api.ZksnarkGrpcAPI.ZksnarkRequest request,
        io.grpc.stub.StreamObserver<org.tron.api.ZksnarkGrpcAPI.ZksnarkResponse> responseObserver) {
      ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCheckZksnarkProofMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TronZksnarkBlockingStub extends io.grpc.stub.AbstractStub<TronZksnarkBlockingStub> {
    private TronZksnarkBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TronZksnarkBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected TronZksnarkBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TronZksnarkBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.tron.api.ZksnarkGrpcAPI.ZksnarkResponse checkZksnarkProof(org.tron.api.ZksnarkGrpcAPI.ZksnarkRequest request) {
      return blockingUnaryCall(
          getChannel(), getCheckZksnarkProofMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TronZksnarkFutureStub extends io.grpc.stub.AbstractStub<TronZksnarkFutureStub> {
    private TronZksnarkFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TronZksnarkFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected TronZksnarkFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TronZksnarkFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.tron.api.ZksnarkGrpcAPI.ZksnarkResponse> checkZksnarkProof(
        org.tron.api.ZksnarkGrpcAPI.ZksnarkRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCheckZksnarkProofMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CHECK_ZKSNARK_PROOF = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TronZksnarkImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TronZksnarkImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CHECK_ZKSNARK_PROOF:
          serviceImpl.checkZksnarkProof((org.tron.api.ZksnarkGrpcAPI.ZksnarkRequest) request,
              (io.grpc.stub.StreamObserver<org.tron.api.ZksnarkGrpcAPI.ZksnarkResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class TronZksnarkBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TronZksnarkBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.tron.api.ZksnarkGrpcAPI.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TronZksnark");
    }
  }

  private static final class TronZksnarkFileDescriptorSupplier
      extends TronZksnarkBaseDescriptorSupplier {
    TronZksnarkFileDescriptorSupplier() {}
  }

  private static final class TronZksnarkMethodDescriptorSupplier
      extends TronZksnarkBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TronZksnarkMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (TronZksnarkGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TronZksnarkFileDescriptorSupplier())
              .addMethod(getCheckZksnarkProofMethod())
              .build();
        }
      }
    }
    return result;
  }
}
