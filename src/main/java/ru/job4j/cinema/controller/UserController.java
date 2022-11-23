package ru.job4j.cinema.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.constant.TypeFailController;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;
import ru.job4j.cinema.util.UserSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@ThreadSafe
@AllArgsConstructor
public class UserController implements ControllerClass {

    private final UserService userService;
    private static final int TYPE = TypeFailController.USER;

    @Override
    @GetMapping("/formAddUser")
    public String formAdd(Model model, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        return "addUser";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute User user, HttpSession session) {
        Optional<User> regUser = userService.add(user);
        String redirect = failRedirect(TYPE, "The user has already been created");
        if (regUser.isPresent()) {
            session.setAttribute("user", regUser.get());
            redirect = "redirect:/index";
        }
        return redirect;
    }

    @Override
    @GetMapping("/users")
    public String lists(Model model, HttpSession session) {
        /*
        С заделом на будущее, чтобы можно было выводить для админ пользователей и их редактировать
        в текущей версии не требуется
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("users", userService.findAll());
        return "users";
         */
        return "index";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model, @RequestParam(name = "fail",
            required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        Optional<User> userDb = userService.findUserByEmailAndPwd(
                user.getEmail(), user.getPassword()
        );
        String redirect = "redirect:/loginPage?fail=true";
        if (userDb.isPresent()) {
            HttpSession session = req.getSession();
            session.setAttribute("user", userDb.get());
            redirect = "redirect:/index";
        }
        return redirect;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }
}
