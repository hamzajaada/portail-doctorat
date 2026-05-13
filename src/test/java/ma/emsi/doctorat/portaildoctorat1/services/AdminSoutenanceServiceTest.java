package ma.emsi.doctorat.portaildoctorat1.services;

import ma.emsi.doctorat.portaildoctorat1.entities.DemandeSoutenance;
import ma.emsi.doctorat.portaildoctorat1.entities.StatutSoutenance;
import ma.emsi.doctorat.portaildoctorat1.repositories.DemandeSoutenanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminSoutenanceServiceTest {

    @Mock
    private DemandeSoutenanceRepository demandeRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AdminSoutenanceService adminSoutenanceService;

    @Test
    void testAutoriserSoutenance_verifierStatutPrecedent() {
        DemandeSoutenance demande = new DemandeSoutenance();
        demande.setStatut(StatutSoutenance.EN_ATTENTE);
        
        when(demandeRepository.findById(1L)).thenReturn(Optional.of(demande));
        
        assertThrows(RuntimeException.class, () -> adminSoutenanceService.autoriserSoutenance(1L));
    }
    
    @Test
    void testRejeterDemande_tousStatuts() {
        DemandeSoutenance demande = new DemandeSoutenance();
        demande.setStatut(StatutSoutenance.PLANIFIEE);
        
        // Mock doctorant et user inside for notification
        // adminSoutenanceService.rejeterDemande(1L, "Raison");
    }
}
