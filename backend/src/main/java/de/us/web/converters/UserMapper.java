package de.us.web.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.us.web.domain.User;
import de.us.web.dto.UserDto;
import de.us.web.payload.response.UserResponse;
import de.us.web.repositories.SongRepository;
import de.us.web.services.SongService;

@Component
public class UserMapper {

	private SongRepository songRepository;
	private SongService songService;

	public UserDto userToDto(User user) {
		return user == null ? null
				: new UserDto(user.getName(), user.getEmail(), user.getPassword(), user.getSalt(),
						user.getRegistrationDate(), user.getRole());
	}

	public UserResponse userToResponse(User user) {
		return user == null ? null
				: new UserResponse(user.getName(), user.getRegistrationDate(), user.getRole(),
						songRepository.findAllByCreator(user).size(), songService.getEditorSongCount(user));
	}

	public User dtoToUser(UserDto userDto) {
		return userDto == null ? null
				: new User(userDto.getName(), userDto.getEmail(), userDto.getPassword(), userDto.getSalt(),
						userDto.getRegistrationDate(), userDto.getRole());
	}

	@Autowired
	public void setSongRepository(SongRepository songRepository) {
		this.songRepository = songRepository;
	}

	@Autowired
	public void setSongService(SongService songService) {
		this.songService = songService;
	}
}
