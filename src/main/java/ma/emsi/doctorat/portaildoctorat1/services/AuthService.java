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
    private final ma.emsi.doctorat.portaildoctorat1.repositories.DoctorantRepository doctorantRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerDoctorant(UserDTO userDTO) {
        String email = userDTO.email().trim().toLowerCase();
        System.out.println("Tentative d'inscription pour : " + email);
        
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Un compte avec cet email existe déjà.");
        }
        
        User user = new User();
        user.setNom(userDTO.nom());
        user.setPrenom(userDTO.prenom());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        user.setRole(Role.DOCTORANT);
        
        User savedUser = userRepository.save(user);
        System.out.println("Utilisateur sauvegardé avec ID : " + savedUser.getOid());

        // Créer automatiquement le profil Doctorant associé
        ma.emsi.doctorat.portaildoctorat1.entities.Doctorant doctorant = new ma.emsi.doctorat.portaildoctorat1.entities.Doctorant();
        doctorant.setUser(savedUser);
        doctorant.setAnneeThese(1); // Première année par défaut
        doctorant.setDatePremiereInscription(java.time.LocalDate.now());
        doctorantRepository.save(doctorant);
        System.out.println("Profil Doctorant créé avec succès.");
    }
}
