package edu.co.udestdea.pruebasoft.web.app.auth.filter;

import static edu.co.udestdea.pruebasoft.web.app.util.SpringSecurityConstantes.CONTENT_TYPE;
import static edu.co.udestdea.pruebasoft.web.app.util.SpringSecurityConstantes.HEADER_AUTHORIZATION;
import static edu.co.udestdea.pruebasoft.web.app.util.SpringSecurityConstantes.MESSAGE;
import static edu.co.udestdea.pruebasoft.web.app.util.SpringSecurityConstantes.PREFIX_TOKEN;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.co.udestdea.pruebasoft.web.app.auth.service.JWTService;
import edu.co.udestdea.pruebasoft.web.app.models.entities.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private AuthenticationManager authenticationManager;
	private JWTService jWTService;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jWTService) {
		this.authenticationManager = authenticationManager;
		this.jWTService=jWTService;
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login","POST"));
	}


	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		Usuario user= new Usuario();
		
		try {
			 user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class) ;
			 
			 if (user.getUsername() == null || user.getUsername().isEmpty() ||
					 user.getPassword() == null ||user.getPassword().isEmpty()) {
		          
				 Map<String, String> body= new HashMap<>();
				 
					body.put(MESSAGE, "Empty");
					body.put("error", "El nombre de usuario y la contraseña son obligatorios");
					response.getWriter().write(new ObjectMapper().writeValueAsString(body));
					response.setContentType(CONTENT_TYPE);
					response.setStatus(HttpStatus.BAD_REQUEST.value());
					return null;
		      }
			 	
				logger.info("Username desde request parameter (raw): " +user.getUsername());
				logger.info("Password desde request parameter (raw): " +user.getPassword());
				
				
		} catch (IOException e) {
			e.printStackTrace();
		}  
		
		UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(
				user.getUsername(), user.getPassword());
		
		
		return authenticationManager.authenticate(authToken);
		
	}


	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		String token = jWTService.create(authResult);
		
		response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);
		Map<String, String> body= new HashMap<>();
		body.put("token", token);
		body.put("username", authResult.getName());
		body.put(MESSAGE, String.format("Bienvenido %s, ha iniciado sesión con éxito!", authResult.getName()));
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setContentType(CONTENT_TYPE);
		response.setStatus(HttpStatus.OK.value());
	}


	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		Map<String, String> body= new HashMap<>();
		body.put(MESSAGE,"Error username o password incorrectos");
		body.put("error", failed.getMessage());
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setContentType(CONTENT_TYPE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}
	
	
	
	
	
	
	
	
}
