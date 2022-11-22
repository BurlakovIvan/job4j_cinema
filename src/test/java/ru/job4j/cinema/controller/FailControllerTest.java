package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.cinema.constant.TypeFailController;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class FailControllerTest {

    @Test
    public void whenFailCall() {
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        FailController failController = new FailController();
        String page = failController.fail(model, TypeFailController.SESSION,
                "Fail", session);
        assertThat(page).isEqualTo("fail");
    }

    @Test
    public void whenFailRedirectUser() {
        FailController failController = new FailController();
        String page = failController.failRedirect(TypeFailController.USER);
        assertThat(page).isEqualTo("redirect:/formAddUser");
    }

    @Test
    public void whenFailRedirectCountry() {
        FailController failController = new FailController();
        String page = failController.failRedirect(TypeFailController.COUNTRY);
        assertThat(page).isEqualTo("redirect:/countries");
    }

    @Test
    public void whenFailRedirectMovie() {
        FailController failController = new FailController();
        String page = failController.failRedirect(TypeFailController.MOVIE);
        assertThat(page).isEqualTo("redirect:/movies");
    }

    @Test
    public void whenFailRedirectSession() {
        FailController failController = new FailController();
        String page = failController.failRedirect(TypeFailController.SESSION);
        assertThat(page).isEqualTo("redirect:/sessions");
    }

    @Test
    public void whenFailRedirectTicket() {
        FailController failController = new FailController();
        String page = failController.failRedirect(TypeFailController.TICKET);
        assertThat(page).isEqualTo("redirect:/index");
    }

    @Test
    public void whenFailRedirectDefault() {
        FailController failController = new FailController();
        String page = failController.failRedirect(0);
        assertThat(page).isEqualTo("redirect:/index");
    }
}