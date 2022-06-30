package de.us.web.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.us.web.domain.Song;
import de.us.web.domain.User;

@Repository
public interface SongRepository extends CrudRepository<Song, Long> {
	List<Song> findByParentSong(Song song);
	List<Song> findAllByOrderByArtistAscTitleAscVersionAscCreatorAsc();
	List<Song> findAllByOrderByArtistDescTitleAscVersionAscCreatorAsc();
	List<Song> findAllByOrderByTitleAscArtistAscVersionAscCreatorAsc();
	List<Song> findAllByOrderByTitleDescArtistAscVersionAscCreatorAsc();
	List<Song> findAllByOrderByVersionAscArtistAscTitleAscVersionAscCreatorAsc();
	List<Song> findAllByOrderByVersionDescArtistAscTitleAscVersionAscCreatorAsc();
	List<Song> findAllByOrderByCreatorAscArtistAscTitleAscVersionAsc();
	List<Song> findAllByOrderByCreatorDescArtistAscTitleAscVersionAsc();
	List<Song> findAllByOrderByPercentageAscArtistAscTitleAscVersionAscCreatorAsc();
	List<Song> findAllByOrderByPercentageDescArtistAscTitleAscVersionAscCreatorAsc();
	List<Song> findAllByFinishedDateIsNotNull();
	List<Song> findAllByFinishedDateBetweenOrderByArtistAscTitleAscVersionAsc(Timestamp first, Timestamp second);
	List<Song> findAllByArtistAndTitleIgnoreCase(String artist, String title);
	List<Song> findAllByArtistAndTitleAndVersionIgnoreCase(String artist, String title, String version);
	List<Song> findByFinishedDateIsNotNullOrderByFinishedDateDescTitleAscArtistAscVersionAsc(Pageable pageable);
	List<Song> findAllByCreator(User creator);
}
