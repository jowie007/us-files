package de.us.web.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

	private Path rootLocation;
	private final StorageProperties properties;

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.properties = properties;
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public void setSubFolder(String subfoldername) {
		this.rootLocation = Paths.get(properties.getLocation() + "/" + subfoldername);
	}

	@Override
	public void store(MultipartFile file, String newName) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file.");
			}
			Path destinationFile = this.rootLocation.resolve(Paths.get(getNewFileName(file, newName))).normalize()
					.toAbsolutePath();
			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				// This is a security check
				throw new StorageException("Cannot store file outside current directory.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			// Files.walk(this.rootLocation, 1).filter(path ->
			// !path.equals(this.rootLocation))
			// .map(this.rootLocation::relativize).forEach(System.out::println);

			return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
					.map(this.rootLocation::relativize);
		} catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Could not read file: " + filename);

			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void delete(String filename) {
		// https://www.baeldung.com/java-collection-stream-foreach
		Consumer<Path> removeElement = s -> {
			if (s.toString().startsWith(filename + ".")) {
				try {
					FileSystemUtils.deleteRecursively(load(s.toString()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		loadAll().forEach(removeElement);
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

	@Override
	public String getNewFileName(MultipartFile multipartFile, String newName) {
		String ret = "";
		String originalName = multipartFile.getOriginalFilename();
		try {
			// https://stackoverflow.com/questions/14833008/java-string-split-with-dot/14833048
			String[] originalNameParts = originalName.split("\\.");
			ret = newName + "." + originalNameParts[originalNameParts.length - 1];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new StorageException("Could not save file, invalid name", e);
		}
		return ret;
	}
}
