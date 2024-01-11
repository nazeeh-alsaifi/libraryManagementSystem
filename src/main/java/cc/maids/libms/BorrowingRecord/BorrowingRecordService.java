package cc.maids.libms.BorrowingRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.maids.libms.Book.Book;
import cc.maids.libms.Book.BookRepository;
import cc.maids.libms.Patron.Patron;
import cc.maids.libms.Patron.PatronRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class BorrowingRecordService {
    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingRecordRepository.findAll();
    }

    public Optional<BorrowingRecord> latestBorrowingRecordOfBookBy(Long bookId) {
        return borrowingRecordRepository.findFirstByBookIdOrderByBorrowingDateDesc(bookId);
    }

    public Optional<BorrowingRecord> latestBorrowingRecordOfBookBy(Long bookId, Long patronId) {
        return borrowingRecordRepository.findFirstByBookIdAndPatronIdOrderByBorrowingDateDesc(bookId, patronId);
    }

    @Transactional
    public BorrowingRecord borrowBook(long bookId, long patronId) throws RuntimeException, EntityNotFoundException {
        Optional<Book> book = bookRepository.findByIdPessimistic(bookId);
        if (!book.isPresent())
            throw new EntityNotFoundException("book not found with id: " + bookId);

        Optional<Patron> patron = patronRepository.findById(patronId);
        if (!patron.isPresent())
            throw new EntityNotFoundException("patron not found with id: " + patronId);

        if (this.bookIsborrowedAndNotRetruned(bookId))
            throw new RuntimeException("book has already been borrowed and not returned yet.");

        // complete the borrowing and return the record
        BorrowingRecord br = new BorrowingRecord();
        br.setBook(book.get());
        br.setPatron(patron.get());
        br.setBorrowingDate(LocalDateTime.now());
        BorrowingRecord savedBr = borrowingRecordRepository.save(br);

        return savedBr;

    }

    @Transactional
    public BorrowingRecord returnBook(long bookId, long patronId) {
        Optional<BorrowingRecord> latestBorrowingRecordByBookAndPatron = this.latestBorrowingRecordOfBookBy(bookId,
                patronId);

        if (!latestBorrowingRecordByBookAndPatron.isPresent())
            throw new RuntimeException("Not in records: patron " + patronId + " didn't borrow book " + bookId);

        BorrowingRecord br = latestBorrowingRecordByBookAndPatron.get();
        if (br.getReturnDate() != null)
            throw new RuntimeException(
                    "SECOND RETURN! Something is not right! in our records patron " + patronId + " returned " + bookId);

        br.setReturnDate(LocalDateTime.now());

        BorrowingRecord updatedBr = borrowingRecordRepository.save(br);

        return updatedBr;
    }

    private boolean bookIsborrowedAndNotRetruned(long bookId) {
        Optional<BorrowingRecord> latestBorrowingRecordOp = this.latestBorrowingRecordOfBookBy(bookId);

        // never been borrowed by anyone before - no records of this book
        if (!latestBorrowingRecordOp.isPresent())
            return false;

        BorrowingRecord borrowingRecord = latestBorrowingRecordOp.get();
        // first borrow
        if (borrowingRecord.getReturnDate() == null)
            return true;

        // borrowingDate after returnDate - TRUE - borrowed and not returned
        // borrowingDate after returnDate - FALSE - borrowed and returned
        return false;
    }

}
