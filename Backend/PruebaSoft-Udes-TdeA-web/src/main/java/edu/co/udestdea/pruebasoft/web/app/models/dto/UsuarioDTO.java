package edu.co.udestdea.pruebasoft.web.app.models.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO implements Serializable{

	private Long id;
	
	@NotBlank
	@Size(max = 30)
	private String username;
	
	@NotBlank
	@Size(max = 45)
	private String email;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@NotBlank
	@Size(max = 80)
	private String password;
	
	@NotBlank
	@Size(max = 45)
	private String nombre;
	
	@NotBlank
	@Size(max = 45)
	private String apellido;
	
	private List<RoleDTO> roles;
	
	private String foto;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private boolean admin;
	@JsonProperty(access = Access.WRITE_ONLY)
	private boolean student;
	@JsonProperty(access = Access.WRITE_ONLY)
	private boolean teacher;
	
	public UsuarioDTO() {
		this.roles=new ArrayList<>();
	}
	
	private static final long serialVersionUID = 1L;

}
