package edu.co.udestdea.pruebasoft.web.app.util;

import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.Nullable;

/**
 * <p>
 * Titulo: Proyecto PruebaSoft
 * </p>
 * <p>
 * Descripción:clase de utilidades para excepciones
 * </p>
 *
 * @author Cristian Misse 
 *
 **/
public class ExcepcionUtil {
	
	private ExcepcionUtil() {
		
	}
	
	public static synchronized boolean contains(Exception ex, @Nullable Class<?> exType) {
		if (exType == null) {
			return false;
		}
		if (exType.isInstance(ex)) {
			return true;
		}
		Throwable cause = ex.getCause();
		if (cause == ex) {
			return false;
		}
		if (cause instanceof NestedRuntimeException nestedEx && nestedEx.contains(exType)) {
			return true;
		}
		else {
			while (cause != null) {
				if (exType.isInstance(cause)) {
					return true;
				}
				if (cause.getCause() == cause) {
					break;
				}
				cause = cause.getCause();
			}
			return false;
		}
	}

}
