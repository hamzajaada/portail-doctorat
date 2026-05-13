package ma.emsi.doctorat.portaildoctorat1.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/directeur/dashboard")
@PreAuthorize("hasRole('DIRECTEUR')")
public class DashboardDirecteurController {

    @GetMapping
    public String dashboard() {
        return "dashboard/directeur";
    }
}
