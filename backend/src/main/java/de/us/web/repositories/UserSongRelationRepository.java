package de.us.web.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.us.web.domain.Song;
import de.us.web.domain.User;
import de.us.web.domain.UserSongRelation;

@Repository
public interface UserSongRelationRepository extends CrudRepository<UserSongRelation, Long> {
	List<UserSongRelation> findBySong(Song song);
	Optional<UserSongRelation> findByUserAndSong(User user, Song song);
}
