package cc.maids.libms.Patron;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class PatronRepositoryTest {

    @Autowired
    private PatronRepository patronRepository;

    @Test
    public void PatronRepository_FindAll_ReturnAllPatrons() {
        Patron patron1 = Patron.builder()
                .name("name1")
                .contactInformation("contactInformation1")

                .build();

        Patron patron2 = Patron.builder()
                .name("name1")
                .contactInformation("contactInformation1")

                .build();

        patronRepository.save(patron1);
        patronRepository.save(patron2);

        List<Patron> patronList = patronRepository.findAll();

        Assertions.assertThat(!patronList.isEmpty());
        Assertions.assertThat(patronList).isEqualTo(List.of(patron1, patron2));
    }

    @Test
    public void PatronRepository_Save_ReturnSavedPatron() {
        Patron patron = Patron.builder()
                .name("name1")
                .contactInformation("contactInformation1")

                .build();

        Patron savedPatron = patronRepository.save(patron);

        Assertions.assertThat(savedPatron).isNotNull();
        Assertions.assertThat(savedPatron.getId()).isGreaterThan(0);
    }

    @Test
    public void PatronRepository_FindById_ReturnPatron() {
        Patron patron = Patron.builder()
                .name("name1")
                .contactInformation("contactInformation1")

                .build();
        patronRepository.save(patron);

        Patron foundPatron = patronRepository.findById(patron.getId()).get();

        Assertions.assertThat(foundPatron).isNotNull();
        Assertions.assertThat(foundPatron.getName()).isEqualTo(patron.getName());
        Assertions.assertThat(foundPatron.getContactInformation()).isEqualTo(patron.getContactInformation());
    }

    @Test
    public void PatronRepository_DeleteById_ReturnIsEmptyPatron() {
        Patron patron = Patron.builder()
                .name("name1")
                .contactInformation("contactInformation1")

                .build();
        patronRepository.save(patron);

        patronRepository.deleteById(patron.getId());

        Optional<Patron> patronOp = patronRepository.findById(patron.getId());

        Assertions.assertThat(patronOp).isEmpty();

    }

}
