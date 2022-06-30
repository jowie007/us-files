package de.us.web.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.us.web.domain.UserSongRelation;
import de.us.web.dto.UserSongRelationDto;

@Component
public class UserSongRelationMapper {

	private UserMapper userMapper;
	private SongMapper songMapper;

	@Autowired
	public UserSongRelationMapper(UserMapper userMapper, SongMapper songMapper) {
		this.userMapper = userMapper;
		this.songMapper = songMapper;
	}

	public UserSongRelationDto userSongRelationToDto(UserSongRelation userSongRelation) {
		return userSongRelation == null ? null
				: new UserSongRelationDto(userMapper.userToDto(userSongRelation.getUser()),
						songMapper.songToDto(userSongRelation.getSong()), userSongRelation.getRating(),
						userSongRelation.getRatingDate(), userSongRelation.getDownloadDate(),
						userSongRelation.isFavorite(),
						userSongRelation.getFavoriteDate());
	}

	public UserSongRelation dtoToUserSongRelation(UserSongRelationDto userSongRelationDto) {
		return userSongRelationDto == null ? null
				: new UserSongRelation(userMapper.dtoToUser(userSongRelationDto.getUserDto()),
						songMapper.dtoToSong(userSongRelationDto.getSongDto()), userSongRelationDto.getRating(),
						userSongRelationDto.getRatingDate(), userSongRelationDto.getDownloadDate(),
						userSongRelationDto.isFavorite(),
						userSongRelationDto.getFavoriteDate());
	}
}
