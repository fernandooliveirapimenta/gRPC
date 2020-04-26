package br.com.fernando.gRPC.casoDeUso;

import br.com.fernando.gRPC.BookArray;
import br.com.fernando.gRPC.BookRequest;
import br.com.fernando.gRPC.BookResponse;
import br.com.fernando.gRPC.BookServiceGrpc;
import br.com.fernando.gRPC.domain.Book;
import br.com.fernando.gRPC.domain.BookRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Slf4j
@Service
public class BookServiceImpl extends BookServiceGrpc.BookServiceImplBase {

    private final BookRepository bookRepository;

    @Override
    public void salvar(BookRequest request,
               StreamObserver<BookResponse> responseObserver) {

        log.info("Salvando book {}",request);

        Book b = new Book();
        b.setNome(request.getNome());
        b.setAutor(request.getAutor());
        bookRepository.save(b);
        BookResponse response = bookToResponse(b);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private BookResponse bookToResponse(Book b) {
        return BookResponse.newBuilder()
                    .setId(b.getId())
                    .setNome(b.getNome())
                    .setAutor(b.getAutor() == null ? "": b.getAutor())
                    .build();
    }

    @Override
    public void atualizar(BookRequest request,
                          StreamObserver<BookResponse> responseObserver) {

        log.info("Atualizando book {}",request);
        if(request.getId() == 0L)
            throw new RuntimeException("Id Obrigatorio");

        final Optional<Book> byId = bookRepository.findById(request.getId());

        if(byId.isPresent()) {

            final Book b = byId.orElseThrow(RuntimeException::new);

            b.setAutor(request.getAutor());
            b.setNome(request.getNome());
            bookRepository.save(b);
            responseObserver.onNext(bookToResponse(b));
        } else {
            responseObserver.onNext(BookResponse.newBuilder().build());

        }

        responseObserver.onCompleted();
    }

    @Override
    public void deletar(BookRequest request,
                        StreamObserver<BookResponse> responseObserver) {

        log.info("Deletando book {}",request);
        final Optional<Book> byId = bookRepository.findById(request.getId());

        if(byId.isPresent()) {

            final Book b = byId.orElseThrow(RuntimeException::new);
            bookRepository.delete(b);
            responseObserver.onNext(bookToResponse(b));
        }else {
            responseObserver.onNext(BookResponse.newBuilder().build());
        }

        responseObserver.onCompleted();
    }

    @Override
    public void buscar(BookRequest request,
                       StreamObserver<BookArray> responseObserver) {


        log.info("Buscando book {}",request);
        final Iterator<Book> iterator = bookRepository.findAll().iterator();
        List<BookResponse> bs = new ArrayList<>();
        iterator.forEachRemaining(i -> bs.add(bookToResponse(i)));

        BookArray r = BookArray.newBuilder()
                .addAllLivros(bs)
                .build();

        responseObserver.onNext(r);
        responseObserver.onCompleted();

    }
}
