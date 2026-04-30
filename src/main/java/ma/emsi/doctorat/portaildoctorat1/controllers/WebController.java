package ma.emsi.doctorat.portaildoctorat1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")
public class WebController {

    // Dashboards dédiés
    @GetMapping({"", "/", "/dashboard/admin"}) public String adminDashboard() { return "dashboard/admin"; }
    @GetMapping("/dashboard/doctorant") public String doctorantDashboard() { return "dashboard/doctorant"; }
    @GetMapping("/dashboard/directeur") public String directeurDashboard() { return "dashboard/directeur"; }

    // Admin
    @GetMapping("/admin/campagnes") public String adminCampagnes() { return "admin/campagnes-list"; }
    @GetMapping("/admin/dossiers") public String adminDossiers() { return "admin/dossiers-list"; }
    @GetMapping("/admin/users") public String adminUsers() { return "admin/users-list"; }
    @GetMapping("/admin/soutenances") public String adminSoutenances() { return "admin/soutenances-list"; }

    // Doctorant
    @GetMapping("/doctorant/dossiers") public String doctorantDossiers() { return "doctorant/dossier-list"; }
    @GetMapping("/doctorant/dossiers/nouveau") public String doctorantDossierForm() { return "doctorant/dossier-form"; }
    @GetMapping("/doctorant/soutenances/nouveau") public String doctorantSoutenanceForm() { return "doctorant/soutenance-form"; }

    // Directeur
    @GetMapping("/directeur/dossiers") public String directeurDossiers() { return "directeur/dossiers-list"; }
    @GetMapping("/directeur/doctorants") public String directeurDoctorants() { return "directeur/doctorants-list"; }
}
