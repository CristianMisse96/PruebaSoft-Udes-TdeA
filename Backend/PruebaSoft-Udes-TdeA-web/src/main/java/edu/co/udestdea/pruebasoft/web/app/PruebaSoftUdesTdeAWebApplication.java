package edu.co.udestdea.pruebasoft.web.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class PruebaSoftUdesTdeAWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(PruebaSoftUdesTdeAWebApplication.class, args);
	}

}
