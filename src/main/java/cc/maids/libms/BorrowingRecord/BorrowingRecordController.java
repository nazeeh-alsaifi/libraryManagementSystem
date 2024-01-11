package cc.maids.libms.BorrowingRecord;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("${apiPrefix}")
public class BorrowingRecordController {
    @Autowired
    private BorrowingRecordService borrowingRecordService;

    @GetMapping("/borrowingRecords")
    public ResponseEntity<List<BorrowingRecord>> getAllBorrowingRecords() {
        List<BorrowingRecord> borrowingRecordsList = borrowingRecordService.getAllBorrowingRecords();
        if (borrowingRecordsList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(borrowingRecordsList);
    }

    @GetMapping("/latestBorroingRecordOfBook/{bookId}")
    public ResponseEntity<BorrowingRecord> latestBorrowingRecordOfBookBy(@PathVariable long bookId) {
        Optional<BorrowingRecord> borrowingRecordOptional = borrowingRecordService
                .latestBorrowingRecordOfBookBy(bookId);
        return borrowingRecordOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecord> borrowBook(@PathVariable long bookId, @PathVariable long patronId) {
        try {
            BorrowingRecord br = borrowingRecordService.borrowBook(bookId, patronId);
            return ResponseEntity.status(HttpStatus.CREATED).body(br);
        } catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecord> returnBook(@PathVariable long bookId, @PathVariable long patronId) {
        try {
            BorrowingRecord updatedBr = borrowingRecordService.returnBook(bookId, patronId);
            return ResponseEntity.ok().body(updatedBr);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
