package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import ru.job4j.cinema.constant.TypeFailController;
import ru.job4j.cinema.model.Country;
import ru.job4j.cinema.service.ServiceCountry;
import ru.job4j.cinema.service.classes.CountryService;

import javax.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryControllerTest {

    @Test
    public void whenAddCountry() {
        Model model = mock(Model.class);
        ServiceCountry countryService = mock(CountryService.class);
        HttpSession session = mock(HttpSession.class);
        CountryController countryController = new CountryController(countryService);
        String page = countryController.formAdd(model, session);
        assertThat(page).isEqualTo("addCountry");
    }

    @Test
    public void whenCreateCountrySuccess() {
        Country input = new Country(1, "New Country");
        ServiceCountry countryService = mock(CountryService.class);
        CountryController countryController = new CountryController(countryService);
        when(countryService.add(input)).thenReturn(true);
        String page = countryController.createCountry(input);
        verify(countryService).add(input);
        assertThat(page).isEqualTo("redirect:/countries");
    }

    @Test
    public void whenCreateCountryFail() {
        Country input = new Country(1, "New Country");
        ServiceCountry countryService = mock(CountryService.class);
        CountryController countryController = new CountryController(countryService);
        when(countryService.add(input)).thenReturn(false);
        String page = countryController.createCountry(input);
        verify(countryService).add(input);
        int type = TypeFailController.COUNTRY;
        assertThat(page)
                .isEqualTo(String
                        .format("redirect:/fail?type=%s&message=Failed to create", type));
    }

    @Test
    public void whenAllCountries() {
        List<Country> countries = Arrays.asList(
                new Country(1, "Russia"),
                new Country(2, "Canada")
        );
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        ServiceCountry countryService = mock(CountryService.class);
        when(countryService.findAll()).thenReturn(countries);
        CountryController countryController = new CountryController(countryService);
        String page = countryController.lists(model, session);
        verify(countryService).findAll();
        assertThat(page).isEqualTo("countries");
    }

    @Test
    public void whenUpdateCountryByIdSuccess() {
        Country country = new Country(1, "New Country");
        Optional<Country> countryResult = Optional.of(country);
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        ServiceCountry countryService = mock(CountryService.class);
        when(countryService.findById(1)).thenReturn(countryResult);
        CountryController countryController = new CountryController(countryService);
        String page = countryController.formUpdateCountry(model, 1, session);
        verify(countryService).findById(1);
        assertThat(page).isEqualTo("updateCountry");
    }

    @Test
    public void whenUpdateCountryByIdFail() {
        Optional<Country> countryResult = Optional.empty();
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        ServiceCountry countryService = mock(CountryService.class);
        when(countryService.findById(1)).thenReturn(countryResult);
        CountryController countryController = new CountryController(countryService);
        String page = countryController.formUpdateCountry(model, 1, session);
        verify(countryService).findById(1);
        int type = TypeFailController.COUNTRY;
        assertThat(page)
                .isEqualTo(String
                        .format("redirect:/fail?type=%s&message=Failed to update", type));
    }

    @Test
    public void whenUpdateCountrySuccess() {
        Country input = new Country(1, "New Country");
        ServiceCountry countryService = mock(CountryService.class);
        when(countryService.update(input)).thenReturn(true);
        CountryController countryController = new CountryController(countryService);
        String page = countryController.updateCountry(input);
        verify(countryService).update(Mockito.any());
        assertThat(page).isEqualTo("redirect:/countries");
    }

    @Test
    public void whenUpdateCountryFail() {
        Country input = new Country(1, "New Country");
        ServiceCountry countryService = mock(CountryService.class);
        when(countryService.update(input)).thenReturn(false);
        CountryController countryController = new CountryController(countryService);
        String page = countryController.updateCountry(input);
        verify(countryService).update(Mockito.any());
        int type = TypeFailController.COUNTRY;
        assertThat(page)
                .isEqualTo(String
                        .format("redirect:/fail?type=%s&message=Failed to update", type));
    }
}