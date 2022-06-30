package de.us.web.bootstrap;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import de.us.web.converters.UserMapper;
import de.us.web.domain.Comment;
import de.us.web.domain.RoleEnum;
import de.us.web.domain.Song;
import de.us.web.domain.User;
import de.us.web.domain.UserSongRelation;
import de.us.web.repositories.CommentRepository;
import de.us.web.repositories.SongRepository;
import de.us.web.repositories.UserRepository;
import de.us.web.repositories.UserSongRelationRepository;
import de.us.web.services.CommentService;
import de.us.web.services.MessageService;

@Component
public class DummyDataBootstrap implements ApplicationListener<ContextRefreshedEvent> {
	private UserRepository userRepository;
	private SongRepository songRepository;
	private CommentRepository commentRepository;
	private UserSongRelationRepository userSongRelationRepository;
	private CommentService commentService;
	private MessageService messageService;

	@Autowired
	public DummyDataBootstrap(UserRepository userRepository, SongRepository songRepository,
			CommentRepository pageCommentRepository, UserSongRelationRepository userSongRelationRepository,
			CommentService commentService, UserMapper userMapper, MessageService messageService) {
		this.userRepository = userRepository;
		this.songRepository = songRepository;
		this.userSongRelationRepository = userSongRelationRepository;
		this.commentRepository = pageCommentRepository;
		this.commentService = commentService;
		this.messageService = messageService;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		initData();
	}

	private void initData() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Long currentSeconds = System.currentTimeMillis();
		Timestamp yesterday = new Timestamp(currentSeconds - 1000 * 60 * 60 * 24);
		Timestamp yesteryesterday = new Timestamp(currentSeconds - 1000 * 60 * 60 * 24 * 2);

		// PW: Fhfhfh13
		User user1 = new User("Jonas", "jonas@email1.de",
				"$2a$10$hWbK3Y4eWwaoa0o8FRYtNeXBDlUQwVZe7ZBXr7cdwt/Z15YqfiL7K", "$2a$10$lF8hL7g1FciPpeFsCXy.Fe",
				yesterday, RoleEnum.ROLE_ADMIN);
		User user2 = new User("Patricia", "janik@email.de", "test", "test", now, RoleEnum.ROLE_USER);
		User user3 = new User("Jonaw", "jan@email.de", "test", "test", now, RoleEnum.ROLE_USER);
		User user4 = new User("Eva", "david@email.de", "test", "test", now, RoleEnum.ROLE_USER);
		User user5 = new User("Annika", "daniel@email.de", "test", "test", now, RoleEnum.ROLE_USER);
		User user6 = new User("Timo", "lukas@email.de", "$2a$10$hWbK3Y4eWwaoa0o8FRYtNeXBDlUQwVZe7ZBXr7cdwt/Z15YqfiL7K",
				"$2a$10$lF8hL7g1FciPpeFsCXy.Fe", now, RoleEnum.ROLE_USER);
		User user7 = new User("Rene", "lukas@email.de", "test", "test", now, RoleEnum.ROLE_USER);
		User user8 = new User("Franziska", "lukas@email.de", "test", "test", now, RoleEnum.ROLE_USER);
		User user9 = new User("Julia", "lukas@email.de", "test", "test", now, RoleEnum.ROLE_USER);
		User user10 = new User("Benni", "lukas@email.de", "test", "test", now, RoleEnum.ROLE_USER);
		User[] users = { user1, user2, user3, user4, user5, user6, user7, user8, user9, user10 };
		userRepository.saveAll(Arrays.asList(users));

