package edu.co.udestdea.pruebasoft.web.app.service.entity.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.co.udestdea.pruebasoft.web.app.exception.PruebaSoftException;
import edu.co.udestdea.pruebasoft.web.app.models.dto.UsuarioDTO;
import edu.co.udestdea.pruebasoft.web.app.models.entities.Role;
import edu.co.udestdea.pruebasoft.web.app.models.entities.Usuario;
import edu.co.udestdea.pruebasoft.web.app.models.enums.RolEnum;
import edu.co.udestdea.pruebasoft.web.app.repositories.RoleRepository;
import edu.co.udestdea.pruebasoft.web.app.repositories.UsuarioRepository;
import edu.co.udestdea.pruebasoft.web.app.service.entity.UploadFileService;
import edu.co.udestdea.pruebasoft.web.app.service.entity.UsuarioService;
import jakarta.validation.Valid;

@Service
public class UsuarioServiceImpl implements UsuarioService{
	
	private static final String ERROR_USUARIO_EDITAR_ADMIN_INACTIVO = "error.usuario.editar.admin.inactivo";
	private static final String ERROR_USUARIO_NO_EXISTE_CARGAR_IMAGEN = "error.usuario.no.existe.cargar.imagen";
	private static final String ERROR_USUARIO_REGISTRAR_EXISTE = "error.usuario.registrar.existe";
	
	private UsuarioRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private ModelMapper modelMapper;
	private RoleRepository roleRepository;
	private UploadFileService uploadFileService;
	
	public UsuarioServiceImpl(UsuarioRepository userRepository,PasswordEncoder passwordEncoder,ModelMapper modelMapper,
			                 RoleRepository roleRepository, UploadFileService uploadFileService) {
		this.userRepository=userRepository;
		this.passwordEncoder=passwordEncoder;
		this.modelMapper=modelMapper;
		this.roleRepository= roleRepository;
		this.uploadFileService= uploadFileService;
	}

