package ma.emsi.doctorat.portaildoctorat1.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription;
import ma.emsi.doctorat.portaildoctorat1.entities.Notification;
import ma.emsi.doctorat.portaildoctorat1.entities.Role;
import ma.emsi.doctorat.portaildoctorat1.entities.User;
import ma.emsi.doctorat.portaildoctorat1.repositories.NotificationRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public void notifierDirecteur(DossierInscription dossier) {
        log.info("Notification Directeur: Le dossier {} a été soumis.", dossier.getOid());
        Notification notification = new Notification();
        notification.setDestinataire(dossier.getDirecteur().getUser());
        notification.setDossier(dossier);
        notification.setMessage("Nouveau dossier soumis pour le doctorant " + dossier.getDoctorant().getUser().getNom());
        notification.setLu(false);
        notification.setDateCreation(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public void notifierDoctorant(DossierInscription dossier, String message) {
        log.info("Notification Doctorant: Dossier {} - {}", dossier.getOid(), message);
        Notification notification = new Notification();
        notification.setDestinataire(dossier.getDoctorant().getUser());
        notification.setDossier(dossier);
        notification.setMessage(message);
        notification.setLu(false);
        notification.setDateCreation(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public void notifierAdmin(DossierInscription dossier) {
        log.info("Notification Admin: Le dossier {} nécessite validation.", dossier.getOid());
        List<User> admins = userRepository.findAll().stream().filter(u -> u.getRole() == Role.ADMIN).toList();
        for(User admin : admins) {
            Notification notification = new Notification();
            notification.setDestinataire(admin);
            notification.setDossier(dossier);
            notification.setMessage("Le dossier " + dossier.getOid() + " a été validé par le directeur et attend votre validation.");
            notification.setLu(false);
            notification.setDateCreation(LocalDateTime.now());
            notificationRepository.save(notification);
        }
    }

    public void creerNotificationPourRole(String roleName, String message, String url) {
        Role role = Role.valueOf(roleName);
        List<User> users = userRepository.findAll().stream().filter(u -> u.getRole() == role).toList();
        for (User user : users) {
            Notification notification = new Notification();
            notification.setDestinataire(user);
            notification.setMessage(message + (url != null ? " (URL: " + url + ")" : ""));
            notification.setLu(false);
            notification.setDateCreation(LocalDateTime.now());
            notificationRepository.save(notification);
        }
    }

    public void creerNotification(Long userId, String message, String url) {
        userRepository.findById(userId).ifPresent(user -> {
            Notification notification = new Notification();
            notification.setDestinataire(user);
            notification.setMessage(message + (url != null ? " (URL: " + url + ")" : ""));
            notification.setLu(false);
            notification.setDateCreation(LocalDateTime.now());
            notificationRepository.save(notification);
        });
    }
}
