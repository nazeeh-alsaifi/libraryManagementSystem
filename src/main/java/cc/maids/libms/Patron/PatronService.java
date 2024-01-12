package cc.maids.libms.Patron;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PatronService {
    @Autowired
    private PatronRepository patronRepository;

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();

    }

    @Cacheable("patrons")
    public Optional<Patron> getPatronById(long id) {
        return patronRepository.findById(id);
    }

    @CacheEvict(value = "patrons", key = "#patron.id")
    @Transactional
    public Patron savePatron(Patron patron) {
        return patronRepository.save(patron);
    }

    @CacheEvict("patrons")
    @Transactional
    public void deletePatron(Long id) {
        patronRepository.deleteById(id);
    }
}
