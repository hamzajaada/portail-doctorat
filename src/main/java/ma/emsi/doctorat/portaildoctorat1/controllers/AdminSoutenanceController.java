package ma.emsi.doctorat.portaildoctorat1.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.PlanificationDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.DemandeSoutenance;
import ma.emsi.doctorat.portaildoctorat1.entities.Jury;
import ma.emsi.doctorat.portaildoctorat1.entities.StatutSoutenance;
import ma.emsi.doctorat.portaildoctorat1.repositories.DemandeSoutenanceRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.JuryRepository;
import ma.emsi.doctorat.portaildoctorat1.services.AdminSoutenanceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/soutenances")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminSoutenanceController {

    private final AdminSoutenanceService adminSoutenanceService;
    private final DemandeSoutenanceRepository demandeRepository;
    private final JuryRepository juryRepository;

    @GetMapping
    public String liste(@RequestParam(required = false) StatutSoutenance statut, Model model) {
        if (statut != null) {
            model.addAttribute("demandes", adminSoutenanceService.getDemandesByStatut(statut));
        } else {
            model.addAttribute("demandes", adminSoutenanceService.getAllDemandes());
        }
        return "admin/soutenances/liste";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        DemandeSoutenance demande = demandeRepository.findById(id).orElseThrow();
        model.addAttribute("demande", demande);
        juryRepository.findByDemandeSoutenance(demande).ifPresent(jury -> model.addAttribute("jury", jury));
        return "admin/soutenances/detail";
    }

    @PostMapping("/{id}/valider-prerequis")
    public String validerPrerequis(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adminSoutenanceService.validerPrerequis(id);
        redirectAttributes.addFlashAttribute("successMessage", "Les prérequis ont été validés.");
        return "redirect:/admin/soutenances/" + id;
    }

    @PostMapping("/{id}/autoriser")
    public String autoriser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adminSoutenanceService.autoriserSoutenance(id);
        redirectAttributes.addFlashAttribute("successMessage", "La soutenance a été autorisée.");
        return "redirect:/admin/soutenances/" + id;
    }

    @GetMapping("/{id}/planifier")
    public String planifierForm(@PathVariable Long id, Model model) {
        DemandeSoutenance demande = demandeRepository.findById(id).orElseThrow();
        model.addAttribute("demande", demande);
        model.addAttribute("planificationDTO", new PlanificationDTO(demande.getDateSoutenance(), demande.getLieuSoutenance(), null));
        juryRepository.findByDemandeSoutenance(demande).ifPresent(jury -> model.addAttribute("jury", jury));
        return "admin/soutenances/planifier";
    }

    @PostMapping("/{id}/planifier")
    public String planifierSubmit(@PathVariable Long id, 
                                  @Valid @ModelAttribute("planificationDTO") PlanificationDTO dto,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            DemandeSoutenance demande = demandeRepository.findById(id).orElseThrow();
            model.addAttribute("demande", demande);
            juryRepository.findByDemandeSoutenance(demande).ifPresent(jury -> model.addAttribute("jury", jury));
            return "admin/soutenances/planifier";
        }
        
        adminSoutenanceService.planifierSoutenance(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "La soutenance a été planifiée avec succès.");
        return "redirect:/admin/soutenances/" + id;
    }

    @PostMapping("/{id}/rejeter")
    public String rejeter(@PathVariable Long id, @RequestParam String motif, RedirectAttributes redirectAttributes) {
        adminSoutenanceService.rejeterDemande(id, motif);
        redirectAttributes.addFlashAttribute("errorMessage", "La demande de soutenance a été rejetée.");
        return "redirect:/admin/soutenances/" + id;
    }
}
