package edu.co.udestdea.pruebasoft.web.app.service.task;

import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import edu.co.udestdea.pruebasoft.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udestdea.pruebasoft.web.app.models.dto.UsuarioDTO;
import jakarta.validation.Valid;

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

	ResponseEntity<RespuestaServicioDTO<Boolean>> existeCorreo(String email);

	ResponseEntity<RespuestaServicioDTO<Boolean>> existeUsername(String username);

	ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> cargarImg(MultipartFile archivo, Long id);

	Resource obtenerImg(String nombreRecurso);

	ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> findUser(Long userId);

	ResponseEntity<RespuestaServicioDTO<Boolean>> updateProfile(UsuarioDTO userDTO, Long userId);

	ResponseEntity<RespuestaServicioDTO<Page<UsuarioDTO>>> getAll(Map<String, String> parametros);

	ResponseEntity<RespuestaServicioDTO<String>> stateChange(Long userId, Boolean estado);

	ResponseEntity<RespuestaServicioDTO<Boolean>> updateProfileAdmin(UsuarioDTO userDTO, Long userId);

}
