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
        if (userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }
        User user = mapToEntity(userDTO);
        User savedUser = userRepository.save(user);
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

        if (!user.getEmail().equals(userDTO.email()) && userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new IllegalArgumentException("Cet email est déjà utilisé par un autre utilisateur");
        }

        user.setNom(userDTO.nom());
        user.setPrenom(userDTO.prenom());
        user.setEmail(userDTO.email());
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
