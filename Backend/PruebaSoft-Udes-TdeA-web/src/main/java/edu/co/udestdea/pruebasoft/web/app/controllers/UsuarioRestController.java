package edu.co.udestdea.pruebasoft.web.app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.co.udestdea.pruebasoft.web.app.models.dto.MensajeDTO;
import edu.co.udestdea.pruebasoft.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udestdea.pruebasoft.web.app.models.dto.UsuarioDTO;
import edu.co.udestdea.pruebasoft.web.app.models.enums.SeverityEnum;
import edu.co.udestdea.pruebasoft.web.app.service.task.UsuarioServiceTask;
import edu.co.udestdea.pruebasoft.web.app.util.Messages;
import jakarta.validation.Valid;

/**
 * <p>
 * Titulo: Proyecto PruebaSoft
 * </p>
 * <p>
 * Descripción: controlador REST para realizar procesos de gestión de usuarios.
 * </p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 *
 **/

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api/users")
public class UsuarioRestController {
	
	private Messages messages;
	private UsuarioServiceTask userServiceTask;
	
	private static final String KEY_ERROR_GENERICO = "error.peticion.generico";
	
	public UsuarioRestController(UsuarioServiceTask userServiceTask,Messages messages) {
		this.userServiceTask=userServiceTask;
		this.messages=messages;
	}
	
	@PostMapping(value="/create")
	public ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> newUserAdmin(
			@Valid @RequestBody UsuarioDTO userDTO,
			BindingResult result){
		
		if (result.hasErrors()) {
			RespuestaServicioDTO<UsuarioDTO> respuestaServicioDTO = new RespuestaServicioDTO<>();
			
			respuestaServicioDTO.setMensajeDTO(
					new MensajeDTO(SeverityEnum.ERROR, 
							messages.getKey(SeverityEnum.ERROR.getKey()),messages.getKey(KEY_ERROR_GENERICO)));
			
			respuestaServicioDTO.setOk(false);
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.BAD_REQUEST);
		}
		
		return userServiceTask.registarUsuario(userDTO);
		
	}
	
	@PostMapping(value="/register")
	public ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> newUser(
			@Valid @RequestBody UsuarioDTO userDTO,
			BindingResult result){
		
		userDTO.setStudent(true);
		return newUserAdmin(userDTO, result);
	}
}
