package ma.emsi.doctorat.portaildoctorat1.security;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.entities.User;
import ma.emsi.doctorat.portaildoctorat1.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Tentative de connexion pour : " + email);
        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> {
                    System.out.println("Utilisateur non trouvé pour : " + email);
                    return new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email);
                });
        System.out.println("Utilisateur trouvé : " + user.getEmail() + " avec rôle : " + user.getRole());
        return new CustomUserDetails(user);
    }
}
