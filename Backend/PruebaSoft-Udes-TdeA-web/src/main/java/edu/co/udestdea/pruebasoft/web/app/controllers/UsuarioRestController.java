package edu.co.udestdea.pruebasoft.web.app.controllers;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.co.udestdea.pruebasoft.web.app.models.dto.MensajeDTO;
import edu.co.udestdea.pruebasoft.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udestdea.pruebasoft.web.app.models.dto.UsuarioDTO;
import edu.co.udestdea.pruebasoft.web.app.models.enums.SeverityEnum;
import edu.co.udestdea.pruebasoft.web.app.service.task.UsuarioServiceTask;
import edu.co.udestdea.pruebasoft.web.app.util.Messages;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import static edu.co.udestdea.pruebasoft.web.app.util.SpringSecurityConstantes.HEADER_AUTHORIZATION;
import static edu.co.udestdea.pruebasoft.web.app.util.SpringSecurityConstantes.SECRET_KEY;

import java.util.Map;

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
	
	@GetMapping(value = "/isemail/{email}")
	public ResponseEntity<RespuestaServicioDTO<Boolean>> existeCorreo(@PathVariable String email){
		
		if (!StringUtils.hasText(email)) {
			RespuestaServicioDTO<Boolean> respuestaServicioDTO = new RespuestaServicioDTO<>();
			
			respuestaServicioDTO.setMensajeDTO(
					new MensajeDTO(SeverityEnum.ERROR, 
							messages.getKey(SeverityEnum.ERROR.getKey()),messages.getKey(KEY_ERROR_GENERICO)));
			
			respuestaServicioDTO.setOk(false);
			respuestaServicioDTO.setNegocio(false);
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.BAD_REQUEST);
		}
		return userServiceTask.existeCorreo(email);
	}
	
	@GetMapping(value = "/isusername/{username}")
	public ResponseEntity<RespuestaServicioDTO<Boolean>> existeUsername(@PathVariable String username){
		
		if (!StringUtils.hasText(username)) {
			RespuestaServicioDTO<Boolean> respuestaServicioDTO = new RespuestaServicioDTO<>();
			
			respuestaServicioDTO.setMensajeDTO(
					new MensajeDTO(SeverityEnum.ERROR, 
							messages.getKey(SeverityEnum.ERROR.getKey()),messages.getKey(KEY_ERROR_GENERICO)));
			
			respuestaServicioDTO.setOk(false);
			respuestaServicioDTO.setNegocio(false);
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.BAD_REQUEST);
		}
		return userServiceTask.existeUsername(username);
	}
	
	@GetMapping(value = "/finduser/{userId}")
	public ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> buscarUsuario(@PathVariable Long userId, 
						@RequestHeader(HEADER_AUTHORIZATION) String authorizationHeader){
        
        if (validarId(authorizationHeader,userId)) {
        	RespuestaServicioDTO<UsuarioDTO> respuestaServicioDTO = new RespuestaServicioDTO<>();
			
			respuestaServicioDTO.setMensajeDTO(
					new MensajeDTO(SeverityEnum.ERROR, 
							messages.getKey(SeverityEnum.ERROR.getKey()),messages.getKey(KEY_ERROR_GENERICO)));
			
			respuestaServicioDTO.setOk(false);
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.BAD_REQUEST);
        }
        
        return userServiceTask.findUser(userId);
	}
	
	@PutMapping(value = "/edit/profile/{userId}")
	public ResponseEntity<RespuestaServicioDTO<Boolean>> updateProfile(@Valid @RequestBody UsuarioDTO userDTO,BindingResult result,
			@PathVariable Long userId){
		
		if (result.hasErrors()) {
			RespuestaServicioDTO<Boolean> respuestaServicioDTO = new RespuestaServicioDTO<>();
			
			respuestaServicioDTO.setMensajeDTO(
					new MensajeDTO(SeverityEnum.ERROR, 
							messages.getKey(SeverityEnum.ERROR.getKey()),messages.getKey(KEY_ERROR_GENERICO)));
			
			respuestaServicioDTO.setOk(false);
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.BAD_REQUEST);
		}
		
		return userServiceTask.updateProfile(userDTO,userId);
	}
	
	@PutMapping(value = "/edit/admin/{userId}")
	public ResponseEntity<RespuestaServicioDTO<Boolean>> updateProfileAdmin(@Valid @RequestBody UsuarioDTO userDTO,BindingResult result,
			@PathVariable Long userId){
		
		if (result.hasErrors()) {
			RespuestaServicioDTO<Boolean> respuestaServicioDTO = new RespuestaServicioDTO<>();
			
			respuestaServicioDTO.setMensajeDTO(
					new MensajeDTO(SeverityEnum.ERROR, 
							messages.getKey(SeverityEnum.ERROR.getKey()),messages.getKey(KEY_ERROR_GENERICO)));
			
			respuestaServicioDTO.setOk(false);
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.BAD_REQUEST);
		}
		
		return userServiceTask.updateProfileAdmin(userDTO,userId);
	}
	

	private boolean validarId(String authorizationHeader, Long id) {
		String token = authorizationHeader.substring(7);
        Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
        Integer idClaim = (Integer) claims.get("id");
        
		return !id.equals(idClaim.longValue());
	}
	
	@PostMapping("/upload")
	public ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> uploadImg(@RequestParam("archivo") MultipartFile archivo, 
			@RequestParam("id") Long id){
		
		if (archivo.isEmpty() || ObjectUtils.isEmpty(id)) {
			RespuestaServicioDTO<UsuarioDTO> respuestaServicioDTO = new RespuestaServicioDTO<>();
			
			respuestaServicioDTO.setMensajeDTO(
					new MensajeDTO(SeverityEnum.ERROR, 
							messages.getKey(SeverityEnum.ERROR.getKey()),messages.getKey(KEY_ERROR_GENERICO)));
			
			respuestaServicioDTO.setOk(false);
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.BAD_REQUEST);
		}
		
		return userServiceTask.cargarImg(archivo,id);
	}
	
	@GetMapping("/recurso/usuario/{nombreRecurso:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreRecurso){
		
		Resource recurso = userServiceTask.obtenerImg(nombreRecurso);
		
		HttpHeaders cabecera= new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + recurso.getFilename() + "\"");
		return new ResponseEntity<>(recurso,cabecera, HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<RespuestaServicioDTO<Page<UsuarioDTO>>> pageUser(@RequestParam Map<String, String> parametros){
		
		if(!parametros.containsKey("cantRegPorPagina") || !parametros.containsKey("numPagina") ){
			
			RespuestaServicioDTO<Page<UsuarioDTO>> respuestaServicioDTO = new RespuestaServicioDTO<>();
			respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR,  messages.getKey(SeverityEnum.ERROR.getKey()),messages.getKey(KEY_ERROR_GENERICO)));
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.BAD_REQUEST);
		}
		
		return userServiceTask.getAll(parametros);
	}
	
	@PutMapping("/estado/{userId}/{state}")
	public ResponseEntity<RespuestaServicioDTO<String>> cambiarEstado(@PathVariable Long userId,@PathVariable Boolean state){
		
		return userServiceTask.stateChange(userId,state);
	}
}
