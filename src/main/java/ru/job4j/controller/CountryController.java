package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.constant.TypeFailController;
import ru.job4j.model.Country;
import ru.job4j.service.CountryService;
import ru.job4j.util.UserSession;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class CountryController implements ControllerClass {
    private final CountryService countryService;
    private static final int TYPE = TypeFailController.COUNTRY;

    @Override
    @GetMapping("/formAddCountry")
    public String formAdd(Model model, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("country", new Country());
        return "addCountry";
    }

    @PostMapping("/createCountry")
    public String createCountry(@ModelAttribute Country country) {
        boolean rsl = countryService.add(country);
        String redirect = "redirect:/countries";
        if (!rsl) {
            redirect = failRedirect(TYPE, "Failed to create");
        }
        return redirect;
    }

    @Override
    @GetMapping("/countries")
    public String lists(Model model, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("countries", countryService.findAll());
        return "countries";
    }

    @GetMapping("/formUpdateCountry/{countryId}")
    public String formUpdateCountry(Model model, @PathVariable("countryId") int id, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        Optional<Country> countryOptional = countryService.findById(id);
        String redirect = failRedirect(TYPE, "Failed to update");
        if (countryOptional.isPresent()) {
            model.addAttribute("country", countryOptional.get());
            redirect = "updateCountry";
        }
        return redirect;
    }

    @PostMapping("/updateCountry")
    public String updateCountry(@ModelAttribute Country country) {
        boolean rsl = countryService.update(country);
        String redirect = "redirect:/countries";
        if (!rsl) {
            redirect = failRedirect(TYPE, "Failed to update");
        }
        return redirect;
    }
}
