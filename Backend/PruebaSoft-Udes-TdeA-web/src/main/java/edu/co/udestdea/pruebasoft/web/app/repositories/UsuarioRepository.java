package edu.co.udestdea.pruebasoft.web.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.co.udestdea.pruebasoft.web.app.models.entities.Usuario;

/**
 * <p>
 * Titulo: Proyecto PruebaSoft
 * </p>
 * <p>
 * Descripción: Repositorio de la entidad Usuario
 * </p>
 *
 * @author Cristian Misse
 **/

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	boolean existsByEmailOrUsername(String correo,String username);
	boolean existsByEmail(String correo);
	boolean existsByUsername(String username);
	
	@Query("SELECT u FROM Usuario u WHERE u.username = :username OR u.email = :username")
	Optional<Usuario> findByUsernameOrCorreo(String username);
	
}
