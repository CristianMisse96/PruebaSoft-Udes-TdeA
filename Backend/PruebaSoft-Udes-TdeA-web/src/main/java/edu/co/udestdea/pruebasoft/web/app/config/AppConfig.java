package edu.co.udestdea.pruebasoft.web.app.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Titulo: Proyecto PruebaSoft
 * </p>
 * <p>
 * Descripción: Clase de configuración de la aplicación encargada de proveer beans.
 * </p>
 *
 * @author Cristian Misse
 *
 **/

@Configuration
public class AppConfig {
	
	@Bean
    ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
