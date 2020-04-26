package br.com.fernando.gRPC;

import br.com.fernando.gRPC.domain.Book;
import br.com.fernando.gRPC.domain.BookRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    private final BookRepository bookRepository;

    @Override
    public void hello(
      HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        System.out.println("Request received from client:\n" + request);


        Book b = new Book();
        b.setNome(request.getFirstName() + request.getLastName());
        bookRepository.save(b);

        String greeting = new StringBuilder().append("Hello, ")
            .append(request.getFirstName())
            .append(" ")
            .append(request.getLastName())
            .toString();

        HelloResponse response = HelloResponse.newBuilder()
            .setGreeting(greeting)
            .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}