package ma.emsi.doctorat.portaildoctorat1.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.PrerequisDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.DemandeSoutenance;
import ma.emsi.doctorat.portaildoctorat1.entities.Doctorant;
import ma.emsi.doctorat.portaildoctorat1.entities.StatutSoutenance;
import ma.emsi.doctorat.portaildoctorat1.exception.PrerequisNonRemplisException;
import ma.emsi.doctorat.portaildoctorat1.repositories.DemandeSoutenanceRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DoctorantRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.JuryRepository;
import ma.emsi.doctorat.portaildoctorat1.services.PrerequisService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/doctorant/soutenance")
@PreAuthorize("hasRole('DOCTORANT')")
@RequiredArgsConstructor
public class SoutenanceController {

    private final PrerequisService prerequisService;
    private final DoctorantRepository doctorantRepository;
    private final DemandeSoutenanceRepository demandeRepository;
    private final JuryRepository juryRepository;

    @GetMapping("/nouvelle")
    public String nouvelle(Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        Doctorant doctorant = doctorantRepository.findByUser_Email(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Doctorant non trouvé"));

        Optional<DemandeSoutenance> demandeActive = demandeRepository.findByDoctorantAndStatutNot(doctorant, StatutSoutenance.REJETEE);
        if (demandeActive.isPresent()) {
            return "redirect:/doctorant/soutenance/suivi";
        }

        model.addAttribute("prerequisDTO", new PrerequisDTO(null, null, null, null, null, null, null, null));
        return "doctorant/soutenance/nouvelle";
    }

    @PostMapping("/nouvelle")
    public String soumettre(@Valid @ModelAttribute("prerequisDTO") PrerequisDTO dto, 
                            BindingResult bindingResult, 
                            Authentication authentication, 
                            RedirectAttributes redirectAttributes,
                            Model model) {
        if (bindingResult.hasErrors()) {
            return "doctorant/soutenance/nouvelle";
        }

        Doctorant doctorant = doctorantRepository.findByUser_Email(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Doctorant non trouvé"));

        try {
            prerequisService.soumettreDemandeAvecPrerequis(dto, doctorant.getOid());
            redirectAttributes.addFlashAttribute("successMessage", "Votre demande de soutenance a été soumise avec succès.");
            return "redirect:/doctorant/soutenance/suivi";
        } catch (PrerequisNonRemplisException e) {
            model.addAttribute("erreursPre", e.getErreurs());
            return "doctorant/soutenance/nouvelle";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "doctorant/soutenance/nouvelle";
        }
    }

    @GetMapping("/suivi")
    public String suivi(Model model, Authentication authentication) {
        Doctorant doctorant = doctorantRepository.findByUser_Email(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Doctorant non trouvé"));

        Optional<DemandeSoutenance> demandeOpt = demandeRepository.findByDoctorantAndStatutNot(doctorant, StatutSoutenance.REJETEE);
        
        if (demandeOpt.isEmpty()) {
            return "redirect:/doctorant/dashboard";
        }

        DemandeSoutenance demande = demandeOpt.get();
        model.addAttribute("demande", demande);
        
        if (demande.getStatut() == StatutSoutenance.JURY_PROPOSE || 
            demande.getStatut() == StatutSoutenance.AUTORISEE || 
            demande.getStatut() == StatutSoutenance.PLANIFIEE) {
            juryRepository.findByDemandeSoutenance(demande).ifPresent(jury -> model.addAttribute("jury", jury));
        }

        return "doctorant/soutenance/suivi";
    }
}
