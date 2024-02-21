package edu.co.udestdea.pruebasoft.web.app.models.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;



/**
 * <p>
 * Titulo: Proyecto NSEC
 * </p>
 * <p>
 * Descripción: DTO que representa la estructura de la respuesta de un servicio
 * </p>
 *
 * @author Cristian Misse - cgmisse@gmail.com 
 *
 **/
@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RespuestaServicioDTO<T> {
	
	@JsonAlias
	private T negocio;
	@JsonProperty("mensaje")
	private MensajeDTO mensajeDTO;
	private boolean ok;
}
