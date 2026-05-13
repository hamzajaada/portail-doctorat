package ma.emsi.doctorat.portaildoctorat1.services;

import ma.emsi.doctorat.portaildoctorat1.dto.PrerequisDTO;
import ma.emsi.doctorat.portaildoctorat1.dto.PrerequisResultat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SoutenanceServiceTest {

    @InjectMocks
    private PrerequisService prerequisService;

    @Test
    void testSoumissionAvecPrerequisComplets_succes() {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "test".getBytes());
        PrerequisDTO dto = new PrerequisDTO(2, 2, 200, file, file, file, file, file);
        PrerequisResultat result = prerequisService.verifierPrerequis(dto);
        assertTrue(result.toutesConditionsRemplies());
    }

    @Test
    void testSoumissionSansPublicationsSuffisantes_leveException() {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "test".getBytes());
        PrerequisDTO dto = new PrerequisDTO(1, 2, 200, file, file, file, file, file);
        PrerequisResultat result = prerequisService.verifierPrerequis(dto);
        assertFalse(result.toutesConditionsRemplies());
        assertTrue(result.messagesErreur().contains("Au moins 2 publications Q1/Q2 requises"));
    }

    @Test
    void testSoumissionSansConferencesSuffisantes_leveException() {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "test".getBytes());
        PrerequisDTO dto = new PrerequisDTO(2, 1, 200, file, file, file, file, file);
        PrerequisResultat result = prerequisService.verifierPrerequis(dto);
        assertFalse(result.toutesConditionsRemplies());
        assertTrue(result.messagesErreur().contains("Au moins 2 conférences requises"));
    }

    @Test
    void testSoumissionSansHeuresFormation_leveException() {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "test".getBytes());
        PrerequisDTO dto = new PrerequisDTO(2, 2, 150, file, file, file, file, file);
        PrerequisResultat result = prerequisService.verifierPrerequis(dto);
        assertFalse(result.toutesConditionsRemplies());
        assertTrue(result.messagesErreur().contains("Au moins 200h de formation requises"));
    }
}