	@Override
	@Transactional
	public UsuarioDTO registrarUsuario(UsuarioDTO userDTO) throws PruebaSoftException{
		if(userRepository.existsByEmailOrUsername(userDTO.getEmail(), userDTO.getUsername())) {
				throw new PruebaSoftException(ERROR_USUARIO_REGISTRAR_EXISTE);
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
	@Transactional(readOnly = true)
	public Boolean validarCorreo(String email) throws PruebaSoftException {
		
		return userRepository.existsByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean validarUsername(String username) throws PruebaSoftException {
		return userRepository.existsByUsername(username);
	}

	@Override
	@Transactional
	public UsuarioDTO cargarImagen(MultipartFile archivo, Long id) throws PruebaSoftException, IOException {
		
		Optional<Usuario> usuarioActual= userRepository.findById(id);
		
		Usuario user= usuarioActual.orElseThrow(()-> new PruebaSoftException(ERROR_USUARIO_NO_EXISTE_CARGAR_IMAGEN));
		//el usuario existe, se procede a actualizar imagen
		String nombreArchivo=uploadFileService.copiar(archivo, "uploads/usuarios/img");
		//eliminar si contiene una foto anterior
		String nombreFotoAnterior= user.getFoto();
		uploadFileService.eliminar(nombreFotoAnterior,"uploads/usuarios/img");
		//actualizar la imagen al usuario
		user.setFoto(nombreArchivo);
		
		return  modelMapper.map(userRepository.save(user), UsuarioDTO.class);
	}

	@Override
	public Resource obtenerImg(String nombreRecurso) throws MalformedURLException {
		return uploadFileService.cargar(nombreRecurso, "uploads/usuarios/img");
	}

	@Override
	@Transactional(readOnly = true)
	public UsuarioDTO buscarUsuario(Long userId) throws PruebaSoftException {
		
		Usuario user = userRepository.findById(userId)
	            .orElseThrow(() -> new PruebaSoftException(ERROR_USUARIO_NO_EXISTE_CARGAR_IMAGEN));
		
		return modelMapper.map(user, UsuarioDTO.class);
	}

	@Override
	@Transactional
	@Modifying
	public void updateProfile(UsuarioDTO userDTO, Long userId) throws PruebaSoftException {
		Usuario user = userRepository.findById(userId)
	            .orElseThrow(() -> new PruebaSoftException(ERROR_USUARIO_NO_EXISTE_CARGAR_IMAGEN));
		
		user.setEmail(userDTO.getEmail());
		
		userRepository.save(user);
		
	}
	
	@Override
	@Transactional
	public void stateChange(Long userId, Boolean estado) throws PruebaSoftException {
		
		Usuario user = userRepository.findById(userId)
	            .orElseThrow(() -> new PruebaSoftException(ERROR_USUARIO_NO_EXISTE_CARGAR_IMAGEN));
		
		user.setEnabled(estado);
		
		userRepository.save(user);
		
	}
	

	@Override
	@Transactional(readOnly = true)
	public Page<UsuarioDTO> getPageData(Map<String, String> parametros) {
		
		int pageNumber = Integer.parseInt(parametros.get("numPagina"));
	    int pageSize = Integer.parseInt(parametros.get("cantRegPorPagina"));
	    String sortField= (!parametros.containsKey("sortField"))?"nombre": parametros.get("sortField");
	    String sortOrder= (!parametros.containsKey("sortOrder"))?"asc": Integer.parseInt(parametros.get("sortOrder"))>0?"asc":"desc";
	    String palabraClave= parametros.getOrDefault("filtros", null);
	    
	    Pageable pageable = PageRequest.of(pageNumber, pageSize);

	    Page<Usuario> usuarioPage = userRepository.findAll(pageable);
	    
	    List<UsuarioDTO> usuarioDTOList = usuarioPage.getContent().stream()
	    		.filter(usuario -> palabraClave == null || contienePalabraClave(usuario, palabraClave))
                .map(this::mapUsuarioToDTO)
                .sorted(getComparator(sortField, sortOrder)).toList();

	    return new PageImpl<>(usuarioDTOList, pageable, usuarioPage.getTotalElements());
	}


	private UsuarioDTO mapUsuarioToDTO(Usuario usuario) {
		return modelMapper.map(usuario, UsuarioDTO.class);
	}
	
	private Comparator<UsuarioDTO> getComparator(String sortField, String sortOrder) {
        Comparator<UsuarioDTO> comparator = null;
        switch (sortField) {
            case "nombre":
                comparator = Comparator.comparing(UsuarioDTO::getNombre);
                break;
            case "apellido":
                comparator = Comparator.comparing(UsuarioDTO::getApellido);
                break;
            case "username":
                comparator = Comparator.comparing(UsuarioDTO::getUsername);
                break;
            case "enabled":
                comparator = Comparator.comparing(UsuarioDTO::getEnabled);
                break;
            default:
            	comparator = Comparator.comparing(UsuarioDTO::getNombre);
        }
        if (sortOrder.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }
        return comparator;
    }
	
	private boolean contienePalabraClave(Usuario usuario, String palabraClave) {
		return usuario.getNombre().toLowerCase().contains(palabraClave.toLowerCase())
                || usuario.getApellido().toLowerCase().contains(palabraClave.toLowerCase())
                || usuario.getUsername().toLowerCase().contains(palabraClave.toLowerCase());
	}

	@Override
	@Transactional
	@Modifying
	public void updateProfileAdmin(UsuarioDTO userDTO, Long userId) throws PruebaSoftException {
		
		Usuario user = userRepository.findById(userId)
	            .orElseThrow(() -> new PruebaSoftException(ERROR_USUARIO_NO_EXISTE_CARGAR_IMAGEN));
		
		validacionesEditar(user,userDTO);
		
		validarRoles(user,userDTO);
		
		userRepository.save(user);
		
	}

	private void validarRoles(Usuario user, UsuarioDTO userDTO) {
		actualizarRol(user, userDTO.isAdmin(), RolEnum.ROLE_ADMIN);
	    actualizarRol(user, userDTO.isTeacher(), RolEnum.ROLE_TEACHER);
	    actualizarRol(user, userDTO.isStudent(), RolEnum.ROLE_STUDENT);
		
	}
	
	private void actualizarRol(Usuario user, boolean tieneRol, RolEnum rolEnum) {
	    // Obtener el rol correspondiente del repositorio
	    Optional<Role> rolOptional = roleRepository.findByRol(rolEnum);
	    
	    // Si el rol existe en el repositorio
	    rolOptional.ifPresent(rol -> {
	        if (tieneRol) {
	            // Si el usuario tiene la bandera activa y no tiene el rol, agregarlo
	            if (!user.getRoles().contains(rol)) {
	                user.getRoles().add(rol);
	            }
	        } else {
	            // Si el usuario no tiene la bandera activa y tiene el rol, quitarlo
	            user.getRoles().remove(rol);
	        }
	    });
	}

	
	private void validacionesEditar(Usuario user, UsuarioDTO userDTO) throws PruebaSoftException {
	    if (!Objects.equals(user.getUsername(), userDTO.getUsername()) &&
	            userRepository.existsByUsername(userDTO.getUsername())) {
	        throw new PruebaSoftException(ERROR_USUARIO_REGISTRAR_EXISTE);
	    }

	    if (!Objects.equals(user.getEmail(), userDTO.getEmail()) &&
	            userRepository.existsByEmail(userDTO.getEmail())) {
	        throw new PruebaSoftException(ERROR_USUARIO_REGISTRAR_EXISTE);
	    }

	    if (Boolean.FALSE.equals(user.getEnabled())) {
	        throw new PruebaSoftException(ERROR_USUARIO_EDITAR_ADMIN_INACTIVO);
	    }

	    // Asignar valores después de las validaciones
	    user.setNombre(userDTO.getNombre());
	    user.setApellido(userDTO.getApellido());
	    user.setEmail(userDTO.getEmail());
	    user.setUsername(userDTO.getUsername());
	}



}
