package com.pokemon.pokidex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PokidexApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokidexApplication.class, args);
	}

}
