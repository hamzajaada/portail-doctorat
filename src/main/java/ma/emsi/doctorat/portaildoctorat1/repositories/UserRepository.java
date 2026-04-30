package ma.emsi.doctorat.portaildoctorat1.repositories;

import ma.emsi.doctorat.portaildoctorat1.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}