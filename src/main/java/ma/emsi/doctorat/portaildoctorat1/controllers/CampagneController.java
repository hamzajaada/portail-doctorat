package ma.emsi.doctorat.portaildoctorat1.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.CampagneDTO;
import ma.emsi.doctorat.portaildoctorat1.services.CampagneService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/campagnes")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class CampagneController {

    private final CampagneService campagneService;

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("campagnes", campagneService.getAllCampagnes());
        return "admin/campagnes/liste";
    }

    @GetMapping("/nouvelle")
    public String nouvelle(Model model) {
        model.addAttribute("campagne", new CampagneDTO(null, "", null, null, false));
        return "admin/campagnes/form";
    }

    @PostMapping("/nouvelle")
    public String creer(@Valid @ModelAttribute("campagne") CampagneDTO dto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/campagnes/form";
        }
        try {
            campagneService.createCampagne(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Campagne créée avec succès.");
        } catch (Exception e) {
            result.rejectValue("active", "error.campagne", e.getMessage());
            return "admin/campagnes/form";
        }
        return "redirect:/admin/campagnes";
    }

    @GetMapping("/{id}/edit")
    public String editer(@PathVariable Long id, Model model) {
        model.addAttribute("campagne", campagneService.getCampagneById(id));
        return "admin/campagnes/form";
    }

    @PostMapping("/{id}/edit")
    public String modifier(@PathVariable Long id, @Valid @ModelAttribute("campagne") CampagneDTO dto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/campagnes/form";
        }
        try {
            campagneService.updateCampagne(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Campagne modifiée avec succès.");
        } catch (Exception e) {
            result.rejectValue("active", "error.campagne", e.getMessage());
            return "admin/campagnes/form";
        }
        return "redirect:/admin/campagnes";
    }

    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        campagneService.toggleActivation(id);
        redirectAttributes.addFlashAttribute("successMessage", "Statut de la campagne modifié.");
        return "redirect:/admin/campagnes";
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            campagneService.deleteCampagne(id);
            redirectAttributes.addFlashAttribute("successMessage", "Campagne supprimée.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/campagnes";
    }
}
