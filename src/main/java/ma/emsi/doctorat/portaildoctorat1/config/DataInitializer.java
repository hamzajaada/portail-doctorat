package ma.emsi.doctorat.portaildoctorat1.config;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.entities.Role;
import ma.emsi.doctorat.portaildoctorat1.entities.User;
import ma.emsi.doctorat.portaildoctorat1.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

// CommandLineRunner il sait qu'il a l'ordre strict d'appuyer sur son bouton
// run() au démarrage

public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Admin
            User admin = new User();
            admin.setNom("Admin");
            admin.setPrenom("Système");
            admin.setEmail("admin@portail.ma");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            // Directeur
            User directeur = new User();
            directeur.setNom("Directeur");
            directeur.setPrenom("Thèse");
            directeur.setEmail("directeur@portail.ma");
            directeur.setPassword(passwordEncoder.encode("dir123"));
            directeur.setRole(Role.DIRECTEUR);
            userRepository.save(directeur);

            // Doctorant
            User doctorant = new User();
            doctorant.setNom("Doctorant");
            doctorant.setPrenom("Chercheur");
            doctorant.setEmail("doctorant@portail.ma");
            doctorant.setPassword(passwordEncoder.encode("doc123"));
            doctorant.setRole(Role.DOCTORANT);
            userRepository.save(doctorant);

            System.out.println("Comptes de test générés avec succès !");
        }
    }
}
