package edu.co.udestdea.pruebasoft.web.app.service.entity.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.co.udestdea.pruebasoft.web.app.exception.PruebaSoftException;
import edu.co.udestdea.pruebasoft.web.app.models.dto.RoleDTO;
import edu.co.udestdea.pruebasoft.web.app.models.dto.UsuarioDTO;
import edu.co.udestdea.pruebasoft.web.app.models.entities.Role;
import edu.co.udestdea.pruebasoft.web.app.models.entities.Usuario;
import edu.co.udestdea.pruebasoft.web.app.models.enums.RolEnum;
import edu.co.udestdea.pruebasoft.web.app.repositories.RoleRepository;
import edu.co.udestdea.pruebasoft.web.app.repositories.UsuarioRepository;
import edu.co.udestdea.pruebasoft.web.app.service.entity.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{
	
	private UsuarioRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private ModelMapper modelMapper;
	private RoleRepository roleRepository;
	
	public UsuarioServiceImpl(UsuarioRepository userRepository,PasswordEncoder passwordEncoder,ModelMapper modelMapper,
			                 RoleRepository roleRepository) {
		this.userRepository=userRepository;
		this.passwordEncoder=passwordEncoder;
		this.modelMapper=modelMapper;
		this.roleRepository= roleRepository;
	}

	@Override
	@Transactional
	public UsuarioDTO registrarUsuario(UsuarioDTO userDTO) throws PruebaSoftException{
		if(userRepository.existsByEmailOrUsername(userDTO.getEmail(), userDTO.getUsername())) {
				throw new PruebaSoftException("errorrr");
		}
		
		Usuario user= modelMapper.map(userDTO, Usuario.class);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		if(userDTO.isStudent()) {
			userDTO.setTeacher(false);
			userDTO.setAdmin(false);
			Optional<Role> rolStudent= roleRepository.findByRol(RolEnum.ROLE_STUDENT);
			rolStudent.ifPresentOrElse(r-> user.getRoles().add(r),()-> user.getRoles().add(new Role(RolEnum.ROLE_STUDENT)));
		}
		
		if(userDTO.isTeacher()) {
			Optional<Role> rolStudent= roleRepository.findByRol(RolEnum.ROLE_TEACHER);
			rolStudent.ifPresentOrElse(r-> user.getRoles().add(r),()-> user.getRoles().add(new Role(RolEnum.ROLE_TEACHER)));
		}
		
		if(userDTO.isAdmin()) {
			Optional<Role> rolStudent= roleRepository.findByRol(RolEnum.ROLE_ADMIN);
			rolStudent.ifPresentOrElse(r-> user.getRoles().add(r),()-> user.getRoles().add(new Role(RolEnum.ROLE_ADMIN)));
		}
		
		return modelMapper.map(userRepository.save(user), UsuarioDTO.class);
	}

	@Override
	public Boolean validarCorreo(String email) throws Exception {
		
		return userRepository.existsByEmail(email);
	}
	

}
