package edu.co.udestdea.pruebasoft.web.app.service.entity;

import edu.co.udestdea.pruebasoft.web.app.exception.PruebaSoftException;
import edu.co.udestdea.pruebasoft.web.app.models.dto.UsuarioDTO;

public interface UsuarioService {
	
	/**
	 * Método de la capa de negocio encargado de persistir nuevos usuarios.
	 * @param userDTO usuario que contiene la información para persistir
	 * @return usuario persistido en la BD.
	 * @throws PruebaSoftException 
	 */
	UsuarioDTO registrarUsuario(UsuarioDTO userDTO) throws PruebaSoftException;
	

}
