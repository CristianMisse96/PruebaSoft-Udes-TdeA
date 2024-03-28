package edu.co.udestdea.pruebasoft.web.app.service.entity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import edu.co.udestdea.pruebasoft.web.app.exception.PruebaSoftException;
import edu.co.udestdea.pruebasoft.web.app.models.dto.UsuarioDTO;
import jakarta.validation.Valid;

public interface UsuarioService {
	
	/**
	 * Método de la capa de negocio encargado de persistir nuevos usuarios.
	 * @param userDTO usuario que contiene la información para persistir
	 * @return usuario persistido en la BD.
	 * @throws PruebaSoftException 
	 */
	UsuarioDTO registrarUsuario(UsuarioDTO userDTO) throws PruebaSoftException;
	
	Boolean validarCorreo(String email) throws PruebaSoftException;

	Boolean validarUsername(String username) throws PruebaSoftException;

	UsuarioDTO cargarImagen(MultipartFile archivo, Long id)throws PruebaSoftException, IOException;

	Resource obtenerImg(String nombrefoto) throws MalformedURLException;

	UsuarioDTO buscarUsuario(Long userId)throws PruebaSoftException;

	void updateProfile(UsuarioDTO userDTO, Long userId) throws PruebaSoftException;

	Page<UsuarioDTO> getPageData(Map<String, String> parametros);

	void stateChange(Long userId, Boolean estado) throws PruebaSoftException;

	void updateProfileAdmin(UsuarioDTO userDTO, Long userId) throws PruebaSoftException;

}
