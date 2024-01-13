package cc.maids.libms.Book;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void BookRepository_FindAll_ReturnAllBooks() {
        Book book1 = Book.builder()
                .author("author1")
                .title("title1")
                .isbn("1234567")
                .publicationYear("1996")
                .build();

        Book book2 = Book.builder()
                .author("author1")
                .title("title1")
                .isbn("1234567")
                .publicationYear("1996")
                .build();

        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> bookList = bookRepository.findAll();

        Assertions.assertThat(!bookList.isEmpty());
        Assertions.assertThat(bookList).isEqualTo(List.of(book1,book2));
    }

    @Test
    public void BookRepository_Save_ReturnSavedBook() {
        Book book = Book.builder()
                .author("author1")
                .title("title1")
                .isbn("1234567")
                .publicationYear("1996")
                .build();

        Book savedBook = bookRepository.save(book);

        Assertions.assertThat(savedBook).isNotNull();
        Assertions.assertThat(savedBook.getId()).isGreaterThan(0);
    }

    @Test
    public void BookRepository_FindById_ReturnBook() {
        Book book = Book.builder()
                .author("author1")
                .title("title1")
                .isbn("1234567")
                .publicationYear("1996")
                .build();
        bookRepository.save(book);

        Book foundBook = bookRepository.findById(book.getId()).get();

        Assertions.assertThat(foundBook).isNotNull();
        Assertions.assertThat(foundBook.getIsbn()).isEqualTo(book.getIsbn());
        Assertions.assertThat(foundBook.getAuthor()).isEqualTo(book.getAuthor());
    }

    // need to test locking
    @Test
    public void BookRepository_FindByIdPessimistic_ReturnBook() {
        Book book = Book.builder()
                .author("author1")
                .title("title1")
                .isbn("1234567")
                .publicationYear("1996")
                .build();
        bookRepository.save(book);

        Book foundBook = bookRepository.findByIdPessimistic(book.getId()).get();

        Assertions.assertThat(foundBook).isNotNull();
        Assertions.assertThat(foundBook.getIsbn()).isEqualTo(book.getIsbn());
        Assertions.assertThat(foundBook.getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    public void BookRepository_DeleteById_ReturnIsEmptyBook() {
        Book book = Book.builder()
                .author("author1")
                .title("title1")
                .isbn("1234567")
                .publicationYear("1996")
                .build();
        bookRepository.save(book);

        bookRepository.deleteById(book.getId());

        Optional<Book> bookOp = bookRepository.findById(book.getId());

        Assertions.assertThat(bookOp).isEmpty();

    }

}
