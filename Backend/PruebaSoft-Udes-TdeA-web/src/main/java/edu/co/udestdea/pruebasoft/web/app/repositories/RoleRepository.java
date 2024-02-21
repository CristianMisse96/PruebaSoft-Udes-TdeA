package edu.co.udestdea.pruebasoft.web.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.co.udestdea.pruebasoft.web.app.models.dto.RoleDTO;
import edu.co.udestdea.pruebasoft.web.app.models.entities.Role;
import edu.co.udestdea.pruebasoft.web.app.models.enums.RolEnum;

/**
 * <p>
 * Titulo: Proyecto PruebaSoft
 * </p>
 * <p>
 * Descripción: Repositorio de la entidad Role
 * </p>
 *
 * @author Cristian Misse
 **/

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
	
	Optional<Role> findByRol(RolEnum rol);

}
