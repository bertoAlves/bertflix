package com.bertflix.tmdbms.controllers;

import lombok.AllArgsConstructor;
import com.bertflix.tmdbms.services.MovieService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/movie")
@AllArgsConstructor
public class MovieController {

    private final MovieService movieservice;

    @GetMapping("/list")
    public String list() {
        return movieservice.list();
    }
}
