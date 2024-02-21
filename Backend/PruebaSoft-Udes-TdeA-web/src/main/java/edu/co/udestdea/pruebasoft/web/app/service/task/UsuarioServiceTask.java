package edu.co.udestdea.pruebasoft.web.app.service.task;

import org.springframework.http.ResponseEntity;

import edu.co.udestdea.pruebasoft.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udestdea.pruebasoft.web.app.models.dto.UsuarioDTO;

/**
 * <p>
 * Titulo: Proyecto PruebaSoft
 * </p>
 * <p>
 * Descripción: Interfaz para definir los metodos realizados sobra la entidad Usuario 
 * con sus correspondientes validaciones
 * </p>
 *
 * @author Cristian Misse
 *
 **/
public interface UsuarioServiceTask {
	/**
	 * Definición del facade que va realizar el llamado al service para registrar un usuario
	 * @param userDTO dto con la información suministrada para ser persistida
	 * @return dto con la informacion persistida
	 */
	ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> registarUsuario(UsuarioDTO userDTO);
}
