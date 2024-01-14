package cc.maids.libms.Patron;

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
public class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;
    @InjectMocks
    private PatronService patronService;

    @Test
    public void PatronService_GetAllPatrons_ReturnListOfAllPatrons() {
        Patron patron1 = Patron.builder()
                .name("name1")
                .contactInformation("contactInformation1")
                .build();

        Patron patron2 = Patron.builder()
                .name("name1")
                .contactInformation("contactInformation1")
                .build();

        List<Patron> patronList = List.of(patron1, patron2);

        when(patronRepository.findAll()).thenReturn(patronList);

        List<Patron> allPatrons = patronService.getAllPatrons();
        Assertions.assertThat(allPatrons).isNotNull();
    }

    @Test
    public void PatronService_GetPatronById_ReturnPatron() {
        Long patronId = 1l;
        Patron patron = Patron.builder()
                .id(patronId)
                .name("name1")
                .contactInformation("contactInformation1")
                .build();
        Optional<Patron> patronOp = Optional.of(patron);
        when(patronRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(patron));

        Optional<Patron> foundPatron = patronService.getPatronById(patronId);
        Assertions.assertThat(foundPatron).isEqualTo(patronOp);
    }

    @Test
    public void PatronService_SavePatron_ReturnSavedPatron() {
        Patron patron = Patron.builder()
                .name("name1")
                .contactInformation("contactInformation1")
                .build();

        when(patronRepository.save(Mockito.any(Patron.class))).thenReturn(patron);

        Patron savedPatron = patronService.savePatron(patron);

        Assertions.assertThat(savedPatron).isNotNull();
    }

    @Test
    public void PatronService_DeletePatron_ReturnVoid() {
        Long patronId = 1l;

        doNothing().when(patronRepository).deleteById(patronId);

        assertAll(() -> patronService.deletePatron(patronId));

    }
}
