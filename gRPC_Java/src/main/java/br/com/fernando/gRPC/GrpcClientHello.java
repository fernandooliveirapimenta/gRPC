package br.com.fernando.gRPC;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClientHello {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8080)
            .usePlaintext()
            .build();

        HelloServiceGrpc.HelloServiceBlockingStub stub 
          = HelloServiceGrpc.newBlockingStub(channel);

        for (int i = 0; i<10; i++){
            HelloResponse helloResponse = stub.hello(HelloRequest.newBuilder()
                    .setFirstName("Fernando")
                    .setLastName("Pimenta")
                    .build());

            System.out.println("Response received from server:\n" + helloResponse);

        }
        channel.shutdown();
    }
}