		Song song1 = new Song(user1, "Provinz", "Chaos", "Video Version", null, null,
				"https://www.youtube.com/watch?v=TOgWtlj5QMM", 100, "Indie", "German", 2019, now, null);
		Song song2 = new Song(user2, "Taylor Swift", "All Too Well",
				"10 Minute Version, Taylor’s Version, Video Version", null, null,
				"https://www.youtube.com/watch?v=BZ-rLBkUZf4", 100, "Pop", "English", 2021, now, null);
		Song song3 = new Song(user3, "Adele", "Easy on me", "Video Version", null, null, "", 100, "Pop", "English",
				2021, yesterday, now);
		Song song4 = new Song(user4, "The Killers", "Mr. Brightside", "Album Version", null, null, "", 100, "Rock",
				"English", 2004, now, null);
		Song song5 = new Song(user5, "The Beatles", "Let it be", "Single Version", null, null, "", 20, "Pop", "English",
				1970, null, null);
		Song song6 = new Song(user6, "Bruno Mars, Anderson.Paak, Silk Sonic", "Leave the door open", "Video Version",
				null, null, "", 100, "Pop", "English", 2021, now, null);
		Song song7 = new Song(user1, "Taylor Swift", "All Too Well",
				"10 Minute Version, Taylor’s Version, Live on Saturday Night Live", null, song2,
				"https://www.youtube.com/watch?v=nJr_8l0AEWE", 100, "Pop", "English", 2021, yesterday, null);
		Song song8 = new Song(user2, "Billie Eilish", "Happier Than Ever", "Video Version", null, null, "", 100, "Pop",
				"English", 2021, now, null);
		Song song9 = new Song(user3, "Taylor Swift", "All Too Well", "Spotify Version", null, song2,
				"https://open.spotify.com/track/6GNRkaWUB0Lwc19SdkTgx8?si=b575405fab964412", 100, "Pop", "English",
				2012, now, null);
		Song song10 = new Song(user3, "Coldplay", "Viva la vida", "Video Version", null, null, "", 40, "Pop", "English",
				2008, null, null);
		Song song11 = new Song(user5, "Justin Timberlake", "Mirrors", "Video Version", null, null, "", 60, "Pop",
				"English", 2013, null, null);
		Song song12 = new Song(user4, "Leoniden", "1990", "Spotify Version", null, null, "", 100, "Rock", "English",
				2017, now, null);
		Song song13 = new Song(user4, "WILLOW feat. Travis Barker", "Transparent Soul", "Spotify Version", null, null,
				"", 100, "Rock", "English", 2021, now, null);
		Song song14 = new Song(user4, "Ariana Grande", "7 Rings", "Spotify Version", null, null, "", 100, "Pop",
				"English", 2019, now, null);
		Song song15 = new Song(user4, "Avril Lavigne", "My happy ending", "Spotify Version", null, null, "", 100,
				"Rock", "English", 2004, now, null);
		Song song16 = new Song(user4, "Billie Eilish", "Happier Than Ever", "Spotify Version", null, song8, "", 100,
				"Pop", "English", 2021, now, null);
		Song song17 = new Song(user4, "Billie Eilish", "Your Power", "Spotify Version", null, null, "", 100, "Pop",
				"English", 2021, now, null);
		Song song18 = new Song(user4, "Dua Lipa", "Don't start now", "Spotify Version", null, null, "", 100, "Pop",
				"English", 2020, now, null);
		Song song19 = new Song(user4, "Echo & The Bunnymen", "The Killing Moon", "Spotify Version", null, null, "", 100,
				"Rock", "English", 1985, now, null);
		Song song20 = new Song(user4, "Ed Sheeran", "Bad habits", "Spotify Version", null, null, "", 100, "Pop",
				"English", 2021, now, null);
		Song song21 = new Song(user4, "OutKast", "Ms. Jackson", "Spotify Version", null, null, "", 100, "Rap",
				"English", 2000, now, null);
		Song song22 = new Song(user4, "Wheatus", "Teenage Dirtback", "Spotify Version", null, null, "", 100, "Rock",
				"English", 1999, yesterday, null);
		Song song23 = new Song(user4, "Queen", "Don't stop me now", "Spotify Version", null, null, "", 100, "Rock",
				"English", 1978, yesterday, null);
		Song song24 = new Song(user4, "Queen", "Don't stop me now", "Revisited, Spotify Version", null, song23, "", 100,
				"Rock", "English", 2018, now, null);
		Song song25 = new Song(user1, "Leoniden feat. Pabst", "Freaks", "Video Version", null, null, "", 100, "Rock",
				"English", 2021, yesteryesterday, null);
		song2.addEditor(user1);
		song2.addEditor(user6);
		song7.addEditor(user2);

		Song[] songs = new Song[] { song1, song2, song3, song4, song5, song6, song7, song8, song9, song10, song11,
				song12, song13, song14, song15, song16, song17, song18, song19, song20, song21, song22, song23, song24,
				song25 };
		songRepository.saveAll(Arrays.asList(songs));

