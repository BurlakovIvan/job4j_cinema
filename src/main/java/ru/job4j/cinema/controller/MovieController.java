package ru.job4j.cinema.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cinema.constant.TypeFailController;
import ru.job4j.cinema.model.Country;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.util.UserSession;
import ru.job4j.cinema.service.CountryService;
import ru.job4j.cinema.service.MovieService;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@ThreadSafe
@AllArgsConstructor
public class MovieController implements ControllerClass {
    private final MovieService movieService;
    private final CountryService countryService;
    private final SessionService sessionService;
    private static final int TYPE = TypeFailController.MOVIE;

    @Override
    @GetMapping("/formAddMovie")
    public String formAdd(Model model, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("createdDate", LocalDate.now());
        model.addAttribute("movie", new Movie());
        model.addAttribute("countries", countryService.findAll());
        return "addMovie";
    }

    @PostMapping("/createMovie")
    public String createMovie(@ModelAttribute Movie movie,
                              @RequestParam("file") MultipartFile file,
                              @RequestParam("createdDate")
                              @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate createdDate) throws IOException {
        movie.setCreated(createdDate);
        movie.setPhoto(file.getBytes());
        boolean rsl = movieService.add(movie);
        String redirect = "redirect:/movies";
        if (!rsl) {
            redirect = failRedirect(TYPE, "Failed to create");
        }
        return redirect;
    }

    @Override
    @GetMapping("/movies")
    public String lists(Model model, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        model.addAttribute("movies", movieService.findAllWithCountryName());
        return "movies";
    }

    @GetMapping("/formUpdateMovie/{movieId}")
    public String formUpdateMovie(Model model, @PathVariable("movieId") int id, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        Optional<Movie> movie = movieService.findById(id);
        String redirect = failRedirect(TYPE, "Failed to update");
        if (movie.isPresent()) {
            model.addAttribute("movie", movie.get());
            model.addAttribute("createDate", movie.get().getCreated());
            model.addAttribute("countries", countryService.findAll());
            redirect = "updateMovie";
        }
        return redirect;
    }

    @PostMapping("/updateMovie")
    public String updateMovie(@ModelAttribute Movie movie,
                              @RequestParam("file") MultipartFile file,
                              @RequestParam("createdDate")
                              @DateTimeFormat(pattern = "yyyy-MM-dd")
                                  LocalDate createdDate) throws IOException {
        movie.setPhoto(file.getBytes());
        movie.setCreated(createdDate);
        boolean rsl = movieService.update(movie);
        String redirect = "redirect:/movies";
        if (!rsl) {
            redirect = failRedirect(TYPE, "Failed to update");
        }
        return redirect;
    }

    @GetMapping("/photoMovie/{movieId}")
    public ResponseEntity<Resource> download(@PathVariable("movieId") Integer movieId) {
        Optional<Movie> movie = movieService.findById(movieId);
        ResponseEntity<Resource> result = null;
        if (movie.isPresent()) {
            result = ResponseEntity.ok()
                    .headers(new HttpHeaders())
                    .contentLength(movie.get().getPhoto().length)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new ByteArrayResource(movie.get().getPhoto()));
        }
        return result;
    }

    @GetMapping("/theMovie/{movieId}")
    public String theMovie(Model model,
                           @PathVariable("movieId") Integer movieId, HttpSession session) {
        model.addAttribute("user", UserSession.user(session));
        Optional<Movie> movie = movieService.findById(movieId);
        String redirect = failRedirect(TYPE, "Failed");
        if (movie.isPresent()) {
            model.addAttribute("movie", movie.get());
            model.addAttribute("sessions", sessionService.sessionForMovie(movie.get().getId()));
            Optional<Country> country = countryService.findById(movie.get().getCountryId());
            if (country.isPresent()) {
                model.addAttribute("country", country.get().getName());
                redirect = "movie";
            }
        }
        return redirect;
    }

}
