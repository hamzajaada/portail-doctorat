package ma.emsi.doctorat.portaildoctorat1.services;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.DoctorantDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.Doctorant;
import ma.emsi.doctorat.portaildoctorat1.entities.Role;
import ma.emsi.doctorat.portaildoctorat1.entities.User;
import ma.emsi.doctorat.portaildoctorat1.exception.ResourceNotFoundException;
import ma.emsi.doctorat.portaildoctorat1.repositories.DoctorantRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorantService {

    private final DoctorantRepository doctorantRepository;
    private final UserRepository userRepository;

    public DoctorantDTO createDoctorant(DoctorantDTO dto) {
        User user = userRepository.findByEmail(dto.userEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email : " + dto.userEmail()));

        if (user.getRole() != Role.DOCTORANT) {
            throw new IllegalArgumentException("L'utilisateur doit avoir le rôle DOCTORANT");
        }

        if (doctorantRepository.findByUser_Email(dto.userEmail()).isPresent()) {
            throw new IllegalArgumentException("Ce doctorant existe déjà pour cet utilisateur");
        }

        Doctorant doctorant = new Doctorant();
        doctorant.setUser(user);
        doctorant.setCne(dto.cne());
        doctorant.setAnneeThese(dto.anneeThese());
        doctorant.setDatePremiereInscription(dto.datePremiereInscription());

        Doctorant savedDoctorant = doctorantRepository.save(doctorant);
        return mapToDTO(savedDoctorant);
    }

    @Transactional(readOnly = true)
    public DoctorantDTO getDoctorantById(Long id) {
        Doctorant doctorant = doctorantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctorant non trouvé avec l'id : " + id));
        return mapToDTO(doctorant);
    }

    @Transactional(readOnly = true)
    public List<DoctorantDTO> getAllDoctorants() {
        return doctorantRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public DoctorantDTO updateDoctorant(Long id, DoctorantDTO dto) {
        Doctorant doctorant = doctorantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctorant non trouvé avec l'id : " + id));

        // On ne permet pas de changer le User lié facilement, on vérifie si l'email correspond
        if (!doctorant.getUser().getEmail().equals(dto.userEmail())) {
            throw new IllegalArgumentException("Impossible de changer l'utilisateur associé au doctorant de cette manière");
        }

        doctorant.setCne(dto.cne());
        doctorant.setAnneeThese(dto.anneeThese());
        doctorant.setDatePremiereInscription(dto.datePremiereInscription());

        return mapToDTO(doctorantRepository.save(doctorant));
    }

    public void deleteDoctorant(Long id) {
        Doctorant doctorant = doctorantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctorant non trouvé avec l'id : " + id));
        doctorantRepository.delete(doctorant);
    }

    @Transactional(readOnly = true)
    public DoctorantDTO getDoctorantByUserEmail(String email) {
        Doctorant doctorant = doctorantRepository.findByUser_Email(email)
                .orElseThrow(() -> new ResourceNotFoundException("Doctorant non trouvé pour l'email : " + email));
        return mapToDTO(doctorant);
    }

    private DoctorantDTO mapToDTO(Doctorant doctorant) {
        return new DoctorantDTO(
                doctorant.getOid(),
                doctorant.getUser().getEmail(),
                doctorant.getCne(),
                doctorant.getAnneeThese(),
                doctorant.getDatePremiereInscription()
        );
    }
}
