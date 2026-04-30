package ma.emsi.doctorat.portaildoctorat1.services;

import ma.emsi.doctorat.portaildoctorat1.dto.DossierDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.Campagne;
import ma.emsi.doctorat.portaildoctorat1.entities.Directeur;
import ma.emsi.doctorat.portaildoctorat1.entities.Doctorant;
import ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription;
import ma.emsi.doctorat.portaildoctorat1.repositories.CampagneRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DirecteurRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DoctorantRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DossierInscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InscriptionServiceTest {

    @Mock
    private CampagneRepository campagneRepository;
    @Mock
    private DirecteurRepository directeurRepository;
    @Mock
    private DoctorantRepository doctorantRepository;
    @Mock
    private DossierInscriptionRepository dossierRepository;

    @InjectMocks
    private InscriptionService inscriptionService;

    private Campagne activeCampagne;
    private Doctorant doctorant;
    private Directeur directeur;

    @BeforeEach
    void setUp() {
        activeCampagne = new Campagne();
        activeCampagne.setOid(1L);
        activeCampagne.setActive(true);
        activeCampagne.setDateOuverture(LocalDate.now().minusDays(1));
        activeCampagne.setDateFermeture(LocalDate.now().plusDays(30));

        doctorant = new Doctorant();
        doctorant.setOid(1L);
        doctorant.setDatePremiereInscription(LocalDate.now().minusYears(1));

        directeur = new Directeur();
        directeur.setOid(1L);
    }

    @Test
    void testSoumissionAvecCampagneActive() {
        DossierDTO dto = new DossierDTO("Sujet AI", 1L, 1L);

        when(campagneRepository.findByActiveTrue()).thenReturn(Optional.of(activeCampagne));
        when(doctorantRepository.findById(1L)).thenReturn(Optional.of(doctorant));
        when(dossierRepository.findByDoctorantAndCampagne(doctorant, activeCampagne)).thenReturn(Optional.empty());
        when(directeurRepository.findById(1L)).thenReturn(Optional.of(directeur));
        when(dossierRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        DossierInscription dossier = inscriptionService.soumettreInscription(dto, 1L);

        assertNotNull(dossier);
        assertEquals("Sujet AI", dossier.getSujetThese());
        verify(dossierRepository).save(any());
    }

    @Test
    void testSoumissionSansCampagneActive_leveException() {
        DossierDTO dto = new DossierDTO("Sujet AI", 1L, 1L);
        when(campagneRepository.findByActiveTrue()).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            inscriptionService.soumettreInscription(dto, 1L);
        });

        assertTrue(exception.getMessage().contains("Aucune campagne active ouverte"));
    }

    @Test
    void testSoumissionDoctorantDepasse3Ans_leveException() {
        doctorant.setDatePremiereInscription(LocalDate.now().minusYears(4)); // > 3 ans
        DossierDTO dto = new DossierDTO("Sujet AI", 1L, 1L);

        when(campagneRepository.findByActiveTrue()).thenReturn(Optional.of(activeCampagne));
        when(doctorantRepository.findById(1L)).thenReturn(Optional.of(doctorant));
        when(dossierRepository.findByDoctorantAndCampagne(doctorant, activeCampagne)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            inscriptionService.soumettreInscription(dto, 1L);
        });

        assertTrue(exception.getMessage().contains("Durée maximale de 3 ans dépassée"));
    }

    @Test
    void testSoumissionDoublonMemeCampagne_leveException() {
        DossierDTO dto = new DossierDTO("Sujet AI", 1L, 1L);

        when(campagneRepository.findByActiveTrue()).thenReturn(Optional.of(activeCampagne));
        when(doctorantRepository.findById(1L)).thenReturn(Optional.of(doctorant));
        when(dossierRepository.findByDoctorantAndCampagne(doctorant, activeCampagne)).thenReturn(Optional.of(new DossierInscription()));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            inscriptionService.soumettreInscription(dto, 1L);
        });

        assertTrue(exception.getMessage().contains("Vous avez déjà un dossier pour cette campagne."));
    }
}
