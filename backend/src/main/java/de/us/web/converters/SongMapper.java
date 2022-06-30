package de.us.web.converters;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.us.web.domain.Song;
import de.us.web.domain.User;
import de.us.web.dto.SongDto;
import de.us.web.dto.UserDto;
import de.us.web.payload.request.SongRequest;
import de.us.web.payload.response.SongResponse;
import de.us.web.repositories.SongRepository;
import de.us.web.repositories.UserRepository;

@Component
public class SongMapper {
	private UserMapper userMapper;
	private SongRepository songRepository;
	private UserRepository userRepository;

	@Autowired
	public SongMapper(UserMapper userMapper, SongRepository songRepository, UserRepository userRepository) {
		this.userMapper = userMapper;
		this.songRepository = songRepository;
		this.userRepository = userRepository;
	}

	public Song getSongToUpdate(SongRequest songRequest) {
		Song song = songRepository.findById(songRequest.getId()).get();
		song.setArtist(songRequest.getArtist());
		song.setTitle(songRequest.getTitle());
		song.setVersion(songRequest.getVersion());
		song.setGenre(songRequest.getGenre());
		song.setLanguage(song.getLanguage());
		song.setLink(songRequest.getLink());
		song.setPercentage(songRequest.getPercentage());
		return song;
	}

	public SongDto songRequestToSongDto(SongRequest songRequest) {

		if (songRequest == null) {
			return null;
		}

		Song initialSong = null;
		if (songRequest.getId() != null) {
			initialSong = songRepository.findById(songRequest.getId()).get();
		}
		Song parentSong = null;
		if (songRequest.getParentSongId() != null) {
			parentSong = songRepository.findById(songRequest.getParentSongId()).get();
		} else {
			List<Song> parentSongs = songRepository.findAllByArtistAndTitleIgnoreCase(songRequest.getArtist(),
					songRequest.getTitle());
			if (parentSongs.size() > 0) {
				parentSong = parentSongs.get(0);
			}
		}

		User creator = userRepository.findByName(songRequest.getCreatorName()).get();

		Set<UserDto> editors = new HashSet<>();
		if (editors.size() > 0) {
			for (String editorName : songRequest.getEditorNames()) {
				editors.add(userMapper.userToDto(userRepository.findByName(editorName).get()));
			}
		}

		Timestamp finishedDate = null;
		if (songRequest.getPercentage() != null && songRequest.getPercentage() == 100) {
			if (initialSong != null) {
				if (initialSong.getPercentage() != null && initialSong.getPercentage() < 100) {
					finishedDate = new Timestamp(System.currentTimeMillis());
				} else {
					finishedDate = initialSong.getFinishedDate();
				}
			} else {
				finishedDate = new Timestamp(System.currentTimeMillis());
			}
		}

		return new SongDto(userMapper.userToDto(creator), songRequest.getArtist(), songRequest.getTitle(),
				songRequest.getVersion(), editors, songToDto(parentSong), songRequest.getLink(),
				songRequest.getPercentage(), songRequest.getGenre(), songRequest.getLanguage(),
				songRequest.getReleaseYear(), finishedDate, initialSong == null ? null : initialSong.getUpdatedDate());
	}

	public SongResponse songDtoToSongResponse(SongDto songDto) {
		if (songDto == null) {
			return null;
		}

		return new SongResponse(songDto.getId(), songDto.getArtist(), songDto.getTitle(),
				songDto.getCreator().getName(), songDto.getVersion(),
				songDto.getEditors().stream().map(e -> e.getName()).collect(Collectors.toSet()), songDto.getLink(),
				songDto.getPercentage(), songDto.getGenre(), songDto.getLanguage(), songDto.getReleaseYear(),
				songDto.getFinishedDate(), songDto.getUpdatedDate(),
				songDto.getParentSongDto() == null ? null : songDtoToSongResponse(songDto.getParentSongDto()));
	}

	public SongDto songToDto(Song song) {
		if (song == null) {
			return null;
		}

		SongDto songDto = new SongDto(userMapper.userToDto(song.getCreator()), song.getArtist(), song.getTitle(),
				song.getVersion(),
				song.getEditors().stream().map(e -> userMapper.userToDto(e)).collect(Collectors.toSet()),
				song.getParentSong() == null ? null : songToDto(song.getParentSong()), song.getLink(),
				song.getPercentage(), song.getGenre(), song.getLanguage(), song.getReleaseYear(),
				song.getFinishedDate(), song.getUpdatedDate());
		songDto.setId(song.getId());

		return songDto;
	}

	public Song dtoToSong(SongDto songDto) {
		if (songDto == null) {
			return null;
		}

		Song song = new Song(userMapper.dtoToUser(songDto.getCreator()), songDto.getArtist(), songDto.getTitle(),
				songDto.getVersion(),
				songDto.getEditors() == null ? null
						: songDto.getEditors().stream().map(e -> userMapper.dtoToUser(e)).collect(Collectors.toSet()),
				songDto == null ? null : dtoToSong(songDto.getParentSongDto()), songDto.getLink(),
				songDto.getPercentage(), songDto.getGenre(), songDto.getLanguage(), songDto.getReleaseYear(),
				songDto.getFinishedDate(), songDto.getUpdatedDate());
		song.setId(songDto.getId());

		return song;
	}
}