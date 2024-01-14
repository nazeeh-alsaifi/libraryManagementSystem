package cc.maids.libms.Book;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cc.maids.libms.LibmsApplication;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Value("${apiPrefix}")
    private String apiPrefix;

    @Autowired
    ObjectMapper om;

    @Test
    @WithMockUser
    public void BookController_GetAllBooks_ReturnAllBooksAndOk() throws Exception {
        Book book1 = Book.builder()
                .author("author1")
                .title("title1")
                .isbn("1234567")
                .publicationYear("1996")
                .build();

        Book book2 = Book.builder()
                .author("author2")
                .title("title2")
                .isbn("1234567")
                .publicationYear("1996")
                .build();

        List<Book> bookList = List.of(book1, book2);

        when(bookService.getAllBooks()).thenReturn(bookList);

        ResultActions response = mockMvc.perform(
                get(apiPrefix + "/books")
                        .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk()).andExpect(jsonPath("$.length()", CoreMatchers.is(bookList.size())))
                .andExpect(jsonPath("$[*].author", Matchers.containsInAnyOrder("author1", "author2")));
    }

    @Test
    @WithMockUser
    public void BookController_GetAllBooks_ReturnNoContent() throws Exception {
        List<Book> bookList = new ArrayList<>();

        when(bookService.getAllBooks()).thenReturn(bookList);

        ResultActions response = mockMvc.perform(
                get(apiPrefix + "/books")
                        .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNoContent()).andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser
    public void BookController_getBookById_ReturnBookAndOk() throws Exception {
        int bookId = 1;
        Book book = Book.builder()
                .author("author1")
                .title("title1")
                .isbn("1234567")
                .publicationYear("1996")
                .build();
        Optional<Book> bookOp = Optional.ofNullable(book);

        when(bookService.getBookById(bookId)).thenReturn(bookOp);

        ResultActions response = mockMvc.perform(
                get(apiPrefix + "/books/1")
                        .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.author", CoreMatchers.is(book.getAuthor())))
                .andExpect(jsonPath("$.isbn", CoreMatchers.is(book.getIsbn())));
    }

    @Test
    @WithMockUser
    public void BookController_getBookByIdNotFound_ReturnNotNotFound() throws Exception {
        int bookId = 1;
        Book book = null;
        Optional<Book> bookOp = Optional.ofNullable(book);

        when(bookService.getBookById(bookId)).thenReturn(bookOp);

        ResultActions response = mockMvc.perform(
                get(apiPrefix + "/books/1")
                        .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNotFound()).andExpect(jsonPath("$").doesNotExist());

    }

    @Test
    @WithMockUser
    public void BookController_AddBook_ReturnCreatedBookInBody() throws JsonProcessingException, Exception {
        Book book = Book.builder()
                .author("author1")
                .title("title1")
                .isbn("1234567")
                .publicationYear("1996")
                .build();
        when(bookService.saveBook(ArgumentMatchers.any())).thenAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post(apiPrefix + "/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(book)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.author", CoreMatchers.is(book.getAuthor())))
                .andExpect(jsonPath("$.isbn", CoreMatchers.is(book.getIsbn())));
    }

    @Test
    @WithMockUser
    public void BookController_UpdateBook_ReturnUpdatedBook() throws Exception {
        int bookId = 1;
        Book book = Book.builder().id(1L)
                .author("author1")
                .title("title1")
                .isbn("1234567")
                .publicationYear("1996")
                .build();
        when(bookService.saveBook(ArgumentMatchers.any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookService.getBookById(bookId)).thenReturn(Optional.of(book));

        ResultActions response = mockMvc.perform(put(apiPrefix + "/books/" + bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(book)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.author", CoreMatchers.is(book.getAuthor())))
                .andExpect(jsonPath("$.isbn", CoreMatchers.is(book.getIsbn())));

    }

    @Test
    @WithMockUser
    public void BookController_UpdateBook_ReturnNotFound() throws Exception {
        int bookId = 1;
        Book book = Book.builder().id(1L)
                .author("author1")
                .title("title1")
                .isbn("1234567")
                .publicationYear("1996")
                .build();
        when(bookService.saveBook(ArgumentMatchers.any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookService.getBookById(bookId)).thenReturn(Optional.ofNullable(null));

        ResultActions response = mockMvc.perform(put(apiPrefix + "/books/" + bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(book)));

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void BookController_AdminDeleteBookByADMIN_DeleteBookAndReturnOk() throws Exception {
        when(bookService.getBookById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(Mockito.mock(Book.class)));
        doNothing().when(bookService).deleteBook(ArgumentMatchers.anyLong());

        ResultActions response = mockMvc.perform(delete(apiPrefix + "/books/" + 1)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void BookController_AdminDeleteBook_DeleteBookAndNotFound() throws Exception {
        when(bookService.getBookById(ArgumentMatchers.anyLong())).thenReturn(Optional.ofNullable(null));
        doNothing().when(bookService).deleteBook(ArgumentMatchers.anyLong());

        ResultActions response = mockMvc.perform(delete(apiPrefix + "/books/" + 1)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNotFound());

    }
}