		for (User user : users) {
			for (Song song : songs) {
				if (song.getArtist() == "Adele" && user.getName() == "Jonas") {
					UserSongRelation userSongRelation = new UserSongRelation(user, song);
					userSongRelation.setDownloadDate(yesterday);
					userSongRelation.setRating(5);
					userSongRelation.setFavorite(true);
					userSongRelationRepository.save(userSongRelation);
				} else if (song.getCreator() != user) {
					Random ran = new Random();
					int hasRelation = ran.nextInt(2);
					int hasRated = ran.nextInt(2);
					int hasFavorited = ran.nextInt(2);
					if (hasRelation == 1) {
						UserSongRelation userSongRelation = new UserSongRelation(user, song);
						userSongRelation.setDownloadDate(now);
						if (hasRated == 1 && song.getPercentage() == 100) {
							userSongRelation.setRating(ran.nextInt(6));
						}
						if (hasFavorited == 1) {
							userSongRelation.setFavorite(true);
						}
						userSongRelationRepository.save(userSongRelation);
					}
				}
			}
		}

		commentRepository.save(new Comment(Long.valueOf(-1), user1, "First comment!",
				new Timestamp((long) (currentSeconds - 1000 * 60 * 60 * 118.6))));
		commentRepository.save(new Comment(Long.valueOf(-1), user1, "Great work!",
				new Timestamp((long) (currentSeconds - 1000 * 60 * 60 * 108.5))));
		commentRepository.save(new Comment(Long.valueOf(-1), user1,
				"The Top 50 page is very odd. Who would download these modern songs?",
				new Timestamp((long) (currentSeconds - 1000 * 60 * 60 * 104.4))));
		commentRepository.save(new Comment(Long.valueOf(-1), user2,
				"Kennt irgendjemand noch weitere alternative Webseiten, auf denen ich Karaoke-Dateien beziehen kann? Ich kenne sonst nur USDB und dieses italienische Forum.",
				new Timestamp((long) (currentSeconds - 1000 * 60 * 60 * 49.5))));
		commentRepository
				.save(new Comment(Long.valueOf(-1), user3, "Just downloaded my first song. Can't wait to sing it!!!",
						new Timestamp((long) (currentSeconds - 1000 * 60 * 60 * 48.2))));
		commentRepository.save(new Comment(Long.valueOf(-1), user4, "It is nice to see the commnity growing",
				new Timestamp((long) (currentSeconds - 1000 * 60 * 60 * 25))));
		commentRepository.save(new Comment(Long.valueOf(-1), user5, "Wspaniały",
				new Timestamp((long) (currentSeconds - 1000 * 60 * 60 * 17.8))));
		commentRepository.save(
				new Comment(Long.valueOf(-1), user6, "Do you guys prefer UltraStar Deluxe or UltraStar Worldparty",
						new Timestamp(currentSeconds - 1000 * 60 * 60 * 3)));
		commentRepository.save(new Comment(Long.valueOf(-1), user7, "I prefer UltraStar Deluxe!",
				new Timestamp((long) (currentSeconds - 1000 * 60 * 60 * 2.2))));
		commentRepository.save(new Comment(Long.valueOf(-1), user8, "I think Worldparty because of the design",
				new Timestamp((long) (currentSeconds - 1000 * 60 * 60 * 1.7))));
		commentRepository.save(new Comment(Long.valueOf(-1), user9,
				"Het zou leuk zijn als iemand het nieuwe nummer van Froukje zou kunnen uploaden",
				new Timestamp((long) (currentSeconds - 1000 * 60 * 60 * 1.2))));
		commentRepository
				.save(new Comment(Long.valueOf(-1), user10, "Just started creating my first file! Wish me luck :)",
						new Timestamp((long) (currentSeconds - 1000 * 60 * 60 * 0.2))));

		commentService.saveComment(
				new Comment(Long.valueOf(7), user4, "Thanks, works perfect with the official video!", yesterday));
		commentService.saveComment(new Comment(Long.valueOf(7), user2, "Works best with GAP 10300",
				new Timestamp((long) (currentSeconds - 1000 * 60 * 60 * 1.2))));

		messageService.saveSongUpdatedMessage(Long.valueOf(3));
	}
}