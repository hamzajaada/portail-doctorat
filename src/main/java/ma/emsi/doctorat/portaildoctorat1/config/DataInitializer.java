package ma.emsi.doctorat.portaildoctorat1.config;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.entities.Directeur;
import ma.emsi.doctorat.portaildoctorat1.entities.Doctorant;
import ma.emsi.doctorat.portaildoctorat1.entities.Role;
import ma.emsi.doctorat.portaildoctorat1.entities.User;
import ma.emsi.doctorat.portaildoctorat1.repositories.DirecteurRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DoctorantRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DirecteurRepository directeurRepository;
    private final DoctorantRepository doctorantRepository;
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
            User directeurUser = new User();
            directeurUser.setNom("Directeur");
            directeurUser.setPrenom("Thèse");
            directeurUser.setEmail("directeur@portail.ma");
            directeurUser.setPassword(passwordEncoder.encode("dir123"));
            directeurUser.setRole(Role.DIRECTEUR);
            userRepository.save(directeurUser);
            
            Directeur directeur = new Directeur();
            directeur.setUser(directeurUser);
            directeur.setGrade("Professeur de l'Enseignement Supérieur");
            directeur.setDepartement("Informatique");
            directeurRepository.save(directeur);

            // Doctorant
            User doctorantUser = new User();
            doctorantUser.setNom("Doctorant");
            doctorantUser.setPrenom("Chercheur");
            doctorantUser.setEmail("doctorant@portail.ma");
            doctorantUser.setPassword(passwordEncoder.encode("doc123"));
            doctorantUser.setRole(Role.DOCTORANT);
            userRepository.save(doctorantUser);
            
            Doctorant doctorant = new Doctorant();
            doctorant.setUser(doctorantUser);
            doctorant.setDatePremiereInscription(java.time.LocalDate.now());
            doctorantRepository.save(doctorant);

            System.out.println("Comptes de test générés avec succès !");
        }
    }
}
