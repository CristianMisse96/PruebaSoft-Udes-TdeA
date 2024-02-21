package edu.co.udestdea.pruebasoft.web.app.exception;

import java.util.List;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class PruebaSoftException extends Exception {

	private final String mensaje;

	private final List<String> parametros;

	private static final long serialVersionUID = 1L;

	public PruebaSoftException(String mensaje) {
		super(mensaje);
		this.mensaje = mensaje;
		this.parametros = null;
	}

	public PruebaSoftException(String mensaje, List<String> parametros) {
		super(mensaje);
		this.parametros = parametros;
		this.mensaje = mensaje;
	}

	public PruebaSoftException(String mensaje, Throwable cause) {
		super(cause);
		this.mensaje = mensaje;
		this.parametros = null;
	}

	public PruebaSoftException(Exception e) {
		super(e);
		this.mensaje = e.getLocalizedMessage();
		this.parametros = null;
	}

	public String getMensaje() {
		if (mensaje == null || mensaje.isEmpty()) {
			log.warn("Se intenta obtener un mensaje de excepcion vacio. Imprimiendo traza de excepcion",
					this.getMessage());
			return this.getMessage();
		}
		return mensaje;
	}

	public List<String> getParametros() {
		return parametros;
	}

}
