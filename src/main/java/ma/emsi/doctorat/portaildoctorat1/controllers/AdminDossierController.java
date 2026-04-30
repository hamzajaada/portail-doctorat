package ma.emsi.doctorat.portaildoctorat1.controllers;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription;
import ma.emsi.doctorat.portaildoctorat1.repositories.DocumentRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DossierInscriptionRepository;
import ma.emsi.doctorat.portaildoctorat1.services.ValidationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/dossiers")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminDossierController {

    private final DossierInscriptionRepository dossierRepository;
    private final DocumentRepository documentRepository;
    private final ValidationService validationService;

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("dossiers", dossierRepository.findAll());
        return "admin/dossiers/liste";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        DossierInscription dossier = dossierRepository.findById(id).orElseThrow();
        model.addAttribute("dossier", dossier);
        model.addAttribute("documents", documentRepository.findByDossierInscription(dossier));
        return "admin/dossiers/detail";
    }

    @PostMapping("/{id}/valider")
    public String valider(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            validationService.validerParAdmin(id);
            redirectAttributes.addFlashAttribute("successMessage", "Dossier validé.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/dossiers";
    }

    @PostMapping("/{id}/rejeter")
    public String rejeter(@PathVariable Long id, @RequestParam String motif, RedirectAttributes redirectAttributes) {
        try {
            validationService.rejeterParAdmin(id, motif);
            redirectAttributes.addFlashAttribute("successMessage", "Dossier rejeté.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/dossiers";
    }
}
