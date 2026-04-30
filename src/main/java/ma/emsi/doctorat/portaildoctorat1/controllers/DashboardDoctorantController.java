package ma.emsi.doctorat.portaildoctorat1.controllers;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.entities.Doctorant;
import ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription;
import ma.emsi.doctorat.portaildoctorat1.entities.StatutDossier;
import ma.emsi.doctorat.portaildoctorat1.repositories.DossierInscriptionRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.NotificationRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DoctorantRepository;
import ma.emsi.doctorat.portaildoctorat1.security.CustomUserDetails;
import ma.emsi.doctorat.portaildoctorat1.services.InscriptionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/doctorant/dashboard")
@PreAuthorize("hasRole('DOCTORANT')")
@RequiredArgsConstructor
public class DashboardDoctorantController {

    private final DossierInscriptionRepository dossierRepository;
    private final NotificationRepository notificationRepository;
    private final InscriptionService inscriptionService;
    private final DoctorantRepository doctorantRepository;

    @GetMapping
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Doctorant doctorant = doctorantRepository.findByUser_Email(userDetails.getUsername()).orElseThrow();
        List<DossierInscription> dossiers = dossierRepository.findByDoctorant(doctorant);
        
        boolean alerte3ans = false;
        boolean alerte6ans = false;

        if (doctorant.getDatePremiereInscription() != null) {
            long months = ChronoUnit.MONTHS.between(doctorant.getDatePremiereInscription().withDayOfMonth(1), LocalDate.now().withDayOfMonth(1));
            if (months > 66) { // > 5 years and 6 months
                alerte6ans = true;
            } else if (months > 33) { // approaching 3 years
                alerte3ans = true;
            }
        }

        boolean hasCampagneActive = false;
        try {
            inscriptionService.getCampagneActive();
            hasCampagneActive = true;
        } catch (Exception ignored) {}

        boolean hasActiveDossier = dossiers.stream().anyMatch(d -> d.getStatut() != StatutDossier.REJETE);

        model.addAttribute("dossiers", dossiers);
        model.addAttribute("notifications", notificationRepository.findByDestinataireAndLuFalseOrderByDateCreationDesc(userDetails.getUser()));
        model.addAttribute("alerte3ans", alerte3ans);
        model.addAttribute("alerte6ans", alerte6ans);
        model.addAttribute("hasCampagneActive", hasCampagneActive);
        model.addAttribute("canCreateDossier", hasCampagneActive && !hasActiveDossier);

        return "doctorant/dashboard";
    }
}
