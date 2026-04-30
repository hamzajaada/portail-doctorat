package ma.emsi.doctorat.portaildoctorat1.services;

import ma.emsi.doctorat.portaildoctorat1.entities.Directeur;
import ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription;
import ma.emsi.doctorat.portaildoctorat1.entities.StatutDossier;
import ma.emsi.doctorat.portaildoctorat1.repositories.DossierInscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {

    @Mock
    private DossierInscriptionRepository dossierRepository;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ValidationService validationService;

    private DossierInscription dossier;
    private Directeur directeur;

    @BeforeEach
    void setUp() {
        directeur = new Directeur();
        directeur.setOid(10L);

        dossier = new DossierInscription();
        dossier.setOid(1L);
        dossier.setDirecteur(directeur);
        dossier.setStatut(StatutDossier.SOUMIS);
    }

    @Test
    void testValidationParDirecteurCorrect() {
        when(dossierRepository.findById(1L)).thenReturn(Optional.of(dossier));

        validationService.validerParDirecteur(1L, 10L, "Bon sujet");

        assertEquals(StatutDossier.VALIDE_DIRECTEUR, dossier.getStatut());
        verify(dossierRepository).save(dossier);
        verify(notificationService).notifierAdmin(dossier);
    }

    @Test
    void testValidationParMauvaisDirecteur_leveException() {
        when(dossierRepository.findById(1L)).thenReturn(Optional.of(dossier));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            validationService.validerParDirecteur(1L, 99L, "Avis"); // Wrong directeur ID
        });

        assertTrue(exception.getMessage().contains("Vous n'êtes pas le directeur de ce dossier"));
    }

    @Test
    void testTransitionStatutComplet() {
        when(dossierRepository.findById(1L)).thenReturn(Optional.of(dossier));

        // Validation admin
        validationService.validerParAdmin(1L);

        assertEquals(StatutDossier.VALIDE_ADMIN, dossier.getStatut());
        verify(dossierRepository).save(dossier);
        verify(notificationService).notifierDoctorant(any(), anyString());
    }
}
