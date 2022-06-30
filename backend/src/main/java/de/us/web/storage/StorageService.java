package de.us.web.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	void store(MultipartFile file, String newName);

	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename);

	void deleteAll();
	
	void delete(String filename);

	void setSubFolder(String subfoldername);
	
	String getNewFileName(MultipartFile multipartFile, String newName);

}
