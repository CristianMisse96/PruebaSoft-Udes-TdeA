package edu.co.udestdea.pruebasoft.web.app.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * Titulo: Proyecto NSEC
 * </p>
 * <p>
 * Descripción:enumerador para definición de los diferentes tipos de respuestas a una petición http
 * </p>
 *
 * @author Cristian Misse 
 *
 **/
public enum SeverityEnum {
	
	ERROR("error"), SUCCESS("success"), INFO("info"), WARN("warn");
	
	@JsonValue
	String key;

	private SeverityEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
