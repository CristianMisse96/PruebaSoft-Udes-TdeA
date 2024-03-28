package edu.co.udestdea.pruebasoft.web.app.service.task.impl;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.co.udestdea.pruebasoft.web.app.exception.PruebaSoftException;
import edu.co.udestdea.pruebasoft.web.app.models.dto.MensajeDTO;
import edu.co.udestdea.pruebasoft.web.app.models.dto.RespuestaServicioDTO;
import edu.co.udestdea.pruebasoft.web.app.models.dto.UsuarioDTO;
import edu.co.udestdea.pruebasoft.web.app.models.enums.SeverityEnum;
import edu.co.udestdea.pruebasoft.web.app.service.entity.UsuarioService;
import edu.co.udestdea.pruebasoft.web.app.service.task.UsuarioServiceTask;
import edu.co.udestdea.pruebasoft.web.app.util.ExcepcionUtil;
import edu.co.udestdea.pruebasoft.web.app.util.Messages;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * Titulo: Proyecto Pruebasoft
 * </p>
 * <p>
 * Descripción: Implementación de la interfaz UsuarioServiceTask
 * </p>
 *
 * @author Cristian Misse
 *
 **/

@Service
@Log4j2
public class UsuarioServiceTaskImpl implements UsuarioServiceTask {
	
	private static final String USUARIO_EXITO_EDITAR_PERFIL = "usuario.exito.editar.perfil";
	private static final String USUARIO_EXITO_CARGAR_IMAGEN = "usuario.exito.cargar.imagen";
	private static final String SUCCESS_DETALLE = "success.detalle";
	private static final String ERROR_INESPERADO_USUARIO = "error inesperado usuario -> ";
	private static final String KEY_SUCCESS= "success";
	private static final String KEY_SUCCESS_REG_EXITOSO= "usuario.registro.exitoso";
	private static final String KEY_ERROR_LABEL 	= "error";
	private static final String KEY_ERROR_GENERICO	= "error.generico";
	
	private UsuarioService userService;
	private Messages messages;
	

	public UsuarioServiceTaskImpl(UsuarioService userService, Messages messages) {
		this.userService= userService;
		this.messages=messages;
	}
	
