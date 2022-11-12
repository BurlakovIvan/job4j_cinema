package ru.job4j.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.constant.TypeFailController;
import ru.job4j.util.UserSession;

import javax.servlet.http.HttpSession;

@Controller
@ThreadSafe
@AllArgsConstructor
public class FailController {

    @GetMapping("/fail")
    public String fail(Model model,
                       @RequestParam(name = "type") int type,
                       @RequestParam(name = "message") String message,
                       HttpSession session) {
        model.addAttribute("type", type);
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("message", message);
        return "fail";
    }

    @PostMapping("/failRedirect")
    public String failRedirect(@RequestParam("type") int type) {
        return switch (type) {
            case TypeFailController.USER -> "redirect:/formAddUser";
            case TypeFailController.COUNTRY -> "redirect:/countries";
            case TypeFailController.MOVIE -> "redirect:/movies";
            case TypeFailController.SESSION -> "redirect:/sessions";
            default -> "redirect:/index";
        };
    }
}
