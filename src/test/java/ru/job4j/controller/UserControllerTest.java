package ru.job4j.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import ru.job4j.constant.TypeFailController;
import ru.job4j.model.User;
import ru.job4j.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Test
    public void whenAddUser() {
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        String page = userController.formAdd(model, session);
        assertThat(page).isEqualTo("addUser");
    }

    @Test
    public void whenRegistrationUserSuccess() {
        User user = new User(1, "Name", "Password", "email");
        Optional<User> input = Optional.of(user);
        HttpSession session = mock(HttpSession.class);
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        when(userService.add(user)).thenReturn(input);
        String page = userController.registration(user, session);
        verify(userService).add(user);
        assertThat(page).isEqualTo("redirect:/index");
    }

    @Test
    public void whenRegistrationUserFail() {
        User user = new User(1, "Name", "Password", "email");
        Optional<User> input = Optional.empty();
        HttpSession session = mock(HttpSession.class);
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        when(userService.add(user)).thenReturn(input);
        String page = userController.registration(user, session);
        verify(userService).add(user);
        int type = TypeFailController.USER;
        assertThat(page)
                .isEqualTo(String
                        .format("redirect:/fail?type=%s&message=The user has already been created", type));
    }

    @Test
    public void whenFindAllUsers() {
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        String page = userController.lists(model, session);
        assertThat(page).isEqualTo("index");
    }

    @Test
    public void whenLoginPageRedirect() {
        Model model = mock(Model.class);
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        String page = userController.loginPage(model, false);
        assertThat(page).isEqualTo("login");
    }

    @Test
    public void whenLoginSuccess() {
        User user = new User(1, "Name", "Password", "email");
        Optional<User> input = Optional.of(user);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        UserService userService = mock(UserService.class);
        when(userService.findUserByEmailAndPwd(
                user.getEmail(), user.getPassword()
        )).thenReturn(input);
        when(httpServletRequest.getSession()).thenReturn(session);
        UserController userController = new UserController(userService);
        String page = userController.login(user, httpServletRequest);
        verify(userService).findUserByEmailAndPwd(Mockito.any(), Mockito.any());
        assertThat(page).isEqualTo("redirect:/index");
    }

    @Test
    public void whenLoginFail() {
        User user = new User(1, "Name", "Password", "email");
        Optional<User> input = Optional.empty();
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        UserService userService = mock(UserService.class);
        when(userService.findUserByEmailAndPwd(
                user.getEmail(), user.getPassword()
        )).thenReturn(input);
        UserController userController = new UserController(userService);
        String page = userController.login(user, httpServletRequest);
        verify(userService).findUserByEmailAndPwd(Mockito.any(), Mockito.any());
        assertThat(page).isEqualTo("redirect:/loginPage?fail=true");
    }

    @Test
    public void whenLoginOut() {
        HttpSession session = mock(HttpSession.class);
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        String page = userController.logout(session);
        assertThat(page).isEqualTo("redirect:/loginPage");
    }
}