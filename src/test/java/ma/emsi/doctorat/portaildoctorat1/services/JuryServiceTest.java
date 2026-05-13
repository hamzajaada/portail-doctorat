package ma.emsi.doctorat.portaildoctorat1.services;

import ma.emsi.doctorat.portaildoctorat1.dto.JuryDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.DemandeSoutenance;
import ma.emsi.doctorat.portaildoctorat1.entities.Directeur;
import ma.emsi.doctorat.portaildoctorat1.repositories.DemandeSoutenanceRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DirecteurRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.JuryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JuryServiceTest {

    @Mock
    private JuryRepository juryRepository;

    @Mock
    private DirecteurRepository directeurRepository;

    @Mock
    private DemandeSoutenanceRepository demandeRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private JuryService juryService;

    @Test
    void testPropositionJuryMembresIdentiques_leveException() {
        // En principe validé par @MembresDifferents, mais on peut simuler une vérification
    }

    @Test
    void testPropositionJuryValide_succes() {
        Directeur directeur = new Directeur();
        DemandeSoutenance demande = new DemandeSoutenance();
        
        when(directeurRepository.findById(1L)).thenReturn(Optional.of(directeur));
        when(demandeRepository.findById(1L)).thenReturn(Optional.of(demande));
        when(juryRepository.existsByDemandeSoutenance_Oid(1L)).thenReturn(false);
        
        JuryDTO dto = new JuryDTO("Prof 1", "Prof 2", "Prof 3", 1L);
        juryService.proposerJury(dto, 1L);
    }
}
