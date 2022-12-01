package ru.job4j.cinema.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.constant.TypeFailController;
import ru.job4j.cinema.model.Country;
import ru.job4j.cinema.service.CountryService;
import ru.job4j.cinema.util.UserSession;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Контроллер. Страна
 * @author Burlakov
 */
@Controller
@AllArgsConstructor
public class CountryController implements ControllerClass {
    private final CountryService countryService;
    private static final int TYPE = TypeFailController.COUNTRY;

    /**
     * форма добавления новой записи
     * @param model Model
     * @param session HttpSession
     * @return addCountry
     */
    @Override
    @GetMapping("/formAddCountry")
    public String formAdd(Model model, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("country", new Country());
        return "addCountry";
    }

    /**
     * добавление новой записи
     * @param country ModelAttribute country
     * @return redirect:/countries если успешно, failRedirect в противном случае
     */
    @PostMapping("/createCountry")
    public String createCountry(@ModelAttribute Country country) {
        boolean rsl = countryService.add(country);
        String redirect = "redirect:/countries";
        if (!rsl) {
            redirect = failRedirect(TYPE, "Failed to create");
        }
        return redirect;
    }

    /**
     * форма со списком всех элементов
     * @param model Model
     * @param session HttpSession
     * @return countries
     */
    @Override
    @GetMapping("/countries")
    public String lists(Model model, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("countries", countryService.findAll());
        return "countries";
    }

    /**
     * форма редактирования записи
     * @param model Model
     * @param id идентификатор записи, которую редактируем
     * @param session HttpSession
     * @return updateCountry если успешно, failRedirect в противном случае
     */
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

    /**
     * редактирование элемента
     * @param country ModelAttribute Country
     * @return redirect:/countries если успешно, failRedirect в противном случае
     */
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
