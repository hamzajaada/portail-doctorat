package ma.emsi.doctorat.portaildoctorat1.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.JuryDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.DemandeSoutenance;
import ma.emsi.doctorat.portaildoctorat1.entities.Directeur;
import ma.emsi.doctorat.portaildoctorat1.entities.Jury;
import ma.emsi.doctorat.portaildoctorat1.repositories.DemandeSoutenanceRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DirecteurRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.JuryRepository;
import ma.emsi.doctorat.portaildoctorat1.services.JuryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/directeur/soutenances")
@PreAuthorize("hasRole('DIRECTEUR')")
@RequiredArgsConstructor
public class DirecteurSoutenanceController {

    private final JuryService juryService;
    private final DirecteurRepository directeurRepository;
    private final DemandeSoutenanceRepository demandeRepository;
    private final JuryRepository juryRepository;

    @GetMapping
    public String liste(Model model, Authentication authentication) {
        Directeur directeur = directeurRepository.findByUser_Email(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Directeur non trouvé"));
                
        List<DemandeSoutenance> demandes = juryService.getDemandesEnAttentePourDirecteur(directeur.getOid());
        model.addAttribute("demandes", demandes);
        return "directeur/soutenances/liste";
    }

    @GetMapping("/{demandeId}/jury/nouveau")
    public String nouveauJuryForm(@PathVariable Long demandeId, Model model, Authentication authentication) {
        DemandeSoutenance demande = demandeRepository.findById(demandeId).orElseThrow();
        model.addAttribute("demande", demande);
        model.addAttribute("juryDTO", new JuryDTO(null, null, null, demandeId));
        return "directeur/soutenances/jury-form";
    }

    @PostMapping("/{demandeId}/jury/nouveau")
    public String nouveauJurySubmit(@PathVariable Long demandeId, 
                                    @Valid @ModelAttribute("juryDTO") JuryDTO dto, 
                                    BindingResult bindingResult,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        DemandeSoutenance demande = demandeRepository.findById(demandeId).orElseThrow();
        if (bindingResult.hasErrors()) {
            model.addAttribute("demande", demande);
            return "directeur/soutenances/jury-form";
        }
        
        Directeur directeur = directeurRepository.findByUser_Email(authentication.getName()).orElseThrow();
        
        try {
            juryService.proposerJury(dto, directeur.getOid());
            redirectAttributes.addFlashAttribute("successMessage", "Le jury a été proposé avec succès.");
            return "redirect:/directeur/soutenances";
        } catch (Exception e) {
            model.addAttribute("demande", demande);
            model.addAttribute("errorMessage", e.getMessage());
            return "directeur/soutenances/jury-form";
        }
    }

    @GetMapping("/{demandeId}/jury/modifier")
    public String modifierJuryForm(@PathVariable Long demandeId, Model model) {
        DemandeSoutenance demande = demandeRepository.findById(demandeId).orElseThrow();
        Jury jury = juryRepository.findByDemandeSoutenance(demande).orElseThrow();
        
        model.addAttribute("demande", demande);
        model.addAttribute("juryDTO", new JuryDTO(jury.getPresident(), jury.getRapporteur1(), jury.getRapporteur2(), demandeId));
        model.addAttribute("juryId", jury.getOid());
        return "directeur/soutenances/jury-form";
    }

    @PostMapping("/{demandeId}/jury/modifier")
    public String modifierJurySubmit(@PathVariable Long demandeId, 
                                     @RequestParam Long juryId,
                                     @Valid @ModelAttribute("juryDTO") JuryDTO dto, 
                                     BindingResult bindingResult,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        DemandeSoutenance demande = demandeRepository.findById(demandeId).orElseThrow();
        if (bindingResult.hasErrors()) {
            model.addAttribute("demande", demande);
            model.addAttribute("juryId", juryId);
            return "directeur/soutenances/jury-form";
        }
        
        Directeur directeur = directeurRepository.findByUser_Email(authentication.getName()).orElseThrow();
        
        try {
            juryService.modifierJury(juryId, dto, directeur.getOid());
            redirectAttributes.addFlashAttribute("successMessage", "Le jury a été modifié avec succès.");
            return "redirect:/directeur/soutenances";
        } catch (Exception e) {
            model.addAttribute("demande", demande);
            model.addAttribute("juryId", juryId);
            model.addAttribute("errorMessage", e.getMessage());
            return "directeur/soutenances/jury-form";
        }
    }
}
