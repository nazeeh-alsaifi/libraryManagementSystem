package cc.maids.libms.Book;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();

    }

    @Cacheable("books")
    public Optional<Book> getBookById(long id) {
        return bookRepository.findById(id);
    }

    @CacheEvict(value = "books", key = "#book.id")
    @Transactional
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @CacheEvict(value = "books")
    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
