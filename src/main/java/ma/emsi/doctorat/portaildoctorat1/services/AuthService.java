package ma.emsi.doctorat.portaildoctorat1.services;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.UserDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.Role;
import ma.emsi.doctorat.portaildoctorat1.entities.User;
import ma.emsi.doctorat.portaildoctorat1.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerDoctorant(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new IllegalArgumentException("Un compte avec cet email existe déjà.");
        }
        User doctorant = new User();
        doctorant.setNom(userDTO.nom());
        doctorant.setPrenom(userDTO.prenom());
        doctorant.setEmail(userDTO.email());
        doctorant.setPassword(passwordEncoder.encode(userDTO.password()));
        doctorant.setRole(Role.DOCTORANT);
        userRepository.save(doctorant);
    }
}
