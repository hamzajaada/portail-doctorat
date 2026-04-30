package ma.emsi.doctorat.portaildoctorat1.repositories;

import ma.emsi.doctorat.portaildoctorat1.entities.Doctorant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DoctorantRepository extends JpaRepository<Doctorant, Long> {
    Optional<Doctorant> findByUser_Email(String email);
}