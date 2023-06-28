package com.bertflix.tmdbms.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class MovieService {
    private final TMDBService tmdbService;
    public String list() {
        try {
            return tmdbService.list();
        }catch (IOException ex){
            return "System Error -> " + ex.getMessage();
        }
    }
}
