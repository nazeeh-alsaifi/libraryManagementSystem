package cc.maids.libms.Book;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookService bookService;

    @Test
    public void BookService_GetAllBooks_ReturnListOfAllBooks() {
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

        List<Book> bookList = List.of(book1, book2);

        when(bookRepository.findAll()).thenReturn(bookList);

        List<Book> allBooks = bookService.getAllBooks();
        Assertions.assertThat(allBooks).isNotNull();
    }

    @Test
    public void BookService_GetBookById_ReturnBook() {
        Long bookId = 1l;
        Book book = Book.builder()
                .id(bookId)
                .author("author1")
                .title("title1")
                .isbn("1234567")
                .publicationYear("1996")
                .build();
        Optional<Book> bookOp = Optional.of(book);
        when(bookRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(book));

        Optional<Book> foundBook = bookService.getBookById(bookId);
        Assertions.assertThat(foundBook).isEqualTo(bookOp);
    }

    @Test
    public void BookService_SaveBook_ReturnSavedBook() {
        Book book = Book.builder()
                .author("author1")
                .title("title1")
                .isbn("1234567")
                .publicationYear("1996")
                .build();

        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);

        Book savedBook = bookService.saveBook(book);

        Assertions.assertThat(savedBook).isNotNull();
    }

    @Test
    public void BookService_DeleteBook_ReturnVoid() {
        Long bookId = 1l;

        doNothing().when(bookRepository).deleteById(bookId);

        assertAll(() -> bookService.deleteBook(bookId));

    }
}
