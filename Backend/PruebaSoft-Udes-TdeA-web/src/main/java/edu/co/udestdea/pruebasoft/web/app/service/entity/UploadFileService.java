package edu.co.udestdea.pruebasoft.web.app.service.entity;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
	
	public Resource cargar(String nombreFoto, String directorio) throws MalformedURLException;
	public String copiar(MultipartFile archivo,  String directorio) throws IOException;
	public boolean eliminar (String nombreFoto,  String directorio) throws IOException;
}
