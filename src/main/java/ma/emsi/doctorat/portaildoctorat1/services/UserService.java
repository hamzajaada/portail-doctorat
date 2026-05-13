package ma.emsi.doctorat.portaildoctorat1.services;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.UserDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.User;
import ma.emsi.doctorat.portaildoctorat1.exception.ResourceNotFoundException;
import ma.emsi.doctorat.portaildoctorat1.repositories.UserRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DoctorantRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DirecteurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final DoctorantRepository doctorantRepository;
    private final DirecteurRepository directeurRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public UserDTO createUser(UserDTO userDTO) {
        if (userDTO.password() == null || userDTO.password().length() < 6) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire et doit contenir au moins 6 caractères");
        }
        
        String email = userDTO.email().trim().toLowerCase();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }
        
        User user = mapToEntity(userDTO);
        user.setEmail(email);
        User savedUser = userRepository.save(user);
        
        // Créer automatiquement le profil associé selon le rôle
        if (savedUser.getRole() == ma.emsi.doctorat.portaildoctorat1.entities.Role.DOCTORANT) {
            ma.emsi.doctorat.portaildoctorat1.entities.Doctorant doctorant = new ma.emsi.doctorat.portaildoctorat1.entities.Doctorant();
            doctorant.setUser(savedUser);
            doctorant.setAnneeThese(1);
            doctorant.setDatePremiereInscription(java.time.LocalDate.now());
            doctorantRepository.save(doctorant);
        } else if (savedUser.getRole() == ma.emsi.doctorat.portaildoctorat1.entities.Role.DIRECTEUR) {
            ma.emsi.doctorat.portaildoctorat1.entities.Directeur directeur = new ma.emsi.doctorat.portaildoctorat1.entities.Directeur();
            directeur.setUser(savedUser);
            directeurRepository.save(directeur);
        }
        
        return mapToDTO(savedUser);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + id));
        return mapToDTO(user);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + id));

        String newEmail = userDTO.email().trim().toLowerCase();
        if (!user.getEmail().equals(newEmail) && userRepository.findByEmail(newEmail).isPresent()) {
            throw new IllegalArgumentException("Cet email est déjà utilisé par un autre utilisateur");
        }

        user.setNom(userDTO.nom());
        user.setPrenom(userDTO.prenom());
        user.setEmail(newEmail);
        user.setRole(userDTO.role());
        if (userDTO.password() != null && !userDTO.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.password()));
        }

        return mapToDTO(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + id));
        
        doctorantRepository.findByUser_Email(user.getEmail())
                .ifPresent(doctorantRepository::delete);
                
        directeurRepository.findByUser_Email(user.getEmail())
                .ifPresent(directeurRepository::delete);

        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email : " + email));
        return mapToDTO(user);
    }

    // Mappers
    private User mapToEntity(UserDTO dto) {
        User user = new User();
        user.setNom(dto.nom());
        user.setPrenom(dto.prenom());
        user.setEmail(dto.email());
        if(dto.password() != null) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }
        user.setRole(dto.role());
        return user;
    }

    private UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getOid(),
                user.getNom(),
                user.getPrenom(),
                user.getEmail(),
                null, // Ne pas renvoyer le mot de passe
                user.getRole()
        );
    }
}
