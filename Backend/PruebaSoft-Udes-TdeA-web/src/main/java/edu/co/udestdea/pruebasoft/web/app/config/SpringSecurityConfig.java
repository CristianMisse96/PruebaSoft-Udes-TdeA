package edu.co.udestdea.pruebasoft.web.app.config;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import edu.co.udestdea.pruebasoft.web.app.auth.filter.JWTAuthenticationFilter;
import edu.co.udestdea.pruebasoft.web.app.auth.filter.JWTValidationFilter;
import edu.co.udestdea.pruebasoft.web.app.auth.service.JWTService;
import static edu.co.udestdea.pruebasoft.web.app.util.SpringSecurityConstantes.HEADER_AUTHORIZATION;

/**
 * <p>
 * Titulo: Proyecto PruebaSoft
 * </p>
 * <p>
 * Descripción: Clase de configuración de seguridad en la  aplicación.
 * </p>
 *
 * @author Cristian Misse
 *
 **/

@Configuration
public class SpringSecurityConfig {
	
	private AuthenticationConfiguration authenticationConfiguration;
	private JWTService jwtService;
	
	public SpringSecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTService jwtService) {
		this.authenticationConfiguration=authenticationConfiguration;
		this.jwtService=jwtService;
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		return http.authorizeHttpRequests((authz)-> authz
				.requestMatchers("/api/users/register").permitAll()
				.requestMatchers("/api/users/isemail/{email}").permitAll()
				//.requestMatchers(HttpMethod.POST,"/api/users/create").hasRole("ADMIN")
				.anyRequest().authenticated())
				.addFilter(new JWTAuthenticationFilter(authenticationManager(),jwtService))
				.addFilter(new JWTValidationFilter(authenticationManager(),jwtService))
				.csrf(config-> config.disable())
				.cors(cors-> cors.configurationSource(corsConfigurationSource()))
				.sessionManagement(magnament-> magnament.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();
	}
	
	AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOriginPatterns(Arrays.asList("*"));
		config.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(),
											   HttpMethod.POST.name(),
											   HttpMethod.PUT.name(),
											   HttpMethod.DELETE.name()
											   ));
		config.setAllowedHeaders(Arrays.asList(HEADER_AUTHORIZATION, "Content-Type"));
		config.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		
		return source;
	}
	
	@Bean
	FilterRegistrationBean<CorsFilter> corsFilter(){
		
		FilterRegistrationBean<CorsFilter> corsBean= new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
		corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		
		return corsBean;
	}
}
