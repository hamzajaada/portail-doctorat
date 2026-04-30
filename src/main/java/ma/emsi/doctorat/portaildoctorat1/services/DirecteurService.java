package ma.emsi.doctorat.portaildoctorat1.services;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.DirecteurDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.Directeur;
import ma.emsi.doctorat.portaildoctorat1.entities.Role;
import ma.emsi.doctorat.portaildoctorat1.entities.User;
import ma.emsi.doctorat.portaildoctorat1.exception.ResourceNotFoundException;
import ma.emsi.doctorat.portaildoctorat1.repositories.DirecteurRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DirecteurService {

    private final DirecteurRepository directeurRepository;
    private final UserRepository userRepository;

    public DirecteurDTO createDirecteur(DirecteurDTO dto) {
        User user = userRepository.findByEmail(dto.userEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email : " + dto.userEmail()));

        if (user.getRole() != Role.DIRECTEUR) {
            throw new IllegalArgumentException("L'utilisateur doit avoir le rôle DIRECTEUR");
        }

        if (directeurRepository.findByUser_Email(dto.userEmail()).isPresent()) {
            throw new IllegalArgumentException("Ce directeur existe déjà pour cet utilisateur");
        }

        Directeur directeur = new Directeur();
        directeur.setUser(user);
        directeur.setGrade(dto.grade());
        directeur.setDepartement(dto.departement());

        Directeur savedDirecteur = directeurRepository.save(directeur);
        return mapToDTO(savedDirecteur);
    }

    @Transactional(readOnly = true)
    public DirecteurDTO getDirecteurById(Long id) {
        Directeur directeur = directeurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Directeur non trouvé avec l'id : " + id));
        return mapToDTO(directeur);
    }

    @Transactional(readOnly = true)
    public List<DirecteurDTO> getAllDirecteurs() {
        return directeurRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public DirecteurDTO updateDirecteur(Long id, DirecteurDTO dto) {
        Directeur directeur = directeurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Directeur non trouvé avec l'id : " + id));

        if (!directeur.getUser().getEmail().equals(dto.userEmail())) {
            throw new IllegalArgumentException("Impossible de changer l'utilisateur associé au directeur de cette manière");
        }

        directeur.setGrade(dto.grade());
        directeur.setDepartement(dto.departement());

        return mapToDTO(directeurRepository.save(directeur));
    }

    public void deleteDirecteur(Long id) {
        Directeur directeur = directeurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Directeur non trouvé avec l'id : " + id));
        directeurRepository.delete(directeur);
    }

    @Transactional(readOnly = true)
    public DirecteurDTO getDirecteurByUserEmail(String email) {
        Directeur directeur = directeurRepository.findByUser_Email(email)
                .orElseThrow(() -> new ResourceNotFoundException("Directeur non trouvé pour l'email : " + email));
        return mapToDTO(directeur);
    }

    private DirecteurDTO mapToDTO(Directeur directeur) {
        return new DirecteurDTO(
                directeur.getOid(),
                directeur.getUser().getEmail(),
                directeur.getGrade(),
                directeur.getDepartement()
        );
    }
}
