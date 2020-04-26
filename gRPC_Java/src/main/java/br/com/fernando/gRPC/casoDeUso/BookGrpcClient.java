package br.com.fernando.gRPC.casoDeUso;

import br.com.fernando.gRPC.BookArray;
import br.com.fernando.gRPC.BookRequest;
import br.com.fernando.gRPC.BookResponse;
import br.com.fernando.gRPC.BookServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BookGrpcClient {

    public static void main(String[] args) {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        BookServiceGrpc.BookServiceBlockingStub stub
                = BookServiceGrpc.newBlockingStub(channel);

        BookRequest r = BookRequest.newBuilder()
                .setId(1L)
                .setNome("java")
                .setAutor("Fernando")
                .build();
        final BookResponse salvar = stub.salvar(r);
        System.out.println("SAlvar : " + salvar);

        final BookResponse atualizar = stub.atualizar(r);
        System.out.printf("Atualizar %s \n", atualizar);

        final BookResponse deletar = stub.deletar(r);
        System.out.printf("Deletar %s \n", deletar);

        final BookArray buscar = stub.buscar(r);
        System.out.println("Buscar " + buscar.getLivrosList());
    }
}
