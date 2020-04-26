package br.com.fernando.gRPC;

import br.com.fernando.gRPC.casoDeUso.BookServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@RequiredArgsConstructor
@SpringBootApplication
public class GrpcServer implements CommandLineRunner {

    private final HelloServiceImpl helloService;
    private final BookServiceImpl bookService;

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(GrpcServer.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Server server = ServerBuilder.forPort(8080)
                .addService(helloService)
                .addService(bookService)
                .build();

        System.out.println("Starting gRPC server...");
        server.start();
        System.out.println("gRPC server started!");
        server.awaitTermination();
    }
}