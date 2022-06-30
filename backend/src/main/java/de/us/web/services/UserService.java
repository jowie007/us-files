package de.us.web.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import de.us.web.converters.UserMapper;
import de.us.web.domain.User;
import de.us.web.dto.UserDto;
import de.us.web.payload.response.UserResponse;
import de.us.web.repositories.UserRepository;
import de.us.web.storage.StorageFileNotFoundException;
import de.us.web.storage.StorageService;

@Service
public class UserService {

	private UserRepository userRepository;
	private UserMapper userMapper;
	private StorageService storageService;

	@Autowired
	public UserService(UserRepository userRepository, UserMapper userMapper, StorageService storageService) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.storageService = storageService;
	}

	public boolean existUserByName(String name) {
		return userRepository.existsByName(name);
	}

	public UserDto getUserDtoByName(String name) {
		Optional<User> user = userRepository.findByName(name);
		if (user != null && !user.isEmpty() && user.get() != null) {
			User userEntity = userRepository.findByName(name).get();
			return userMapper.userToDto(userEntity);
		}
		throw new NullPointerException("User " + name + " not found.");
	}

	public UserResponse getUserResponseByName(String name) {
		Optional<User> user = userRepository.findByName(name);
		if (user != null && !user.isEmpty() && user.get() != null) {
			User userEntity = userRepository.findByName(name).get();
			return userMapper.userToResponse(userEntity);
		}
		throw new NullPointerException("User " + name + " not found.");
	}

	public User getUserByName(String name) {
		Optional<User> user = userRepository.findByName(name);
		if (user != null && !user.isEmpty() && user.get() != null) {
			return userRepository.findByName(name).get();
		}
		throw new NullPointerException("User mit dem Namen " + name + " existiert nicht.");
	}

	public String getUserSaltByName(String name) {
		return getUserDtoByName(name) == null ? null : getUserDtoByName(name).getSalt();
	}

	public UserDto createUser(UserDto userDto) {
		User userEntity = userMapper.dtoToUser(userDto);
		return userMapper.userToDto(userRepository.save(userEntity));
	}

	public Resource loadProfileFileByName(String name) {
		storageService.loadAll();
		Resource file = null;
		for (String fileExtension : new String[] { "jpg", "jpeg", "png" }) {
			try {
				file = storageService.loadAsResource(name + "." + fileExtension);
			} catch (StorageFileNotFoundException e) {

			}
		}
		return file;
	}
}