	@Override
	public ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> registarUsuario(UsuarioDTO userDTO) {
		
		RespuestaServicioDTO<UsuarioDTO> respuestaServicioUserDTO = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try{
			
			if(!userDTO.isAdmin() && !userDTO.isStudent() && !userDTO.isTeacher()) {
				userDTO.setStudent(true);
			}
			userDTO.setEnabled(Boolean.TRUE);
			UsuarioDTO respuesta= userService.registrarUsuario(userDTO);
			
			respuestaServicioUserDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(KEY_SUCCESS_REG_EXITOSO).replace("{1}", userDTO.getNombre())));
			respuestaServicioUserDTO.setOk(true);
			respuestaServicioUserDTO.setNegocio(respuesta); 
			
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, PruebaSoftException.class)) {
				respuestaServicioUserDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicioUserDTO, HttpStatus.CONFLICT);
			}else {
			    log.error(ERROR_INESPERADO_USUARIO + e.getMessage());
				respuestaServicioUserDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicioUserDTO, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
		
		return new ResponseEntity<>(respuestaServicioUserDTO, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<Boolean>> existeCorreo(String email) {
		
		RespuestaServicioDTO<Boolean> respuestaServicioDTO = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		Boolean validacionCorreo= false;
		try {
			validacionCorreo = userService.validarCorreo(email);
			respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicioDTO.setOk(validacionCorreo);
			respuestaServicioDTO.setNegocio(validacionCorreo);
		}catch(PruebaSoftException e) {
			log.error(ERROR_INESPERADO_USUARIO + e.getMessage());
			respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
			respuestaServicioDTO.setNegocio(validacionCorreo);
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<Boolean>> existeUsername(String username) {
		RespuestaServicioDTO<Boolean> respuestaServicioDTO = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		Boolean validacionUsername= false;
		try {
			validacionUsername = userService.validarUsername(username);
			respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicioDTO.setOk(validacionUsername);
			respuestaServicioDTO.setNegocio(validacionUsername);
		}catch(Exception e) {
			log.error(ERROR_INESPERADO_USUARIO + e.getMessage());
			respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
			respuestaServicioDTO.setNegocio(validacionUsername);
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> cargarImg(MultipartFile archivo, Long id) {
		
		RespuestaServicioDTO<UsuarioDTO> respuestaServicioUserDTO = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			
			UsuarioDTO respuesta= userService.cargarImagen(archivo,id);
			respuestaServicioUserDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(USUARIO_EXITO_CARGAR_IMAGEN)));
			respuestaServicioUserDTO.setOk(true);
			respuestaServicioUserDTO.setNegocio(respuesta);
		} catch (Exception e) {
			if (ExcepcionUtil.contains(e, PruebaSoftException.class)) {
				respuestaServicioUserDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicioUserDTO, HttpStatus.CONFLICT);
			}else {
			    log.error(ERROR_INESPERADO_USUARIO + e.getMessage());
				respuestaServicioUserDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicioUserDTO, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicioUserDTO, HttpStatus.CREATED);
	}

	@Override
	public Resource obtenerImg(String nombreRecurso) {
		
		Resource recurso=null;
		
		try {
			recurso= userService.obtenerImg(nombreRecurso);
			
			 if (!recurso.exists() && !recurso.isReadable()) {
				 Path rutaArchivo = Paths.get("uploads/usuarios","no-usuario.png").toAbsolutePath();

		         recurso = new UrlResource(rutaArchivo.toUri()); 
		    }
		} catch (Exception e) {
			log.error(ERROR_INESPERADO_USUARIO + e.getMessage());
			log.error(e);
		}
		
		return recurso;
		
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> findUser(Long userId) {
		
		RespuestaServicioDTO<UsuarioDTO> respuestaServicioUserDTO = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			
			UsuarioDTO respuesta= userService.buscarUsuario(userId);
			respuestaServicioUserDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicioUserDTO.setOk(true);
			respuestaServicioUserDTO.setNegocio(respuesta);
		} catch (Exception e) {
			if (ExcepcionUtil.contains(e, PruebaSoftException.class)) {
				respuestaServicioUserDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicioUserDTO, HttpStatus.CONFLICT);
			}else {
			    log.error(ERROR_INESPERADO_USUARIO + e.getMessage());
				respuestaServicioUserDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicioUserDTO, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicioUserDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<Boolean>> updateProfile(UsuarioDTO userDTO, Long userId) {
		RespuestaServicioDTO<Boolean> respuestaServicioDTO = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			userService.updateProfile(userDTO,userId);
			respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(USUARIO_EXITO_EDITAR_PERFIL)));
			respuestaServicioDTO.setOk(true);
			respuestaServicioDTO.setNegocio(true);
		}catch(Exception e) {
			if (ExcepcionUtil.contains(e, PruebaSoftException.class)) {
				respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.CONFLICT);
			}else {
			    log.error(ERROR_INESPERADO_USUARIO + e.getMessage());
			    respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<Page<UsuarioDTO>>> getAll(Map<String, String> parametros) {
		
		RespuestaServicioDTO<Page<UsuarioDTO>> respuestaServicioDTO = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			Page<UsuarioDTO> userPag= userService.getPageData(parametros);
			respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicioDTO.setOk(true);
			respuestaServicioDTO.setNegocio(userPag);
		}catch(Exception e) {
			if (ExcepcionUtil.contains(e, PruebaSoftException.class)) {
				respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.CONFLICT);
			}else {
			    log.error(ERROR_INESPERADO_USUARIO + e.getMessage());
			    respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<String>> stateChange(Long userId, Boolean estado) {
		RespuestaServicioDTO<String> respuestaServicioDTO = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			userService.stateChange(userId, estado);
			respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(USUARIO_EXITO_EDITAR_PERFIL)));
			respuestaServicioDTO.setOk(true);
			respuestaServicioDTO.setNegocio(USUARIO_EXITO_EDITAR_PERFIL);
		}catch(Exception e) {
			if (ExcepcionUtil.contains(e, PruebaSoftException.class)) {
				respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.CONFLICT);
			}else {
			    log.error(ERROR_INESPERADO_USUARIO + e.getMessage());
			    respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<Boolean>> updateProfileAdmin(UsuarioDTO userDTO, Long userId) {
		
		RespuestaServicioDTO<Boolean> respuestaServicioDTO = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			userService.updateProfileAdmin(userDTO,userId);
			respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(USUARIO_EXITO_EDITAR_PERFIL)));
			respuestaServicioDTO.setOk(true);
			respuestaServicioDTO.setNegocio(true);
		}catch(Exception e) {
			if (ExcepcionUtil.contains(e, PruebaSoftException.class)) {
				respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.CONFLICT);
			}else {
			    log.error(ERROR_INESPERADO_USUARIO + e.getMessage());
			    respuestaServicioDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.OK);
	}

}
