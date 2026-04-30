package ma.emsi.doctorat.portaildoctorat1.controllers;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription;
import ma.emsi.doctorat.portaildoctorat1.entities.StatutDossier;
import ma.emsi.doctorat.portaildoctorat1.repositories.DossierInscriptionRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DocumentRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DirecteurRepository;
import ma.emsi.doctorat.portaildoctorat1.security.CustomUserDetails;
import ma.emsi.doctorat.portaildoctorat1.entities.Directeur;
import ma.emsi.doctorat.portaildoctorat1.services.ValidationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/directeur/dossiers")
@PreAuthorize("hasRole('DIRECTEUR')")
@RequiredArgsConstructor
public class DirecteurController {

    private final DossierInscriptionRepository dossierRepository;
    private final DocumentRepository documentRepository;
    private final DirecteurRepository directeurRepository;
    private final ValidationService validationService;

    @GetMapping
    public String liste(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Directeur directeur = directeurRepository.findByUser_Email(userDetails.getUsername()).orElseThrow();
        List<DossierInscription> dossiers = dossierRepository.findByDirecteur(directeur);
        model.addAttribute("dossiers", dossiers);
        return "directeur/dossiers/liste";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        DossierInscription dossier = dossierRepository.findById(id).orElseThrow();
        Directeur directeur = directeurRepository.findByUser_Email(userDetails.getUsername()).orElseThrow();
        if (!dossier.getDirecteur().getOid().equals(directeur.getOid())) {
            return "redirect:/directeur/dossiers";
        }
        model.addAttribute("dossier", dossier);
        model.addAttribute("documents", documentRepository.findByDossierInscription(dossier));
        return "directeur/dossiers/detail";
    }

    @PostMapping("/{id}/valider")
    public String valider(@PathVariable Long id, @RequestParam String avis, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        try {
            Directeur directeur = directeurRepository.findByUser_Email(userDetails.getUsername()).orElseThrow();
            validationService.validerParDirecteur(id, directeur.getOid(), avis);
            redirectAttributes.addFlashAttribute("successMessage", "Dossier validé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/directeur/dossiers";
    }

    @PostMapping("/{id}/rejeter")
    public String rejeter(@PathVariable Long id, @RequestParam String motif, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        try {
            Directeur directeur = directeurRepository.findByUser_Email(userDetails.getUsername()).orElseThrow();
            validationService.rejeterParDirecteur(id, directeur.getOid(), motif);
            redirectAttributes.addFlashAttribute("successMessage", "Dossier rejeté.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/directeur/dossiers";
    }
}
