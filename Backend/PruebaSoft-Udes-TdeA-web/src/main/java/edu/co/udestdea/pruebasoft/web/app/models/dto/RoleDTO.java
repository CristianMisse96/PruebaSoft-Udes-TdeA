package edu.co.udestdea.pruebasoft.web.app.models.dto;

import java.io.Serializable;

import edu.co.udestdea.pruebasoft.web.app.models.enums.RolEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO implements Serializable{
	
	private Long id;
	private RolEnum rol;
	
	public RoleDTO(RolEnum rol) {
		this.rol=rol;
	}

	private static final long serialVersionUID = 1L;
	
	
}
