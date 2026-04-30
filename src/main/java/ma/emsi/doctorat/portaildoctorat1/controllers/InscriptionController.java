package ma.emsi.doctorat.portaildoctorat1.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.DocumentDTO;
import ma.emsi.doctorat.portaildoctorat1.dto.DossierDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.Campagne;
import ma.emsi.doctorat.portaildoctorat1.entities.Document;
import ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription;
import ma.emsi.doctorat.portaildoctorat1.repositories.DocumentRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DossierInscriptionRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DoctorantRepository;
import ma.emsi.doctorat.portaildoctorat1.security.CustomUserDetails;
import ma.emsi.doctorat.portaildoctorat1.entities.Doctorant;
import ma.emsi.doctorat.portaildoctorat1.services.InscriptionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/doctorant/inscription")
@PreAuthorize("hasRole('DOCTORANT')")
@RequiredArgsConstructor
public class InscriptionController {

    private final InscriptionService inscriptionService;
    private final DossierInscriptionRepository dossierRepository;
    private final DocumentRepository documentRepository;
    private final DoctorantRepository doctorantRepository;

    @GetMapping("/nouvelle")
    public String nouvelleInscription(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, RedirectAttributes redirectAttributes) {
        try {
            Campagne campagne = inscriptionService.getCampagneActive();
            model.addAttribute("campagne", campagne);
            model.addAttribute("dossier", new DossierDTO("", null, campagne.getOid()));
            model.addAttribute("directeurs", inscriptionService.getDirecteursDisponibles());
            return "doctorant/inscription/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/ui/dashboard/doctorant";
        }
    }

    @PostMapping("/nouvelle")
    public String soumettreInscription(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @ModelAttribute("dossier") DossierDTO dto, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("campagne", inscriptionService.getCampagneActive());
            model.addAttribute("directeurs", inscriptionService.getDirecteursDisponibles());
            return "doctorant/inscription/form";
        }
        try {
            Doctorant doctorant = doctorantRepository.findByUser_Email(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Doctorant introuvable"));
            DossierInscription dossier = inscriptionService.soumettreInscription(dto, doctorant.getOid());
            return "redirect:/doctorant/inscription/" + dossier.getOid() + "/documents";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/doctorant/inscription/nouvelle";
        }
    }

    @GetMapping("/{id}/documents")
    public String documents(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        DossierInscription dossier = dossierRepository.findById(id).orElseThrow();
        Doctorant doctorant = doctorantRepository.findByUser_Email(userDetails.getUsername()).orElseThrow();
        if(!dossier.getDoctorant().getOid().equals(doctorant.getOid())) {
            return "redirect:/ui/dashboard/doctorant";
        }
        List<Document> documents = documentRepository.findByDossierInscription(dossier);
        model.addAttribute("dossier", dossier);
        model.addAttribute("documents", documents);
        model.addAttribute("documentDto", new DocumentDTO("", null));
        return "doctorant/inscription/documents";
    }

    @PostMapping("/{id}/documents")
    public String uploadDocument(@PathVariable Long id, @Valid @ModelAttribute("documentDto") DocumentDTO dto, BindingResult result, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        try {
            inscriptionService.sauvegarderDocument(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Document ajouté.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/doctorant/inscription/" + id + "/documents";
    }

    @PostMapping("/{id}/soumettre")
    public String finaliserSoumission(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        try {
            inscriptionService.finaliserSoumission(id);
            redirectAttributes.addFlashAttribute("successMessage", "Dossier soumis avec succès pour validation.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/ui/dashboard/doctorant";
    }
}
