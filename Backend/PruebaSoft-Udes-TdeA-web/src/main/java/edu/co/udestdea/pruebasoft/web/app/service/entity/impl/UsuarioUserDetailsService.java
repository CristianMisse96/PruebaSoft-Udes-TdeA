package edu.co.udestdea.pruebasoft.web.app.service.entity.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.co.udestdea.pruebasoft.web.app.models.entities.Usuario;
import edu.co.udestdea.pruebasoft.web.app.repositories.UsuarioRepository;

/**
 * <p>
 * Titulo: Proyecto PruebaSoft
 * </p>
 * <p>
 * Descripción: Implementación de la clase userDetailsService spring security para validación de credenciales en login
 * </p>
 *
 * @author Cristian Misse
 **/

@Service
public class UsuarioUserDetailsService implements UserDetailsService {
	
	private UsuarioRepository usuarioRepository;
	
	public UsuarioUserDetailsService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository=usuarioRepository;
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		
		Optional<Usuario> userOptional= usuarioRepository.findByUsernameOrCorreo(username);
		
		if(userOptional.isEmpty()) {
			throw new IllegalArgumentException(String.format("Usuario %s no esta registrado", username));
		}
		
		Usuario usuario= userOptional.orElseThrow();
		
		List<GrantedAuthority> authorities= usuario.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(String.valueOf(role.getRol())))
				.collect(Collectors.toList());
		
		
		return new User(usuario.getUsername(), usuario.getPassword(),  authorities);
	}

}
