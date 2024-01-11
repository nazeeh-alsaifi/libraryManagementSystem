package cc.maids.libms.BorrowingRecord;

import java.time.LocalDateTime;

import cc.maids.libms.Book.Book;
import cc.maids.libms.Patron.Patron;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class BorrowingRecord {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "patron_id", nullable = false)
    private Patron patron;

    @NotNull(message = "Borrowing date is required")
    @Column(nullable = false)
    private LocalDateTime borrowingDate;

    @Column(nullable = true)
    private LocalDateTime returnDate;

    public BorrowingRecord() {
    }

    public BorrowingRecord(Book book, Patron patron,LocalDateTime borrowingDate, LocalDateTime returnDate) {
        this.book = book;
        this.patron = patron;
        this.borrowingDate = borrowingDate;
        this.returnDate = returnDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Patron getPatron() {
        return patron;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }

    public LocalDateTime getBorrowingDate() {
        return borrowingDate;
    }

    public void setBorrowingDate(LocalDateTime borrowingDate) {
        this.borrowingDate = borrowingDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

}
