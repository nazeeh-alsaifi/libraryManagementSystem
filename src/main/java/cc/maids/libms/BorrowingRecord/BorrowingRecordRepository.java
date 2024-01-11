package cc.maids.libms.BorrowingRecord;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord,Long> {

    Optional<BorrowingRecord> findFirstByBookIdOrderByBorrowingDateDesc(Long bookId);
    Optional<BorrowingRecord> findFirstByBookIdAndPatronIdOrderByBorrowingDateDesc(Long bookId, long patronId);
}
