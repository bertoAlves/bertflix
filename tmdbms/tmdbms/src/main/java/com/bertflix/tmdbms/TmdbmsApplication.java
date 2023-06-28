package com.bertflix.tmdbms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TmdbmsApplication {

	public static void main(String[] args) {
		System.out.println("STARTING TMDB MS");
		SpringApplication.run(TmdbmsApplication.class, args);
	}

}
